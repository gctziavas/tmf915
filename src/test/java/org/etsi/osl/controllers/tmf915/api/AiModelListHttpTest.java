package org.etsi.osl.controllers.tmf915.api;

import jakarta.persistence.EntityManagerFactory;
import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.Characteristic;
import org.etsi.osl.controllers.tmf915.model.CharacteristicSpecification;
import org.etsi.osl.controllers.tmf915.repo.AiModelRepository;
import org.etsi.osl.controllers.tmf915.repo.AiModelSpecificationRepository;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelRepositoryService;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Exercises GET /aiModel through the application's real message converters (Jackson 3 in
 * Spring Boot 4) and a real H2 database, so the response shape and query count match production.
 *
 * <p>MockMvc is built without the security filter chain; {@code @PreAuthorize} is satisfied by
 * putting a ROLE_USER authentication in the SecurityContextHolder for the test thread.
 */
// Runs with src/main/resources/application.yml plus the overrides in
// src/test/resources/application.properties (H2, no MLflow).
@SpringBootTest(properties = "spring.jpa.properties.hibernate.generate_statistics=true")
class AiModelListHttpTest {

    private static final String BASE = "/tmf-api/AiM/v4";
    private static final int MODEL_COUNT = 3;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AiModelSpecificationRepository specificationRepository;

    @Autowired
    private AiModelRepository aiModelRepository;

