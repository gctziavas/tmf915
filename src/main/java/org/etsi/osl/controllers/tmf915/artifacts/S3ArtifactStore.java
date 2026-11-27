package org.etsi.osl.controllers.tmf915.artifacts;

import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads {@code s3://bucket/prefix} locations with the MinIO client. The location's authority is
 * the bucket and its path the key prefix; files are the objects under the prefix.
 *
 * <p>Object keys that are not already in canonical form (for example containing {@code //},
 * {@code ./} or {@code ..} segments, or a backslash) are refused with
 * {@link IllegalArgumentException}: the entry name in the zip must be the key itself.
 */
public class S3ArtifactStore implements ArtifactStore {

    private final MinioClient client;

    public S3ArtifactStore(MinioClient client) {
        this.client = client;
    }

    @Override
    public boolean supports(URI location) {
        return "s3".equalsIgnoreCase(location.getScheme());
    }

    @Override
    public List<ArtifactFile> list(URI location) throws IOException {
        String bucket = bucket(location);
        String prefix = prefix(location);
        Iterable<Result<Item>> results = client.listObjects(ListObjectsArgs.builder()
                .bucket(bucket)
                .prefix(prefix)
                .recursive(true)
                .build());
        List<ArtifactFile> files = new ArrayList<>();
        for (Result<Item> result : results) {
            Item item;
            try {
                item = result.get();
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                throw new IOException("Cannot list " + location + ": " + e.getMessage(), e);
            }
            String key = item.objectName();
            if (item.isDir() || key.endsWith("/") || !key.startsWith(prefix)) {
                continue;
            }
            String relative = key.substring(prefix.length());
            String path = ArtifactPaths.normalize(relative);
            if (!path.equals(relative)) {
                throw new IllegalArgumentException("non-canonical object key under " + location + ": " + key);
            }
            files.add(new ArtifactFile(path, item.size()));
        }
        return files;
    }

    @Override
    public InputStream open(URI location, ArtifactFile file) throws IOException {
        String key = prefix(location) + ArtifactPaths.normalize(file.path());
        try {
            return client.getObject(GetObjectArgs.builder().bucket(bucket(location)).object(key).build());
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException("Cannot read " + location + "/" + file.path() + ": " + e.getMessage(), e);
        }
    }

    private static String bucket(URI location) {
        String bucket = location.getRawAuthority();
        if (bucket == null || bucket.isEmpty()) {
            throw new IllegalArgumentException("S3 location without bucket: " + location);
        }
        return bucket;
    }

    /** The key prefix: the path without its leading slash, ending with a slash (or empty). */
    private static String prefix(URI location) {
        String path = location.getPath() == null ? "" : location.getPath();
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path.isEmpty() || path.endsWith("/") ? path : path + "/";
    }
}
