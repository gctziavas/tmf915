package org.etsi.osl.controllers.tmf915.artifacts;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.ServerException;
import io.minio.messages.Item;
import okhttp3.Headers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Clock;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** {@link S3ArtifactStore} against a fake MinIO client. */
class S3ArtifactStoreTest {

    private static final URI LOCATION = URI.create("s3://mlflow/1/models/m-1/artifacts");
    private static final String PREFIX = "1/models/m-1/artifacts/";

    private final MinioClient minio = mock(MinioClient.class);
    private final S3ArtifactStore store = new S3ArtifactStore(minio);

    private static Result<Item> object(String key, long size) {
        Item item = mock(Item.class);
        when(item.objectName()).thenReturn(key);
        when(item.size()).thenReturn(size);
        when(item.isDir()).thenReturn(key.endsWith("/"));
        return new Result<>(item);
    }

    private void objects(List<Result<Item>> results) {
        when(minio.listObjects(any(ListObjectsArgs.class))).thenReturn(results);
    }

    @Test
    void supportsS3Only() {
        assertTrue(store.supports(LOCATION));
        assertTrue(store.supports(URI.create("S3://mlflow/x")));
        assertFalse(store.supports(URI.create("https://mlflow/x")));
        assertFalse(store.supports(URI.create("oci://registry/x")));
    }

    @Test
    void listsObjectsUnderThePrefix_relativeToIt() throws IOException {
        objects(List.of(
                object(PREFIX + "MLmodel", 120),
                object(PREFIX + "model/", 0),
                object(PREFIX + "model/model.pkl", 4096),
                object(PREFIX + "requirements.txt", 30)));

        List<ArtifactFile> files = store.list(LOCATION);

        assertEquals(List.of(new ArtifactFile("MLmodel", 120), new ArtifactFile("model/model.pkl", 4096),
                new ArtifactFile("requirements.txt", 30)), files);
        ArgumentCaptor<ListObjectsArgs> args = ArgumentCaptor.forClass(ListObjectsArgs.class);
        verify(minio).listObjects(args.capture());
        assertEquals("mlflow", args.getValue().bucket());
        assertEquals(PREFIX, args.getValue().prefix());
        assertTrue(args.getValue().recursive());
    }

    @Test
    void prefixEndsWithASlash_soSiblingDirectoriesAreNotIncluded() throws IOException {
        objects(List.of());
        store.list(URI.create("s3://mlflow/1/models/m-1"));
        ArgumentCaptor<ListObjectsArgs> args = ArgumentCaptor.forClass(ListObjectsArgs.class);
        verify(minio).listObjects(args.capture());
        assertEquals("1/models/m-1/", args.getValue().prefix());
    }

    @Test
    void nothingStored_isAnEmptyList() throws IOException {
        objects(List.of());
        assertTrue(store.list(LOCATION).isEmpty());
    }

    @Test
    void unsafeOrNonCanonicalKeys_areRefused() {
        for (String key : List.of(PREFIX + "../../secrets/x", PREFIX + "a//b", PREFIX + "a\\b", PREFIX + "./a")) {
            objects(List.of(object(key, 1)));
            assertThrows(IllegalArgumentException.class, () -> store.list(LOCATION), key);
        }
    }

    @Test
    void listingErrors_areIOExceptions() {
        objects(List.of(new Result<>(new ServerException("AccessDenied", 403, "trace"))));
        assertThrows(IOException.class, () -> store.list(LOCATION));
    }

    @Test
    void streamsAnObject() throws Exception {
        byte[] content = {1, 2, 3};
        when(minio.getObject(any(GetObjectArgs.class))).thenReturn(new GetObjectResponse(
                Headers.of(), "mlflow", "", PREFIX + "model/model.pkl", new ByteArrayInputStream(content)));

        try (InputStream in = store.open(LOCATION, new ArtifactFile("model/model.pkl", 3))) {
            assertArrayEquals(content, in.readAllBytes());
        }
        ArgumentCaptor<GetObjectArgs> args = ArgumentCaptor.forClass(GetObjectArgs.class);
        verify(minio).getObject(args.capture());
        assertEquals("mlflow", args.getValue().bucket());
        assertEquals(PREFIX + "model/model.pkl", args.getValue().object());
    }

    @Test
    void readErrors_areIOExceptions() throws Exception {
        when(minio.getObject(any(GetObjectArgs.class))).thenThrow(new InsufficientDataException("connection reset"));
        assertThrows(IOException.class, () -> store.open(LOCATION, new ArtifactFile("MLmodel", 1)));
    }

    /** Through ArtifactService: a location outside the allow-list never reaches MinIO. */
    @Test
    void locationsOutsideTheAllowList_areNeverListed() {
        ArtifactLocator locator = new ArtifactLocator() {
            @Override
            public boolean supports(org.etsi.osl.controllers.tmf915.model.AiModelSpecification s) {
                return true;
            }

            @Override
            public List<ArtifactLocation> locate(org.etsi.osl.controllers.tmf915.model.AiModelSpecification s) {
                return List.of(new ArtifactLocation(URI.create("s3://backups/db-dump"), "mlflow-model"));
            }
        };
        ArtifactService service = new ArtifactService(List.of(locator), List.of(store), List.of("s3://mlflow/"),
                1000, Duration.ofMinutes(1), Clock.systemUTC());

        assertThrows(ArtifactNotAvailableException.class,
                () -> service.resolve(ArtifactServiceTest.spec()));
        verify(minio, never()).listObjects(any(ListObjectsArgs.class));
    }

    @Test
    void configuration_createsTheClientAndStore_onlyWithAllThreeSettings() {
        ApplicationContextRunner runner = new ApplicationContextRunner().withUserConfiguration(S3Configuration.class);
        runner.run(ctx -> assertThat(ctx).doesNotHaveBean(MinioClient.class).doesNotHaveBean(S3ArtifactStore.class));
        runner.withPropertyValues("mlflow.s3.endpoint.url=http://minio:9000", "mlflow.s3.access.key=a")
                .run(ctx -> assertThat(ctx).doesNotHaveBean(MinioClient.class));
        // Environment-variable form (MLFLOW_S3_ENDPOINT_URL, …), as docker-compose.yml sets it.
        runner.withPropertyValues("mlflow.s3.endpoint.url=http://minio:9000", "mlflow.s3.access.key=a",
                        "mlflow.s3.secret.key=b")
                .run(ctx -> assertThat(ctx).hasSingleBean(MinioClient.class).hasSingleBean(S3ArtifactStore.class));
        // application.yml form.
        runner.withPropertyValues("mlflow.s3-endpoint-url=http://minio:9000", "mlflow.s3-access-key=a",
                        "mlflow.s3-secret-key=b")
                .run(ctx -> assertThat(ctx).hasSingleBean(S3ArtifactStore.class));
    }
}
