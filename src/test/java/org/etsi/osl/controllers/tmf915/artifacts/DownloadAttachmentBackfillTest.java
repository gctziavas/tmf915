package org.etsi.osl.controllers.tmf915.artifacts;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DownloadAttachmentBackfillTest {

    private static final ModelArtifact ARTIFACT = new ModelArtifact(URI.create("s3://mlflow/x"), "mlflow-model",
            List.of(new ArtifactFile("MLmodel", 10)), 10);

    private final AiModelSpecificationRepositoryService specifications = mock(AiModelSpecificationRepositoryService.class);
    private final ArtifactService artifacts = mock(ArtifactService.class);
    private final DownloadAttachmentBackfill backfill = new DownloadAttachmentBackfill(specifications, artifacts);

    private static AiModelSpecification spec(String id) {
        AiModelSpecification spec = new AiModelSpecification();
        spec.setId(id);
        return spec;
    }

    @Test
    void addsAttachmentsToDownloadableSpecificationsWithout_acrossPages() {
        when(artifacts.hasStores()).thenReturn(true);
        List<AiModelSpecification> first = new ArrayList<>(IntStream.range(0, DownloadAttachmentBackfill.PAGE_SIZE)
                .mapToObj(i -> spec("s" + i)).toList());
        AiModelSpecification already = spec("s0");
        already.setAttachment(new ArrayList<>(List.of(DownloadAttachments.create("n", "v", ARTIFACT))));
        first.set(0, already);
        when(specifications.findAllAiModelSpecifications(0, DownloadAttachmentBackfill.PAGE_SIZE)).thenReturn(first);
        when(specifications.findAllAiModelSpecifications(DownloadAttachmentBackfill.PAGE_SIZE, DownloadAttachmentBackfill.PAGE_SIZE))
                .thenReturn(List.of(spec("last")));
        // Only s1 and "last" are downloadable.
        when(artifacts.describe(any())).thenReturn(Optional.empty());
        when(artifacts.describe(first.get(1))).thenReturn(Optional.of(ARTIFACT));
        when(artifacts.describe(org.mockito.ArgumentMatchers.argThat(s -> s != null && "last".equals(s.getId()))))
                .thenReturn(Optional.of(ARTIFACT));
        when(specifications.addDownloadAttachment(any(), any())).thenReturn(true);

        assertEquals(2, backfill.run());

        verify(artifacts, never()).describe(already);
        verify(specifications).addDownloadAttachment("s1", ARTIFACT);
        verify(specifications).addDownloadAttachment("last", ARTIFACT);
    }

    @Test
    void withoutStorage_doesNothing() {
        when(artifacts.hasStores()).thenReturn(false);
        assertEquals(0, backfill.run());
        verify(specifications, never()).findAllAiModelSpecifications(any(), anyInt());
        verify(specifications, never()).addDownloadAttachment(eq("x"), any());
    }
}
