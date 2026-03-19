package org.etsi.osl.controllers.tmf915.api;

import jakarta.annotation.Generated;
import org.etsi.osl.controllers.tmf915.model.Monitor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;

/**
 * A delegate to be called by the {@link MonitorApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public interface MonitorApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /monitor : List or find Monitor objects
     * This operation list or find Monitor entities
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
     * @see MonitorApi#listMonitor
     */
    default ResponseEntity<List<Monitor>> listMonitor(String fields,
        Integer offset,
        Integer limit) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "[ { \"request\" : { \"method\" : \"method\", \"header\" : [ { \"name\" : \"name\", \"value\" : \"value\" }, { \"name\" : \"name\", \"value\" : \"value\" } ], \"to\" : \"to\", \"body\" : \"body\" }, \"@baseType\" : \"@baseType\", \"response\" : { \"header\" : [ { \"name\" : \"name\", \"value\" : \"value\" }, { \"name\" : \"name\", \"value\" : \"value\" } ], \"body\" : \"body\", \"statusCode\" : \"statusCode\" }, \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"href\", \"state\" : \"state\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"sourceHref\" : \"sourceHref\" }, { \"request\" : { \"method\" : \"method\", \"header\" : [ { \"name\" : \"name\", \"value\" : \"value\" }, { \"name\" : \"name\", \"value\" : \"value\" } ], \"to\" : \"to\", \"body\" : \"body\" }, \"@baseType\" : \"@baseType\", \"response\" : { \"header\" : [ { \"name\" : \"name\", \"value\" : \"value\" }, { \"name\" : \"name\", \"value\" : \"value\" } ], \"body\" : \"body\", \"statusCode\" : \"statusCode\" }, \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"href\", \"state\" : \"state\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"sourceHref\" : \"sourceHref\" } ]";
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
     * GET /monitor/{id} : Retrieves a Monitor by ID
     * This operation retrieves a Monitor entity. Attribute selection is enabled for all first level attributes.
     *
     * @param id Identifier of the Monitor (required)
     * @param fields Comma-separated properties to provide in response (optional)
     * @return Success (status code 200)
     *         or Bad Request (status code 400)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     *         or Method Not allowed (status code 405)
     *         or Conflict (status code 409)
     *         or Internal Server Error (status code 500)
     * @see MonitorApi#retrieveMonitor
     */
    default ResponseEntity<Monitor> retrieveMonitor(String id,
        String fields) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"request\" : { \"method\" : \"method\", \"header\" : [ { \"name\" : \"name\", \"value\" : \"value\" }, { \"name\" : \"name\", \"value\" : \"value\" } ], \"to\" : \"to\", \"body\" : \"body\" }, \"@baseType\" : \"@baseType\", \"response\" : { \"header\" : [ { \"name\" : \"name\", \"value\" : \"value\" }, { \"name\" : \"name\", \"value\" : \"value\" } ], \"body\" : \"body\", \"statusCode\" : \"statusCode\" }, \"@type\" : \"@type\", \"id\" : \"id\", \"href\" : \"href\", \"state\" : \"state\", \"@schemaLocation\" : \"https://openapi-generator.tech\", \"sourceHref\" : \"sourceHref\" }";
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
