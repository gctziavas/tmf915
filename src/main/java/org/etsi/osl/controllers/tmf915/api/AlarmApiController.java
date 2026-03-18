package org.etsi.osl.controllers.tmf915.api;

import org.etsi.osl.controllers.tmf915.model.Alarm;
import org.etsi.osl.controllers.tmf915.model.AlarmCreate;
import org.etsi.osl.controllers.tmf915.model.AlarmUpdate;
import org.etsi.osl.controllers.tmf915.model.Error;
import org.springframework.lang.Nullable;


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
public class AlarmApiController implements AlarmApi {

    private final AlarmApiDelegate delegate;

    public AlarmApiController(@Autowired(required = false) AlarmApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new AlarmApiDelegate() {});
    }

    @Override
    public AlarmApiDelegate getDelegate() {
        return delegate;
    }

}
