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
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Exercises GET /aiModel through the application's real message converters (Jackson 3 in
 * Spring Boot 4) and a real H2 database, so the response shape and query count match production.
 *
 * <p>MockMvc is built without the security filter chain; {@code @PreAuthorize} is satisfied by
 * putting a ROLE_USER authentication in the SecurityContextHolder for the test thread.
 */
@SpringBootTest(properties = {
        "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost/certs",
        "spring.jpa.properties.hibernate.generate_statistics=true",
        "spring.jpa.show-sql=false",
        "springdoc.oAuthFlow.authorizationUrl=http://localhost/auth",
        "springdoc.oAuthFlow.tokenUrl=http://localhost/token",
        "mlflow.enabled=true",
        "mlflow.sync.enabled=false",
        "mlflow.tracking-uri=http://127.0.0.1:1"
})
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

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
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

    private static org.etsi.osl.controllers.tmf915.model.AiModelCreate toCreate(AiModel model) {
        org.etsi.osl.controllers.tmf915.model.AiModelCreate create = new org.etsi.osl.controllers.tmf915.model.AiModelCreate();
        create.setName(model.getName());
        create.setAiModelSpecification(model.getAiModelSpecification());
        create.setServiceCharacteristic(model.getServiceCharacteristic());
        return create;
    }
}
