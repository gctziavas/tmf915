package org.etsi.osl.controllers.tmf915.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.etsi.osl.controllers.tmf915.artifacts.ArtifactNotAvailableException;
import org.etsi.osl.controllers.tmf915.artifacts.ArtifactService;
import org.etsi.osl.controllers.tmf915.artifacts.DownloadAttachments;
import org.etsi.osl.controllers.tmf915.artifacts.ModelArtifact;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationUpdate;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.AsyncWebRequest;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequestMapping("${openapi.aiManagement.base-path:/tmf-api/AiM/v4}")
public class AiModelSpecificationApiController implements AiModelSpecificationApi {

    /** TMF 915 extension: download a specification's model files. */
    public static final String PATH_DOWNLOAD_AI_MODEL_SPECIFICATION_ARTIFACTS = "/aiModelSpecification/{id}/artifacts";

    /**
     * Extra time the servlet container gives a download beyond {@code download-timeout}, so the
     * writer's own deadline ends it first, cleanly.
     */
    static final Duration ASYNC_TIMEOUT_GRACE = Duration.ofSeconds(30);

    /** Accepted {@code format} values: also keeps them safe in logs and file names. */
    private static final Pattern FORMAT = Pattern.compile("[A-Za-z0-9._-]{1,64}");

    private final AiModelSpecificationRepositoryService service;
    private final ArtifactService artifactService;

    public AiModelSpecificationApiController(AiModelSpecificationRepositoryService service,
                                             ArtifactService artifactService) {
        this.service = service;
        this.artifactService = artifactService;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<AiModelSpecification> createAiModelSpecification(AiModelSpecificationCreate aiModelSpecification) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createAiModelSpecification(aiModelSpecification));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Void> deleteAiModelSpecification(String id) {
        service.deleteAiModelSpecification(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<List<AiModelSpecification>> listAiModelSpecification(@Nullable String fields,
                                                                                @Nullable Integer offset,
                                                                                @Nullable Integer limit) {
        // ?fields=… is applied to the response by FieldsResponseBodyAdvice.
        return ResponseEntity.ok(service.findAllAiModelSpecifications(offset, limit));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<AiModelSpecification> patchAiModelSpecification(String id,
                                                                           AiModelSpecificationUpdate aiModelSpecification) {
        AiModelSpecification updated = service.updateAiModelSpecification(id, aiModelSpecification);
        return ResponseEntity.ok(updated);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<AiModelSpecification> retrieveAiModelSpecification(String id, @Nullable String fields) {
        AiModelSpecification found = service.findAiModelSpecificationById(id);
        if (found == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(found);
    }

    /**
     * Streams the model's files as a zip, read from storage while it is sent. Not part of the
     * TMF 915 specification: an extension advertised by the specification's
     * {@code attachment} of {@code @type} {@value DownloadAttachments#TYPE}.
     */
    @Operation(
        operationId = "downloadAiModelSpecificationArtifacts",
        summary = "Downloads the model files of an AiModelSpecification (TMF 915 extension)",
        description = "Streams the model's files as a zip. Without format: the specification's primary "
                + "format, its artifactFormat characteristic (e.g. mlflow-model: load with "
                + "mlflow.pyfunc.load_model on the unzipped directory). With format: another format the "
                + "specification offers, e.g. onnx. Not part of TMF 915. Each downloadable format is "
                + "advertised in an attachment of @type ModelArtifactAttachment, with its href and size.",
        tags = { "aiModelSpecification" },
        responses = {
            @ApiResponse(responseCode = "200", description = "The model files as a zip", content = {
                @Content(mediaType = "application/zip", schema = @Schema(type = "string", format = "binary"))
            }),
            @ApiResponse(responseCode = "400", description = "Malformed format parameter", content = {
                @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden: ROLE_USER required"),
            @ApiResponse(responseCode = "404", description = "Unknown id, the specification has no "
                    + "downloadable artifact (including downloadable=false), or not in that format", content = {
                @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            }),
            @ApiResponse(responseCode = "413", description = "Above aimodel.artifacts.max-download-bytes", content = {
                @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            }),
            @ApiResponse(responseCode = "503", description = "Artifact storage unavailable", content = {
                @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            })
        }
    )
    @GetMapping(PATH_DOWNLOAD_AI_MODEL_SPECIFICATION_ARTIFACTS)
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<StreamingResponseBody> downloadAiModelSpecificationArtifacts(
            @Parameter(name = "id", description = "Identifier of the AiModelSpecification", required = true)
            @PathVariable("id") String id,
            @Parameter(name = "format", description = "A format the specification offers (an attachmentType of "
                    + "its ModelArtifactAttachments), e.g. onnx. Default: its primary format.")
            @RequestParam(value = "format", required = false) String format,
            HttpServletRequest request) {
        if (format != null && !FORMAT.matcher(format).matches()) {
            throw new IllegalArgumentException("Invalid format: use letters, digits, '.', '_' or '-' (at most 64)");
        }
        AiModelSpecification specification = service.findAiModelSpecificationById(id);
        if (specification == null) {
            throw new ArtifactNotAvailableException("No AiModelSpecification with ID: " + id);
        }
        ModelArtifact artifact = artifactService.resolve(specification, format);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication == null ? "anonymous" : authentication.getName();
        limitRequestTime(request, artifactService.downloadTimeout().plus(ASYNC_TIMEOUT_GRACE));

        String fileName = DownloadAttachments.fileName(specification.getName(), specification.getVersion(), format);
        StreamingResponseBody body = out -> artifactService.writeZip(id, user, artifact, out);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(DownloadAttachments.MIME_TYPE))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(fileName, StandardCharsets.UTF_8).build().toString())
                .body(body);
    }

    /**
     * Sets this request's async timeout, so downloads get their own limit without changing
     * {@code spring.mvc.async.request-timeout}, which the MCP SSE endpoint also uses.
     */
    private static void limitRequestTime(HttpServletRequest request, Duration timeout) {
        AsyncWebRequest asyncRequest = WebAsyncUtils.getAsyncManager(request).getAsyncWebRequest();
        if (asyncRequest != null) {
            asyncRequest.setTimeout(timeout.toMillis());
        }
    }
}
