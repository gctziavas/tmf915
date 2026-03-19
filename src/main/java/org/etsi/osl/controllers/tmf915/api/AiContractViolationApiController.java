package org.etsi.osl.controllers.tmf915.api;

import jakarta.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
@Controller
@RequestMapping("${openapi.aiManagement.base-path:/tmf-api/AiM/v4}")
public class AiContractViolationApiController implements AiContractViolationApi {

    private final AiContractViolationApiDelegate delegate;

    public AiContractViolationApiController(@Autowired(required = false) AiContractViolationApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new AiContractViolationApiDelegate() {});
    }

    @Override
    public AiContractViolationApiDelegate getDelegate() {
        return delegate;
    }

}
