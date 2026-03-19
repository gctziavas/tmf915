package org.etsi.osl.controllers.tmf915.api;

import jakarta.annotation.Generated;
import org.etsi.osl.controllers.tmf915.model.Alarm;
import org.etsi.osl.controllers.tmf915.model.AlarmCreate;
import org.etsi.osl.controllers.tmf915.model.AlarmUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;

/**
 * A delegate to be called by the {@link AlarmApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public interface AlarmApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /alarm : Creates a Alarm
     * This operation creates a Alarm entity.
     *
     * @param alarm The Alarm to be created (required)
     * @return Created (status code 201)
     *         or Bad Request (status code 400)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     *         or Method Not allowed (status code 405)
     *         or Conflict (status code 409)
     *         or Internal Server Error (status code 500)
     * @see AlarmApi#createAlarm
     */
    default ResponseEntity<Alarm> createAlarm(AlarmCreate alarm) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reportingSystemId\" : \"reportingSystemId\", \"alarmRaisedTime\" : \"2000-01-23T04:56:07.000+00:00\", \"alarmClearedTime\" : \"2000-01-23T04:56:07.000+00:00\", \"alarmedObjectType\" : \"alarmedObjectType\", \"@type\" : \"@type\", \"proposedRepairedActions\" : \"proposedRepairedActions\", \"correlatedAlarm\" : [ { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"alarmReportingTime\" : \"2000-01-23T04:56:07.000+00:00\", \"alarmedObject\" : { \"id\" : \"id\", \"href\" : \"href\" }, \"alarmEscalation\" : true, \"alarmChangedTime\" : \"2000-01-23T04:56:07.000+00:00\", \"@baseType\" : \"@baseType\", \"probableCause\" : \"probableCause\", \"perceivedSeverity\" : \"CRITICAL\", \"affectedService\" : [ { \"id\" : \"id\", \"href\" : \"href\" }, { \"id\" : \"id\", \"href\" : \"href\" } ], \"ackSystemId\" : \"ackSystemId\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"state\" : \"state\", \"place\" : [ { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"href\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"href\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"crossedThresholdInformation\" : { \"indicatorName\" : \"indicatorName\", \"thresholdCrossingDescription\" : \"thresholdCrossingDescription\", \"granularity\" : \"granularity\", \"observedValue\" : \"observedValue\", \"threshold\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"direction\" : \"direction\", \"indicatorUnit\" : \"indicatorUnit\" }, \"@schemaLocation\" : \"https://openapi-generator.tech\", \"externalAlarmId\" : \"externalAlarmId\", \"clearSystemId\" : \"clearSystemId\", \"parentAlarm\" : [ { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"isRootCause\" : true, \"ackUserId\" : \"ackUserId\", \"clearUserId\" : \"clearUserId\", \"serviceAffecting\" : true, \"sourceSystemId\" : \"sourceSystemId\", \"ackState\" : \"ackState\", \"alarmType\" : \"communicationsAlarm\", \"specificProblem\" : \"specificProblem\", \"plannedOutageIndicator\" : \"plannedOutageIndicator\", \"comment\" : [ { \"systemId\" : \"systemId\", \"comment\" : \"comment\", \"time\" : \"2000-01-23T04:56:07.000+00:00\", \"userId\" : \"userId\" }, { \"systemId\" : \"systemId\", \"comment\" : \"comment\", \"time\" : \"2000-01-23T04:56:07.000+00:00\", \"userId\" : \"userId\" } ], \"alarmDetails\" : \"alarmDetails\" }";
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
     * DELETE /alarm/{id} : Deletes a Alarm
     * This operation deletes a Alarm entity.
     *
     * @param id Identifier of the Alarm (required)
     * @return Deleted (status code 204)
     *         or Bad Request (status code 400)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     *         or Method Not allowed (status code 405)
     *         or Conflict (status code 409)
     *         or Internal Server Error (status code 500)
     * @see AlarmApi#deleteAlarm
     */
    default ResponseEntity<Void> deleteAlarm(String id) {
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
     * GET /alarm : List or find Alarm objects
     * This operation list or find Alarm entities
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
     * @see AlarmApi#listAlarm
     */
    default ResponseEntity<List<Alarm>> listAlarm(String fields,
        Integer offset,
        Integer limit) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "[ { \"reportingSystemId\" : \"reportingSystemId\", \"alarmRaisedTime\" : \"2000-01-23T04:56:07.000+00:00\", \"alarmClearedTime\" : \"2000-01-23T04:56:07.000+00:00\", \"alarmedObjectType\" : \"alarmedObjectType\", \"@type\" : \"@type\", \"proposedRepairedActions\" : \"proposedRepairedActions\", \"correlatedAlarm\" : [ { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"alarmReportingTime\" : \"2000-01-23T04:56:07.000+00:00\", \"alarmedObject\" : { \"id\" : \"id\", \"href\" : \"href\" }, \"alarmEscalation\" : true, \"alarmChangedTime\" : \"2000-01-23T04:56:07.000+00:00\", \"@baseType\" : \"@baseType\", \"probableCause\" : \"probableCause\", \"perceivedSeverity\" : \"CRITICAL\", \"affectedService\" : [ { \"id\" : \"id\", \"href\" : \"href\" }, { \"id\" : \"id\", \"href\" : \"href\" } ], \"ackSystemId\" : \"ackSystemId\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"state\" : \"state\", \"place\" : [ { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"href\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"href\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"crossedThresholdInformation\" : { \"indicatorName\" : \"indicatorName\", \"thresholdCrossingDescription\" : \"thresholdCrossingDescription\", \"granularity\" : \"granularity\", \"observedValue\" : \"observedValue\", \"threshold\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"direction\" : \"direction\", \"indicatorUnit\" : \"indicatorUnit\" }, \"@schemaLocation\" : \"https://openapi-generator.tech\", \"externalAlarmId\" : \"externalAlarmId\", \"clearSystemId\" : \"clearSystemId\", \"parentAlarm\" : [ { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"isRootCause\" : true, \"ackUserId\" : \"ackUserId\", \"clearUserId\" : \"clearUserId\", \"serviceAffecting\" : true, \"sourceSystemId\" : \"sourceSystemId\", \"ackState\" : \"ackState\", \"alarmType\" : \"communicationsAlarm\", \"specificProblem\" : \"specificProblem\", \"plannedOutageIndicator\" : \"plannedOutageIndicator\", \"comment\" : [ { \"systemId\" : \"systemId\", \"comment\" : \"comment\", \"time\" : \"2000-01-23T04:56:07.000+00:00\", \"userId\" : \"userId\" }, { \"systemId\" : \"systemId\", \"comment\" : \"comment\", \"time\" : \"2000-01-23T04:56:07.000+00:00\", \"userId\" : \"userId\" } ], \"alarmDetails\" : \"alarmDetails\" }, { \"reportingSystemId\" : \"reportingSystemId\", \"alarmRaisedTime\" : \"2000-01-23T04:56:07.000+00:00\", \"alarmClearedTime\" : \"2000-01-23T04:56:07.000+00:00\", \"alarmedObjectType\" : \"alarmedObjectType\", \"@type\" : \"@type\", \"proposedRepairedActions\" : \"proposedRepairedActions\", \"correlatedAlarm\" : [ { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"alarmReportingTime\" : \"2000-01-23T04:56:07.000+00:00\", \"alarmedObject\" : { \"id\" : \"id\", \"href\" : \"href\" }, \"alarmEscalation\" : true, \"alarmChangedTime\" : \"2000-01-23T04:56:07.000+00:00\", \"@baseType\" : \"@baseType\", \"probableCause\" : \"probableCause\", \"perceivedSeverity\" : \"CRITICAL\", \"affectedService\" : [ { \"id\" : \"id\", \"href\" : \"href\" }, { \"id\" : \"id\", \"href\" : \"href\" } ], \"ackSystemId\" : \"ackSystemId\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"state\" : \"state\", \"place\" : [ { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"href\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"href\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"crossedThresholdInformation\" : { \"indicatorName\" : \"indicatorName\", \"thresholdCrossingDescription\" : \"thresholdCrossingDescription\", \"granularity\" : \"granularity\", \"observedValue\" : \"observedValue\", \"threshold\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"direction\" : \"direction\", \"indicatorUnit\" : \"indicatorUnit\" }, \"@schemaLocation\" : \"https://openapi-generator.tech\", \"externalAlarmId\" : \"externalAlarmId\", \"clearSystemId\" : \"clearSystemId\", \"parentAlarm\" : [ { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"isRootCause\" : true, \"ackUserId\" : \"ackUserId\", \"clearUserId\" : \"clearUserId\", \"serviceAffecting\" : true, \"sourceSystemId\" : \"sourceSystemId\", \"ackState\" : \"ackState\", \"alarmType\" : \"communicationsAlarm\", \"specificProblem\" : \"specificProblem\", \"plannedOutageIndicator\" : \"plannedOutageIndicator\", \"comment\" : [ { \"systemId\" : \"systemId\", \"comment\" : \"comment\", \"time\" : \"2000-01-23T04:56:07.000+00:00\", \"userId\" : \"userId\" }, { \"systemId\" : \"systemId\", \"comment\" : \"comment\", \"time\" : \"2000-01-23T04:56:07.000+00:00\", \"userId\" : \"userId\" } ], \"alarmDetails\" : \"alarmDetails\" } ]";
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
     * PATCH /alarm/{id} : Updates partially a Alarm
     * This operation updates partially a Alarm entity.
     *
     * @param id Identifier of the Alarm (required)
     * @param alarm The Alarm to be updated (required)
     * @return Updated (status code 200)
     *         or Bad Request (status code 400)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     *         or Method Not allowed (status code 405)
     *         or Conflict (status code 409)
     *         or Internal Server Error (status code 500)
     * @see AlarmApi#patchAlarm
     */
    default ResponseEntity<Alarm> patchAlarm(String id,
        AlarmUpdate alarm) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reportingSystemId\" : \"reportingSystemId\", \"alarmRaisedTime\" : \"2000-01-23T04:56:07.000+00:00\", \"alarmClearedTime\" : \"2000-01-23T04:56:07.000+00:00\", \"alarmedObjectType\" : \"alarmedObjectType\", \"@type\" : \"@type\", \"proposedRepairedActions\" : \"proposedRepairedActions\", \"correlatedAlarm\" : [ { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"alarmReportingTime\" : \"2000-01-23T04:56:07.000+00:00\", \"alarmedObject\" : { \"id\" : \"id\", \"href\" : \"href\" }, \"alarmEscalation\" : true, \"alarmChangedTime\" : \"2000-01-23T04:56:07.000+00:00\", \"@baseType\" : \"@baseType\", \"probableCause\" : \"probableCause\", \"perceivedSeverity\" : \"CRITICAL\", \"affectedService\" : [ { \"id\" : \"id\", \"href\" : \"href\" }, { \"id\" : \"id\", \"href\" : \"href\" } ], \"ackSystemId\" : \"ackSystemId\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"state\" : \"state\", \"place\" : [ { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"href\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"href\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"crossedThresholdInformation\" : { \"indicatorName\" : \"indicatorName\", \"thresholdCrossingDescription\" : \"thresholdCrossingDescription\", \"granularity\" : \"granularity\", \"observedValue\" : \"observedValue\", \"threshold\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"direction\" : \"direction\", \"indicatorUnit\" : \"indicatorUnit\" }, \"@schemaLocation\" : \"https://openapi-generator.tech\", \"externalAlarmId\" : \"externalAlarmId\", \"clearSystemId\" : \"clearSystemId\", \"parentAlarm\" : [ { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"isRootCause\" : true, \"ackUserId\" : \"ackUserId\", \"clearUserId\" : \"clearUserId\", \"serviceAffecting\" : true, \"sourceSystemId\" : \"sourceSystemId\", \"ackState\" : \"ackState\", \"alarmType\" : \"communicationsAlarm\", \"specificProblem\" : \"specificProblem\", \"plannedOutageIndicator\" : \"plannedOutageIndicator\", \"comment\" : [ { \"systemId\" : \"systemId\", \"comment\" : \"comment\", \"time\" : \"2000-01-23T04:56:07.000+00:00\", \"userId\" : \"userId\" }, { \"systemId\" : \"systemId\", \"comment\" : \"comment\", \"time\" : \"2000-01-23T04:56:07.000+00:00\", \"userId\" : \"userId\" } ], \"alarmDetails\" : \"alarmDetails\" }";
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
     * GET /alarm/{id} : Retrieves a Alarm by ID
     * This operation retrieves a Alarm entity. Attribute selection is enabled for all first level attributes.
     *
     * @param id Identifier of the Alarm (required)
     * @param fields Comma-separated properties to provide in response (optional)
     * @return Success (status code 200)
     *         or Bad Request (status code 400)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     *         or Not Found (status code 404)
     *         or Method Not allowed (status code 405)
     *         or Conflict (status code 409)
     *         or Internal Server Error (status code 500)
     * @see AlarmApi#retrieveAlarm
     */
    default ResponseEntity<Alarm> retrieveAlarm(String id,
        String fields) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json;charset=utf-8"))) {
                    String exampleString = "{ \"reportingSystemId\" : \"reportingSystemId\", \"alarmRaisedTime\" : \"2000-01-23T04:56:07.000+00:00\", \"alarmClearedTime\" : \"2000-01-23T04:56:07.000+00:00\", \"alarmedObjectType\" : \"alarmedObjectType\", \"@type\" : \"@type\", \"proposedRepairedActions\" : \"proposedRepairedActions\", \"correlatedAlarm\" : [ { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"alarmReportingTime\" : \"2000-01-23T04:56:07.000+00:00\", \"alarmedObject\" : { \"id\" : \"id\", \"href\" : \"href\" }, \"alarmEscalation\" : true, \"alarmChangedTime\" : \"2000-01-23T04:56:07.000+00:00\", \"@baseType\" : \"@baseType\", \"probableCause\" : \"probableCause\", \"perceivedSeverity\" : \"CRITICAL\", \"affectedService\" : [ { \"id\" : \"id\", \"href\" : \"href\" }, { \"id\" : \"id\", \"href\" : \"href\" } ], \"ackSystemId\" : \"ackSystemId\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"state\" : \"state\", \"place\" : [ { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"href\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"role\" : \"role\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"href\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"crossedThresholdInformation\" : { \"indicatorName\" : \"indicatorName\", \"thresholdCrossingDescription\" : \"thresholdCrossingDescription\", \"granularity\" : \"granularity\", \"observedValue\" : \"observedValue\", \"threshold\" : { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, \"direction\" : \"direction\", \"indicatorUnit\" : \"indicatorUnit\" }, \"@schemaLocation\" : \"https://openapi-generator.tech\", \"externalAlarmId\" : \"externalAlarmId\", \"clearSystemId\" : \"clearSystemId\", \"parentAlarm\" : [ { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" }, { \"@referredType\" : \"@referredType\", \"@baseType\" : \"@baseType\", \"@type\" : \"@type\", \"name\" : \"name\", \"id\" : \"id\", \"href\" : \"https://openapi-generator.tech\", \"@schemaLocation\" : \"https://openapi-generator.tech\" } ], \"isRootCause\" : true, \"ackUserId\" : \"ackUserId\", \"clearUserId\" : \"clearUserId\", \"serviceAffecting\" : true, \"sourceSystemId\" : \"sourceSystemId\", \"ackState\" : \"ackState\", \"alarmType\" : \"communicationsAlarm\", \"specificProblem\" : \"specificProblem\", \"plannedOutageIndicator\" : \"plannedOutageIndicator\", \"comment\" : [ { \"systemId\" : \"systemId\", \"comment\" : \"comment\", \"time\" : \"2000-01-23T04:56:07.000+00:00\", \"userId\" : \"userId\" }, { \"systemId\" : \"systemId\", \"comment\" : \"comment\", \"time\" : \"2000-01-23T04:56:07.000+00:00\", \"userId\" : \"userId\" } ], \"alarmDetails\" : \"alarmDetails\" }";
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
