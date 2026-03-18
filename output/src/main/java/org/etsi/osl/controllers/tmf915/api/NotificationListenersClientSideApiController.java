package org.etsi.osl.controllers.tmf915.api;

import org.etsi.osl.controllers.tmf915.model.AiContractAttributeValueChangeEvent;
import org.etsi.osl.controllers.tmf915.model.AiContractCreateEvent;
import org.etsi.osl.controllers.tmf915.model.AiContractDeleteEvent;
import org.etsi.osl.controllers.tmf915.model.AiContractSpecificationAttributeValueChangeEvent;
import org.etsi.osl.controllers.tmf915.model.AiContractSpecificationCreateEvent;
import org.etsi.osl.controllers.tmf915.model.AiContractSpecificationDeleteEvent;
import org.etsi.osl.controllers.tmf915.model.AiContractStateChangeEvent;
import org.etsi.osl.controllers.tmf915.model.AiContractViolationAttributeValueChangeEvent;
import org.etsi.osl.controllers.tmf915.model.AiContractViolationCreateEvent;
import org.etsi.osl.controllers.tmf915.model.AiContractViolationDeleteEvent;
import org.etsi.osl.controllers.tmf915.model.AiModelAttributeValueChangeEvent;
import org.etsi.osl.controllers.tmf915.model.AiModelCreateEvent;
import org.etsi.osl.controllers.tmf915.model.AiModelDeleteEvent;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationAttributeValueChangeEvent;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreateEvent;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationDeleteEvent;
import org.etsi.osl.controllers.tmf915.model.AiModelStateChangeEvent;
import org.etsi.osl.controllers.tmf915.model.AlarmAttributeValueChangeEvent;
import org.etsi.osl.controllers.tmf915.model.AlarmCreateEvent;
import org.etsi.osl.controllers.tmf915.model.AlarmDeleteEvent;
import org.etsi.osl.controllers.tmf915.model.AlarmStateChangeEvent;
import org.etsi.osl.controllers.tmf915.model.Error;
import org.etsi.osl.controllers.tmf915.model.EventCreateEvent;
import org.etsi.osl.controllers.tmf915.model.EventSubscription;
import org.etsi.osl.controllers.tmf915.model.MonitorAttributeValueChangeEvent;
import org.etsi.osl.controllers.tmf915.model.MonitorCreateEvent;
import org.etsi.osl.controllers.tmf915.model.MonitorDeleteEvent;
import org.etsi.osl.controllers.tmf915.model.MonitorStateChangeEvent;
import org.etsi.osl.controllers.tmf915.model.RuleAttributeValueChangeEvent;
import org.etsi.osl.controllers.tmf915.model.RuleCreateEvent;
import org.etsi.osl.controllers.tmf915.model.RuleDeleteEvent;
import org.etsi.osl.controllers.tmf915.model.RuleStateChangeEvent;
import org.etsi.osl.controllers.tmf915.model.TopicChangeEvent;
import org.etsi.osl.controllers.tmf915.model.TopicCreateEvent;
import org.etsi.osl.controllers.tmf915.model.TopicDeleteEvent;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
@Controller
@RequestMapping("${openapi.aiManagement.base-path:/tmf-api/AiM/v4}")
public class NotificationListenersClientSideApiController implements NotificationListenersClientSideApi {

    private final NotificationListenersClientSideApiDelegate delegate;

    public NotificationListenersClientSideApiController(@Autowired(required = false) NotificationListenersClientSideApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new NotificationListenersClientSideApiDelegate() {});
    }

    @Override
    public NotificationListenersClientSideApiDelegate getDelegate() {
        return delegate;
    }

}
