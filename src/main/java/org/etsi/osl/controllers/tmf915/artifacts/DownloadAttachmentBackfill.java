package org.etsi.osl.controllers.tmf915.artifacts;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Once after startup, in the background: adds the download attachment to downloadable
 * specifications that have none, i.e. those stored before downloads existed, or whose
 * {@code downloadable=false} was later removed. New specifications get theirs when they are
 * synced. Downloading does not depend on it; it only makes the download visible in the
 * specification. Off with {@code aimodel.artifacts.backfill-on-startup=false}.
 */
@Component
@ConditionalOnProperty(name = "aimodel.artifacts.backfill-on-startup", havingValue = "true", matchIfMissing = true)
public class DownloadAttachmentBackfill {

    private static final Logger log = LoggerFactory.getLogger(DownloadAttachmentBackfill.class);
    static final int PAGE_SIZE = 100;

    private final AiModelSpecificationRepositoryService specifications;
    private final ArtifactService artifactService;

    public DownloadAttachmentBackfill(AiModelSpecificationRepositoryService specifications,
                                      ArtifactService artifactService) {
        this.specifications = specifications;
        this.artifactService = artifactService;
    }

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        try {
            run();
        } catch (RuntimeException e) {
            log.warn("Download attachment backfill stopped: {}", e.toString());
        }
    }

    /** @return number of specifications that got an attachment */
    public int run() {
        if (!artifactService.hasStores()) {
            log.debug("Download attachment backfill skipped: no artifact storage configured");
            return 0;
        }
        int added = 0;
        int offset = 0;
        List<AiModelSpecification> page;
        do {
            page = specifications.findAllAiModelSpecifications(offset, PAGE_SIZE);
            for (AiModelSpecification spec : page) {
                if (DownloadAttachments.find(spec.getAttachment()).isPresent()) {
                    continue;
                }
                Optional<ModelArtifact> artifact = artifactService.describe(spec);
                if (artifact.isPresent() && specifications.addDownloadAttachment(spec.getId(), artifact.get())) {
                    added++;
                }
            }
            offset += page.size();
        } while (page.size() == PAGE_SIZE);
        if (added > 0) {
            log.info("Download attachment backfill: added to {} specification(s)", added);
        }
        return added;
    }
}
