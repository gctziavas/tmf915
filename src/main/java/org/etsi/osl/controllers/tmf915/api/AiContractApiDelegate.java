package org.etsi.osl.controllers.tmf915.api;

import org.etsi.osl.controllers.tmf915.model.AiContract;
import org.etsi.osl.controllers.tmf915.model.AiContractCreate;
import org.etsi.osl.controllers.tmf915.model.AiContractUpdate;
import org.etsi.osl.controllers.tmf915.model.Error;
import org.springframework.lang.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.annotation.Generated;

/**
 * A delegate to be called by the {@link AiContractApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public interface AiContractApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /aiContract : Creates a AiContract
     * This operation creates a AiContract entity.
     *
     * @param aiContract The AiContract to be created (required)
     * @return Created (status code 201)
     *         or Bad Request (status code 400)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     *         or Method Not allowed (status code 405)
     *         or Conflict (status code 409)
     *         or Internal Server Error (status code 500)
     * @see AiContractApi#createAiContract
     */
    default ResponseEntity<AiContract> createAiContract(AiContractCreate aiContract) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"template\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"description\" : \"description\", \"id\" : \"id\", \"href\" : \"href\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"approvalDate\" : \"2000-01-23T04:56:07.000+00:00\", \"validFor\" : { \"startDateTime\" : \"1985-04-12T23:20:50.52Z\", \"endDateTime\" : \"1985-04-12T23:20:50.52Z\" }, \"@type\" : \"@type\", \"description\" : \"description\", \"rule\" : [ { \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"relatedParty\" : [ { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"version\" : \"version\", \"characteristic\" : [ { \"characteristicRelationship\" : [ { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"valueType\" : \"valueType\", \"name\" : \"name\", \"id\" : \"id\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"value\" : \"{}\" }, { \"characteristicRelationship\" : [ { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"valueType\" : \"valueType\", \"name\" : \"name\", \"id\" : \"id\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"value\" : \"{}\" } ], \"approved\" : true, \"@baseType\" : \"@baseType\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"state\" : \"state\", \"aiModel\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"@schemaLocation\" : \"https://openapi-generator.tech\", \"aiContractSpecification\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * DELETE /aiContract/{id} : Deletes a AiContract
     * This operation deletes a AiContract entity.
     *
     * @param id Identifier of the AiContract (required)
     * @return Deleted (status code 204)
     *         or Bad Request (status code 400)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     *         or Method Not allowed (status code 405)
     *         or Conflict (status code 409)
     *         or Internal Server Error (status code 500)
     * @see AiContractApi#deleteAiContract
     */
    default ResponseEntity<Void> deleteAiContract(String id) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * GET /aiContract : List or find AiContract objects
     * This operation list or find AiContract entities
     *
     * @param fields Comma-separated properties to be provided in response (optional)
     * @param offset Requested index for start of resources to be provided in response (optional)
     * @param limit Requested number of resources to be provided in response (optional)
     * @return Success (status code 200)
     *         or Bad Request (status code 400)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     *         or Method Not allowed (status code 405)
     *         or Conflict (status code 409)
     *         or Internal Server Error (status code 500)
     * @see AiContractApi#listAiContract
     */
    default ResponseEntity<List<AiContract>> listAiContract(String fields,
        Integer offset,
        Integer limit) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "[ { \"template\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"description\" : \"description\", \"id\" : \"id\", \"href\" : \"href\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"approvalDate\" : \"2000-01-23T04:56:07.000+00:00\", \"validFor\" : { \"startDateTime\" : \"1985-04-12T23:20:50.52Z\", \"endDateTime\" : \"1985-04-12T23:20:50.52Z\" }, \"@type\" : \"@type\", \"description\" : \"description\", \"rule\" : [ { \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"relatedParty\" : [ { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"version\" : \"version\", \"characteristic\" : [ { \"characteristicRelationship\" : [ { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"valueType\" : \"valueType\", \"name\" : \"name\", \"id\" : \"id\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"value\" : \"{}\" }, { \"characteristicRelationship\" : [ { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"valueType\" : \"valueType\", \"name\" : \"name\", \"id\" : \"id\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"value\" : \"{}\" } ], \"approved\" : true, \"@baseType\" : \"@baseType\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"state\" : \"state\", \"aiModel\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"@schemaLocation\" : \"https://openapi-generator.tech\", \"aiContractSpecification\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } }, { \"template\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"description\" : \"description\", \"id\" : \"id\", \"href\" : \"href\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"approvalDate\" : \"2000-01-23T04:56:07.000+00:00\", \"validFor\" : { \"startDateTime\" : \"1985-04-12T23:20:50.52Z\", \"endDateTime\" : \"1985-04-12T23:20:50.52Z\" }, \"@type\" : \"@type\", \"description\" : \"description\", \"rule\" : [ { \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"relatedParty\" : [ { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"version\" : \"version\", \"characteristic\" : [ { \"characteristicRelationship\" : [ { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"valueType\" : \"valueType\", \"name\" : \"name\", \"id\" : \"id\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"value\" : \"{}\" }, { \"characteristicRelationship\" : [ { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"valueType\" : \"valueType\", \"name\" : \"name\", \"id\" : \"id\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"value\" : \"{}\" } ], \"approved\" : true, \"@baseType\" : \"@baseType\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"state\" : \"state\", \"aiModel\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"@schemaLocation\" : \"https://openapi-generator.tech\", \"aiContractSpecification\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } } ]";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * PATCH /aiContract/{id} : Updates partially a AiContract
     * This operation updates partially a AiContract entity.
     *
     * @param id Identifier of the AiContract (required)
     * @param aiContract The AiContract to be updated (required)
     * @return Updated (status code 200)
     *         or Bad Request (status code 400)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     *         or Method Not allowed (status code 405)
     *         or Conflict (status code 409)
     *         or Internal Server Error (status code 500)
     * @see AiContractApi#patchAiContract
     */
    default ResponseEntity<AiContract> patchAiContract(String id,
        AiContractUpdate aiContract) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"template\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"description\" : \"description\", \"id\" : \"id\", \"href\" : \"href\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"approvalDate\" : \"2000-01-23T04:56:07.000+00:00\", \"validFor\" : { \"startDateTime\" : \"1985-04-12T23:20:50.52Z\", \"endDateTime\" : \"1985-04-12T23:20:50.52Z\" }, \"@type\" : \"@type\", \"description\" : \"description\", \"rule\" : [ { \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"relatedParty\" : [ { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"version\" : \"version\", \"characteristic\" : [ { \"characteristicRelationship\" : [ { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"valueType\" : \"valueType\", \"name\" : \"name\", \"id\" : \"id\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"value\" : \"{}\" }, { \"characteristicRelationship\" : [ { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"valueType\" : \"valueType\", \"name\" : \"name\", \"id\" : \"id\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"value\" : \"{}\" } ], \"approved\" : true, \"@baseType\" : \"@baseType\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"state\" : \"state\", \"aiModel\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"@schemaLocation\" : \"https://openapi-generator.tech\", \"aiContractSpecification\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * GET /aiContract/{id} : Retrieves a AiContract by ID
     * This operation retrieves a AiContract entity. Attribute selection is enabled for all first level attributes.
     *
     * @param id Identifier of the AiContract (required)
     * @param fields Comma-separated properties to provide in response (optional)
     * @return Success (status code 200)
     *         or Bad Request (status code 400)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     *         or Method Not allowed (status code 405)
     *         or Conflict (status code 409)
     *         or Internal Server Error (status code 500)
     * @see AiContractApi#retrieveAiContract
     */
    default ResponseEntity<AiContract> retrieveAiContract(String id,
        String fields) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"template\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"description\" : \"description\", \"id\" : \"id\", \"href\" : \"href\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"approvalDate\" : \"2000-01-23T04:56:07.000+00:00\", \"validFor\" : { \"startDateTime\" : \"1985-04-12T23:20:50.52Z\", \"endDateTime\" : \"1985-04-12T23:20:50.52Z\" }, \"@type\" : \"@type\", \"description\" : \"description\", \"rule\" : [ { \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"relatedParty\" : [ { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"version\" : \"version\", \"characteristic\" : [ { \"characteristicRelationship\" : [ { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"valueType\" : \"valueType\", \"name\" : \"name\", \"id\" : \"id\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"value\" : \"{}\" }, { \"characteristicRelationship\" : [ { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"valueType\" : \"valueType\", \"name\" : \"name\", \"id\" : \"id\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"value\" : \"{}\" } ], \"approved\" : true, \"@baseType\" : \"@baseType\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"state\" : \"state\", \"aiModel\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"@schemaLocation\" : \"https://openapi-generator.tech\", \"aiContractSpecification\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reason\" : \"reason\", \"code\" : \"code\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"message\" : \"message\", \"referenceError\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"status\" : \"status\" }";
                    ApiUtil.setExampleResponse(request, "application/json;charset=utf-8", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
