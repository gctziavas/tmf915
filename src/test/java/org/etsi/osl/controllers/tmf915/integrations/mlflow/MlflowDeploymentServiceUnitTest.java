package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MlflowDeploymentServiceUnitTest {

    private MlflowDeploymentService deploymentService;
    private MockedConstruction<ProcessBuilder> mockedProcessBuilder;

    @BeforeEach
    void setUp() {
        deploymentService = new MlflowDeploymentService();
        ReflectionTestUtils.setField(deploymentService, "trackingUri", "http://localhost:5000");
        ReflectionTestUtils.setField(deploymentService, "commandTimeoutSeconds", 30);
    }
    
    @AfterEach
    void tearDown() {
        if (mockedProcessBuilder != null) {
            mockedProcessBuilder.close();
        }
    }

    @Test
    void imageExists_callsDockerInspect() throws Exception {
        Process mockProcess = mock(Process.class);
        when(mockProcess.waitFor(anyLong(), eq(TimeUnit.SECONDS))).thenReturn(true);
        when(mockProcess.exitValue()).thenReturn(0);

        mockedProcessBuilder = mockConstruction(ProcessBuilder.class, (mock, context) -> {
            when(mock.environment()).thenReturn(new java.util.HashMap<>());
            when(mock.start()).thenReturn(mockProcess);
            String[] command = (String[]) context.arguments().get(0);
            assertEquals("docker", command[0]);
            assertEquals("image", command[1]);
            assertEquals("inspect", command[2]);
            assertEquals("my-image", command[3]);
        });

        boolean exists = deploymentService.imageExists("my-image", null);
        assertTrue(exists);
        assertEquals(1, mockedProcessBuilder.constructed().size());
    }

    @Test
    void stopContainer_callsDockerStopAndRm() throws Exception {
        Process mockProcess = mock(Process.class);
        when(mockProcess.waitFor(anyLong(), eq(TimeUnit.SECONDS))).thenReturn(true);
        when(mockProcess.exitValue()).thenReturn(0);

        mockedProcessBuilder = mockConstruction(ProcessBuilder.class, (mock, context) -> {
            when(mock.environment()).thenReturn(new java.util.HashMap<>());
            when(mock.start()).thenReturn(mockProcess);
        });

        boolean stopped = deploymentService.stopContainer("c123", true, null);
        
        assertTrue(stopped);
        assertEquals(2, mockedProcessBuilder.constructed().size()); // one for stop, one for rm
    }
}
