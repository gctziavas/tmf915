package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.mlflow.tracking.MlflowClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "mlflow.enabled", havingValue = "true")
public class MlflowConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MlflowConfiguration.class);

    @Value("${mlflow.tracking-uri}")
    private String trackingUri;

    @Value("${mlflow.connection-timeout:30000}")
    private int connectionTimeout;

    @Value("${mlflow.read-timeout:60000}")
    private int readTimeout;

    @Bean
    public MlflowClient mlflowClient() {
        log.info("Initialising MlflowClient with tracking URI: {}", trackingUri);
        return new MlflowClient(trackingUri);
    }

    public String getTrackingUri() {
        return trackingUri;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }
}
