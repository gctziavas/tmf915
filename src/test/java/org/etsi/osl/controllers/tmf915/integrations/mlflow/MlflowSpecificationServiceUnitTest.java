package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/** Handoff 4.5: synced specifications carry a lifecycleStatus. */
class MlflowSpecificationServiceUnitTest {

    @Test
    void loggedModelSpecification_getsTheConfiguredLifecycleStatus() {
        MlflowSpecificationService service = new MlflowSpecificationService(mock(MlflowClientService.class));
        ReflectionTestUtils.setField(service, "defaultLifecycleStatus", "Active");

        LoggedModel.LoggedModelInfo info = new LoggedModel.LoggedModelInfo();
        info.setModelId("m-8a1e");
        info.setExperimentId("1");
        LoggedModel model = new LoggedModel();
        model.setInfo(info);

        AiModelSpecificationCreate spec = service.createSpecificationFromLoggedModel(model, "iris-classifier", "");

        assertEquals("Active", spec.getLifecycleStatus());
        assertEquals("iris-classifier", spec.getName());
        assertEquals("m-8a1e", spec.getVersion());
    }
}
