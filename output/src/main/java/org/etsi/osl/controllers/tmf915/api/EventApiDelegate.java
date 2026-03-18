package org.etsi.osl.controllers.tmf915.api;

import org.etsi.osl.controllers.tmf915.model.Error;
import org.etsi.osl.controllers.tmf915.model.Event;
import org.etsi.osl.controllers.tmf915.model.EventCreate;
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
 * A delegate to be called by the {@link EventApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public interface EventApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /topic/{topicId}/event : Creates a Event
     * This operation creates a Event entity.
     *
     * @param topicId Identifier of the parent Topic entity (required)
     * @param event The Event to be created (required)
     * @return Created (status code 201)
     *         or Bad Request (status code 400)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     *         or Method Not allowed (status code 405)
     *         or Conflict (status code 409)
     *         or Internal Server Error (status code 500)
     * @see EventApi#createEvent
     */
    default ResponseEntity<Event> createEvent(String topicId,
        EventCreate event) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"eventId\" : \"eventId\", \"@type\" : \"@type\", \"analyticCharacteristic\" : [ { \"characteristicRelationship\" : [ { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"valueType\" : \"valueType\", \"name\" : \"name\", \"id\" : \"id\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"value\" : \"{}\" }, { \"characteristicRelationship\" : [ { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"valueType\" : \"valueType\", \"name\" : \"name\", \"id\" : \"id\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"value\" : \"{}\" } ], \"description\" : \"description\", \"eventType\" : \"eventType\", \"source\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"priority\" : \"priority\", \"title\" : \"title\", \"relatedParty\" : [ { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"domain\" : \"domain\", \"eventTime\" : \"2000-01-23T04:56:07.000+00:00\", \"reportingSystem\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"correlationId\" : \"correlationId\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"event\" : \"{}\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"timeOccurred\" : \"2000-01-23T04:56:07.000+00:00\" }";
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
     * GET /topic/{topicId}/event : List or find Event objects
     * This operation list or find Event entities
     *
     * @param topicId Identifier of the parent Topic entity (required)
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
     * @see EventApi#listEvent
     */
    default ResponseEntity<List<Event>> listEvent(String topicId,
        String fields,
        Integer offset,
        Integer limit) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "[ { \"eventId\" : \"eventId\", \"@type\" : \"@type\", \"analyticCharacteristic\" : [ { \"characteristicRelationship\" : [ { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"valueType\" : \"valueType\", \"name\" : \"name\", \"id\" : \"id\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"value\" : \"{}\" }, { \"characteristicRelationship\" : [ { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"valueType\" : \"valueType\", \"name\" : \"name\", \"id\" : \"id\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"value\" : \"{}\" } ], \"description\" : \"description\", \"eventType\" : \"eventType\", \"source\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"priority\" : \"priority\", \"title\" : \"title\", \"relatedParty\" : [ { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"domain\" : \"domain\", \"eventTime\" : \"2000-01-23T04:56:07.000+00:00\", \"reportingSystem\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"correlationId\" : \"correlationId\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"event\" : \"{}\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"timeOccurred\" : \"2000-01-23T04:56:07.000+00:00\" }, { \"eventId\" : \"eventId\", \"@type\" : \"@type\", \"analyticCharacteristic\" : [ { \"characteristicRelationship\" : [ { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"valueType\" : \"valueType\", \"name\" : \"name\", \"id\" : \"id\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"value\" : \"{}\" }, { \"characteristicRelationship\" : [ { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"valueType\" : \"valueType\", \"name\" : \"name\", \"id\" : \"id\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"value\" : \"{}\" } ], \"description\" : \"description\", \"eventType\" : \"eventType\", \"source\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"priority\" : \"priority\", \"title\" : \"title\", \"relatedParty\" : [ { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"domain\" : \"domain\", \"eventTime\" : \"2000-01-23T04:56:07.000+00:00\", \"reportingSystem\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"correlationId\" : \"correlationId\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"event\" : \"{}\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"timeOccurred\" : \"2000-01-23T04:56:07.000+00:00\" } ]";
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
     * GET /topic/{topicId}/event/{id} : Retrieves a Event by ID
     * This operation retrieves a Event entity. Attribute selection is enabled for all first level attributes.
     *
     * @param topicId Identifier of the parent Topic entity (required)
     * @param id Identifier of the Event (required)
     * @param fields Comma-separated properties to provide in response (optional)
     * @return Success (status code 200)
     *         or Bad Request (status code 400)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     *         or Method Not allowed (status code 405)
     *         or Conflict (status code 409)
     *         or Internal Server Error (status code 500)
     * @see EventApi#retrieveEvent
     */
    default ResponseEntity<Event> retrieveEvent(String topicId,
        String id,
        String fields) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"eventId\" : \"eventId\", \"@type\" : \"@type\", \"analyticCharacteristic\" : [ { \"characteristicRelationship\" : [ { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"valueType\" : \"valueType\", \"name\" : \"name\", \"id\" : \"id\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"value\" : \"{}\" }, { \"characteristicRelationship\" : [ { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"relationshipType\" : \"relationshipType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"valueType\" : \"valueType\", \"name\" : \"name\", \"id\" : \"id\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"value\" : \"{}\" } ], \"description\" : \"description\", \"eventType\" : \"eventType\", \"source\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"priority\" : \"priority\", \"title\" : \"title\", \"relatedParty\" : [ { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"@baseType\" : \"@baseType\", \"domain\" : \"domain\", \"eventTime\" : \"2000-01-23T04:56:07.000+00:00\", \"reportingSystem\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"correlationId\" : \"correlationId\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"event\" : \"{}\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"timeOccurred\" : \"2000-01-23T04:56:07.000+00:00\" }";
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
