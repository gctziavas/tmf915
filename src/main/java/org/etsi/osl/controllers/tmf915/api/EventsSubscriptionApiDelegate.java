package org.etsi.osl.controllers.tmf915.api;

import jakarta.annotation.Generated;
import org.etsi.osl.controllers.tmf915.model.EventSubscription;
import org.etsi.osl.controllers.tmf915.model.EventSubscriptionInput;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

/**
 * A delegate to be called by the {@link EventsSubscriptionApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public interface EventsSubscriptionApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /hub : Register a listener
     * Sets the communication endpoint address the service instance must use to deliver information about its health state, execution state, failures and metrics.
     *
     * @param data Data containing the callback endpoint to deliver the information (required)
     * @return Subscribed (status code 201)
     *         or Bad Request (status code 400)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     *         or Method Not allowed (status code 405)
     *         or Conflict (status code 409)
     *         or Internal Server Error (status code 500)
     * @see EventsSubscriptionApi#registerListener
     */
    default ResponseEntity<EventSubscription> registerListener(EventSubscriptionInput data) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"query\" : \"query\", \"callback\" : \"callback\", \"id\" : \"id\" }";
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
     * DELETE /hub/{id} : Unregister a listener
     * Resets the communication endpoint address the service instance must use to deliver information about its health state, execution state, failures and metrics.
     *
     * @param id The id of the registered listener (required)
     * @return Deleted (status code 204)
     *         or Bad request (status code 400)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     *         or Method not allowed (status code 405)
     *         or Internal Server Error (status code 500)
     * @see EventsSubscriptionApi#unregisterListener
     */
    default ResponseEntity<Void> unregisterListener(String id) {
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
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
