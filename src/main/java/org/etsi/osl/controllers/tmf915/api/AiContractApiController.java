package org.etsi.osl.controllers.tmf915.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
@Controller
@RequestMapping("${openapi.aiManagement.base-path:/tmf-api/AiM/v4}")
public class AiContractApiController implements AiContractApi {

    private final AiContractApiDelegate delegate;

    public AiContractApiController(@Autowired(required = false) AiContractApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new AiContractApiDelegate() {});
    }

    @Override
    public AiContractApiDelegate getDelegate() {
        return delegate;
    }

}
