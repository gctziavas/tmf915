package org.etsi.osl.controllers.tmf915.artifacts;

import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * The S3 (MinIO) client, shared by model downloads ({@link S3ArtifactStore}) and the MLflow
 * sync's artifact check. Created only when an endpoint and both keys are configured.
 *
 * <p>Settings: {@code mlflow.s3.endpoint.url}, {@code mlflow.s3.access.key},
 * {@code mlflow.s3.secret.key} (environment {@code MLFLOW_S3_ENDPOINT_URL}, …, as before), or
 * the {@code mlflow.s3-endpoint-url}, {@code mlflow.s3-access-key}, {@code mlflow.s3-secret-key}
 * keys in {@code application.yml}.
 */
@Configuration
public class S3Configuration {

    private static final Logger log = LoggerFactory.getLogger(S3Configuration.class);

    static final String ENDPOINT = "${mlflow.s3.endpoint.url:${mlflow.s3-endpoint-url:}}";
    static final String ACCESS_KEY = "${mlflow.s3.access.key:${mlflow.s3-access-key:}}";
    static final String SECRET_KEY = "${mlflow.s3.secret.key:${mlflow.s3-secret-key:}}";

    @Bean
    @Conditional(S3Configured.class)
    public MinioClient artifactMinioClient(Environment environment) {
        String endpoint = environment.resolvePlaceholders(ENDPOINT);
        log.info("Initialising S3 client for model artifacts at {}", endpoint);
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(environment.resolvePlaceholders(ACCESS_KEY), environment.resolvePlaceholders(SECRET_KEY))
                .build();
    }

    @Bean
    @Conditional(S3Configured.class)
    public S3ArtifactStore s3ArtifactStore(MinioClient artifactMinioClient) {
        return new S3ArtifactStore(artifactMinioClient);
    }

    /** True when the S3 endpoint and both keys are set. */
    static class S3Configured implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Environment env = context.getEnvironment();
            return !env.resolvePlaceholders(ENDPOINT).isBlank()
                    && !env.resolvePlaceholders(ACCESS_KEY).isBlank()
                    && !env.resolvePlaceholders(SECRET_KEY).isBlank();
        }
    }
}