    @Autowired
    private AiModelRepositoryService aiModelRepositoryService;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // server.servlet.context-path is /tmf-api in application.yml; MockMvc does not apply it
        // on its own, so requests carry it explicitly and use the same URLs as production.
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .defaultRequest(get("/").contextPath("/tmf-api"))
                .build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                "tester", "n/a", List.of(new SimpleGrantedAuthority("ROLE_USER"))));

        // One distinct specification per model, so a per-model lazy load shows up as N loads.
        for (int i = 0; i < MODEL_COUNT; i++) {
            AiModelSpecification spec = new AiModelSpecification();
            spec.setId("spec-" + i);
            spec.setName("spec-name-" + i);
            spec.setVersion("v" + i);
            spec.setDescription("SPEC_DESCRIPTION_MUST_NOT_LEAK");
            spec.setModelDataSheet(Map.of("blob", "MODEL_DATA_SHEET_MUST_NOT_LEAK"));
            CharacteristicSpecification cs = new CharacteristicSpecification();
            cs.setName("SPEC_CHARACTERISTIC_MUST_NOT_LEAK");
            spec.setSpecCharacteristic(new ArrayList<>(List.of(cs)));
            specificationRepository.save(spec);

            AiModel model = new AiModel();
            model.setName("model-" + i);
            model.setAiModelSpecification(spec);
            Characteristic c = new Characteristic();
            c.setName("platform");
            c.setValue("mlflow");
            model.setServiceCharacteristic(new ArrayList<>(List.of(c)));
            // Goes through the service so the stored specification reference is filled
            // the same way production creates models.
            aiModelRepositoryService.createAiModel(toCreate(model));
        }
    }

    @AfterEach
    void tearDown() {
        aiModelRepository.deleteAll();
        specificationRepository.deleteAll();
        SecurityContextHolder.clearContext();
    }

    @Test
    void listAiModel_returnsSpecificationAsSlimReference() throws Exception {
        MvcResult result = mockMvc.perform(get(BASE + "/aiModel"))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertFalse(body.contains("specCharacteristic"), body);
        assertFalse(body.contains("modelDataSheet"), body);
        assertFalse(body.contains("MUST_NOT_LEAK"), body);

        mockMvc.perform(get(BASE + "/aiModel"))
                .andExpect(jsonPath("$", hasSize(MODEL_COUNT)))
                .andExpect(jsonPath("$[0].aiModelSpecification.id", startsWith("spec-")))
                .andExpect(jsonPath("$[0].aiModelSpecification.name", startsWith("spec-name-")))
                .andExpect(jsonPath("$[0].aiModelSpecification.version", startsWith("v")))
                .andExpect(jsonPath("$[0].aiModelSpecification.href",
                        startsWith(BASE + "/aiModelSpecification/spec-")));
    }

    @Test
    void listAiModel_doesNotLoadSpecificationsPerModel() throws Exception {
        Statistics stats = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        stats.clear();

        mockMvc.perform(get(BASE + "/aiModel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(MODEL_COUNT)))
                .andExpect(jsonPath("$[0].serviceCharacteristic[0].name").value("platform"));

        long specLoads = stats.getEntityStatistics(AiModelSpecification.class.getName()).getLoadCount();
        assertEquals(0, specLoads, "AiModelSpecification rows loaded while listing AiModels");
        // 1 query for the page of models + 1 batched query for all their characteristics.
        // Without @BatchSize this would be 1 + MODEL_COUNT.
        assertEquals(2, stats.getPrepareStatementCount(), "SQL statements issued while listing AiModels");
    }

    @Test
    void listAiModel_honoursLimitAndOffset() throws Exception {
        mockMvc.perform(get(BASE + "/aiModel").param("limit", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(get(BASE + "/aiModel").param("offset", "2").param("limit", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // Offset not a multiple of limit must still start at the requested row.
        MvcResult all = mockMvc.perform(get(BASE + "/aiModel")).andReturn();
        MvcResult shifted = mockMvc.perform(get(BASE + "/aiModel").param("offset", "1").param("limit", "2"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andReturn();
        String secondId = com.jayway.jsonpath.JsonPath.read(all.getResponse().getContentAsString(), "$[1].id");
        String firstShiftedId = com.jayway.jsonpath.JsonPath.read(shifted.getResponse().getContentAsString(), "$[0].id");
        assertEquals(secondId, firstShiftedId);
    }

    @Test
    void retrieveAiModelSpecification_stillReturnsFullSpecification() throws Exception {
        mockMvc.perform(get(BASE + "/aiModelSpecification/spec-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("spec-1"))
                .andExpect(jsonPath("$.name").value("spec-name-1"))
                .andExpect(jsonPath("$.description").value("SPEC_DESCRIPTION_MUST_NOT_LEAK"))
                .andExpect(jsonPath("$.modelDataSheet.blob").value("MODEL_DATA_SHEET_MUST_NOT_LEAK"))
                .andExpect(jsonPath("$.specCharacteristic[0].name").value("SPEC_CHARACTERISTIC_MUST_NOT_LEAK"))
                .andExpect(jsonPath("$.href").value(BASE + "/aiModelSpecification/spec-1"));
    }

    @Test
    void listAiModelSpecification_returnsFullSpecificationsAndHonoursPaging() throws Exception {
        mockMvc.perform(get(BASE + "/aiModelSpecification"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(MODEL_COUNT)))
                .andExpect(jsonPath("$[0].id").value("spec-0"))
                .andExpect(jsonPath("$[0].modelDataSheet.blob").value("MODEL_DATA_SHEET_MUST_NOT_LEAK"))
                .andExpect(jsonPath("$[0].specCharacteristic[0].name").value("SPEC_CHARACTERISTIC_MUST_NOT_LEAK"));

        // offset=1, limit=2 must start at the second row. The old PageRequest.of(1 / 2, 2)
        // returned page 0, i.e. spec-0 and spec-1.
        mockMvc.perform(get(BASE + "/aiModelSpecification").param("offset", "1").param("limit", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("spec-1"))
                .andExpect(jsonPath("$[1].id").value("spec-2"));
    }

    @Test
    void listAiModelSpecification_batchesCharacteristicLoads() throws Exception {
        Statistics stats = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        stats.clear();

        mockMvc.perform(get(BASE + "/aiModelSpecification"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(MODEL_COUNT)));

        // 1 query for the page of specifications + 1 batched query for their characteristics
        // (the Page result skips the count query when the first page is not full).
        assertEquals(2, stats.getPrepareStatementCount(), "SQL statements issued while listing specifications");
    }

    // ── Part 2: open-in-view off, read-only transactions, indexes ──────────

    @Test
    void retrieveAiModel_worksWithoutOpenSessionInView() throws Exception {
        String id = aiModelRepositoryService.findAiModels(0, 1).get(0).getId();
        mockMvc.perform(get(BASE + "/aiModel/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceCharacteristic[0].name").value("platform"))
                .andExpect(jsonPath("$.aiModelSpecification.name", startsWith("spec-name-")));
    }

    @Test
    void patchAiModel_mergesCharacteristicsWithoutOpenSessionInView() throws Exception {
        String id = aiModelRepositoryService.findAiModels(0, 1).get(0).getId();
        mockMvc.perform(patch(BASE + "/aiModel/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"patched\",\"serviceCharacteristic\":[{\"name\":\"extra\",\"value\":\"x\"}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("patched"))
                .andExpect(jsonPath("$.serviceCharacteristic", hasSize(2)));

        mockMvc.perform(get(BASE + "/aiModel/" + id))
                .andExpect(jsonPath("$.description").value("patched"))
                .andExpect(jsonPath("$.serviceCharacteristic", hasSize(2)));
    }

    @Test
    void patchAiModelSpecification_mergesCharacteristicsWithoutOpenSessionInView() throws Exception {
        mockMvc.perform(patch(BASE + "/aiModelSpecification/spec-0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"patched\",\"specCharacteristic\":[{\"name\":\"extra\"}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("patched"))
                .andExpect(jsonPath("$.specCharacteristic", hasSize(2)));

        mockMvc.perform(get(BASE + "/aiModelSpecification/spec-0"))
                .andExpect(jsonPath("$.description").value("patched"))
                .andExpect(jsonPath("$.specCharacteristic", hasSize(2)));
    }

    @Test
    void listAiModel_legacyRowWithoutStoredReference_stillReturnsReference() throws Exception {
        // Saved straight through the repository: no stored serviceSpecification reference,
        // as for rows created before the reference was populated.
        AiModel legacy = new AiModel();
        legacy.setId("zz-legacy");
        legacy.setName("legacy");
        legacy.setAiModelSpecification(specificationRepository.findById("spec-2").orElseThrow());
        aiModelRepository.save(legacy);

        mockMvc.perform(get(BASE + "/aiModel/zz-legacy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aiModelSpecification.id").value("spec-2"))
                .andExpect(jsonPath("$.aiModelSpecification.name").value("spec-name-2"));
    }

    @Test
    void readOnlyFindInsideOuterTransaction_doesNotSwallowLaterUpdate() {
        // The deployment scheduler calls findAiModelById (read-only) and then updates the same
        // model inside one outer TransactionTemplate. The read-only hint must not stop the
        // update from being flushed.
        String id = aiModelRepositoryService.findAiModels(0, 1).get(0).getId();
        new TransactionTemplate(transactionManager).executeWithoutResult(status -> {
            aiModelRepositoryService.findAiModelById(id);
            aiModelRepositoryService.updateAiModelState(id, ServiceStateType.TERMINATED);
        });
        assertEquals(ServiceStateType.TERMINATED, aiModelRepositoryService.findAiModelById(id).getState());
    }

    @Test
    void openSessionInViewIsOff() {
        assertEquals(0, context.getBeanNamesForType(
                org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor.class).length);
    }

    @Test
    void indexesExist() {
        List<String> indexes = jdbcTemplate.queryForList(
                "SELECT LOWER(INDEX_NAME) FROM INFORMATION_SCHEMA.INDEXES", String.class);
        assertTrue(indexes.contains("idx_aim915_aim_state"), indexes.toString());
        assertTrue(indexes.contains("idx_aim915_aim_name"), indexes.toString());
        assertTrue(indexes.contains("idx_aim915_aimspec_name_version"), indexes.toString());
    }

    // ── Part 2: fields selection and null exclusion ──────────────────────

    @Test
    void listAiModelSpecification_withFields_returnsOnlyRequestedAttributes() throws Exception {
        String body = mockMvc.perform(get(BASE + "/aiModelSpecification").param("fields", "name, version"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(MODEL_COUNT)))
                .andExpect(jsonPath("$[0].id").value("spec-0"))
                .andExpect(jsonPath("$[0].href").value(BASE + "/aiModelSpecification/spec-0"))
                .andExpect(jsonPath("$[0].@type").value("AIModelSpecification"))
                .andExpect(jsonPath("$[0].name").value("spec-name-0"))
                .andExpect(jsonPath("$[0].version").value("v0"))
                .andReturn().getResponse().getContentAsString();

        List<Map<String, Object>> items = com.jayway.jsonpath.JsonPath.read(body, "$");
        for (Map<String, Object> item : items) {
            assertEquals(java.util.Set.of("id", "href", "@type", "name", "version"), item.keySet(), body);
        }
    }

    @Test
    void retrieveAiModelSpecification_withFields_returnsOnlyRequestedAttributes() throws Exception {
        String body = mockMvc.perform(get(BASE + "/aiModelSpecification/spec-1").param("fields", "specCharacteristic"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.specCharacteristic[0].name").value("SPEC_CHARACTERISTIC_MUST_NOT_LEAK"))
                .andReturn().getResponse().getContentAsString();
        Map<String, Object> item = com.jayway.jsonpath.JsonPath.read(body, "$");
        assertEquals(java.util.Set.of("id", "href", "@type", "specCharacteristic"), item.keySet(), body);
    }

    @Test
    void withFields_doesNotModifyStoredSpecification() throws Exception {
        mockMvc.perform(get(BASE + "/aiModelSpecification").param("fields", "name")).andExpect(status().isOk());
        mockMvc.perform(get(BASE + "/aiModelSpecification/spec-0"))
                .andExpect(jsonPath("$.modelDataSheet.blob").value("MODEL_DATA_SHEET_MUST_NOT_LEAK"))
                .andExpect(jsonPath("$.specCharacteristic", hasSize(1)));
    }

    @Test
    void listAiModel_withFields_keepsSpecificationReference() throws Exception {
        String body = mockMvc.perform(get(BASE + "/aiModel").param("fields", "name,aiModelSpecification"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(MODEL_COUNT)))
                .andExpect(jsonPath("$[0].@type").value("AIModel"))
                .andExpect(jsonPath("$[0].name", startsWith("model-")))
                .andExpect(jsonPath("$[0].aiModelSpecification.id", startsWith("spec-")))
                .andExpect(jsonPath("$[0].aiModelSpecification.name", startsWith("spec-name-")))
                .andReturn().getResponse().getContentAsString();

        List<Map<String, Object>> items = com.jayway.jsonpath.JsonPath.read(body, "$");
        for (Map<String, Object> item : items) {
            assertEquals(java.util.Set.of("id", "href", "@type", "name", "aiModelSpecification"),
                    item.keySet(), body);
        }
    }

    @Test
    void retrieveAiModel_withFields_returnsOnlyRequestedAttributes() throws Exception {
        String id = aiModelRepositoryService.findAiModels(0, 1).get(0).getId();
        String body = mockMvc.perform(get(BASE + "/aiModel/" + id).param("fields", "serviceCharacteristic"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceCharacteristic[0].name").value("platform"))
                .andReturn().getResponse().getContentAsString();
        Map<String, Object> item = com.jayway.jsonpath.JsonPath.read(body, "$");
        assertEquals(java.util.Set.of("id", "href", "@type", "serviceCharacteristic"), item.keySet(), body);
    }

    @Test
    void fields_isIgnoredOnWrites() throws Exception {
        String id = aiModelRepositoryService.findAiModels(0, 1).get(0).getId();
        mockMvc.perform(patch(BASE + "/aiModel/" + id).param("fields", "name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"patched\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("patched"))
                .andExpect(jsonPath("$.serviceCharacteristic[0].name").value("platform"));
    }

    // ── Bug-fix plan PR 1: deployment inputs (handoff 5.1, 5.13) ─────────

    @Test
    void createWithServerOwnedCharacteristics_storesNoneOfThem() throws Exception {
        String body = mockMvc.perform(post(BASE + "/aiModel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"probe\",\"state\":\"designed\",\"aiModelSpecification\":{\"id\":\"spec-0\"},\"software\":[{\"id\":\"mlflow\"}],\"serviceCharacteristic\":["
                                + "{\"name\":\"platform\",\"value\":\"mlflow\"},"
                                + "{\"name\":\"containerId\",\"value\":\"tmf915-mysql-1\"},"
                                + "{\"name\":\"imageName\",\"value\":\"mysql:8.0\"}]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.serviceCharacteristic", hasSize(1)))
                .andExpect(jsonPath("$.serviceCharacteristic[0].name").value("platform"))
                .andReturn().getResponse().getContentAsString();
        String id = com.jayway.jsonpath.JsonPath.read(body, "$.id");

        mockMvc.perform(get(BASE + "/aiModel/" + id))
                .andExpect(jsonPath("$.serviceCharacteristic", hasSize(1)));
    }

    @Test
    void patchWithServerOwnedCharacteristics_doesNotStoreThem() throws Exception {
        String id = aiModelRepositoryService.findAiModels(0, 1).get(0).getId();
        mockMvc.perform(patch(BASE + "/aiModel/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"serviceCharacteristic\":[{\"name\":\"containerId\",\"value\":\"tmf915-mysql-1\"}]}"))
                .andExpect(status().isOk());

        String body = mockMvc.perform(get(BASE + "/aiModel/" + id))
                .andReturn().getResponse().getContentAsString();
        assertFalse(body.contains("containerId"), body);
    }

    @Test
    void createWithDisallowedDockerHost_returns400() throws Exception {
        mockMvc.perform(post(BASE + "/aiModel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"probe\",\"state\":\"designed\",\"aiModelSpecification\":{\"id\":\"spec-0\"},\"software\":[{\"id\":\"mlflow\"}],\"serviceCharacteristic\":["
                                + "{\"name\":\"dockerHost\",\"value\":\"tcp://attacker.example:2375\"}]}"))
                .andExpect(status().isBadRequest())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
                        .string(containsString("mlflow.docker.allowed-hosts")));
    }

    @Test
    void patchToActive_returns400() throws Exception {
        String id = aiModelRepositoryService.findAiModels(0, 1).get(0).getId();
        mockMvc.perform(patch(BASE + "/aiModel/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"state\":\"active\"}"))
                .andExpect(status().isBadRequest());
    }

    // ── Bug-fix plan PR 3: webhook callbacks (handoff 5.7) ──────────────

    @Test
    void hubRejectsCallbackToCloudMetadata() throws Exception {
        mockMvc.perform(post(BASE + "/hub")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"callback\":\"http://169.254.169.254/latest/meta-data/\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
                        .string(containsString("internal address")));
    }

    @Test
    void hubAcceptsPublicCallback() throws Exception {
        mockMvc.perform(post(BASE + "/hub")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"callback\":\"https://93.184.216.34/tmf915/events\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void responsesLeaveOutNullFields() throws Exception {
        String models = mockMvc.perform(get(BASE + "/aiModel")).andReturn().getResponse().getContentAsString();
        String specs = mockMvc.perform(get(BASE + "/aiModelSpecification")).andReturn().getResponse().getContentAsString();
        assertFalse(models.contains(":null"), models);
        assertFalse(specs.contains(":null"), specs);
    }

    private static org.etsi.osl.controllers.tmf915.model.AiModelCreate toCreate(AiModel model) {
        org.etsi.osl.controllers.tmf915.model.AiModelCreate create = new org.etsi.osl.controllers.tmf915.model.AiModelCreate();
        create.setName(model.getName());
        create.setAiModelSpecification(model.getAiModelSpecification());
        create.setServiceCharacteristic(model.getServiceCharacteristic());
        return create;
    }
}
