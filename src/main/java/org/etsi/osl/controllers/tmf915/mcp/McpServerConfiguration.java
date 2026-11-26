package org.etsi.osl.controllers.tmf915.mcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers the read-only MCP tools.
 *
 * <p>The MCP endpoints ({@code /sse}, {@code /mcp/**}) require no token (decision D2 in
 * {@code docs/BUG_FIX_PLAN.md}): access must be restricted at the network level. A warning is
 * logged at startup so that operators see this.
 */
@Configuration
public class McpServerConfiguration {

    private static final Logger log = LoggerFactory.getLogger(McpServerConfiguration.class);

    @Bean
    public ToolCallbackProvider aiCatalogToolCallbacks(AiCatalogMcpTools aiCatalogMcpTools) {
        log.warn("MCP endpoints /tmf-api/sse and /tmf-api/mcp/** are NOT authenticated: anyone who can reach "
                + "this port can list AI model specifications and deployed models. Restrict them at the network "
                + "level (firewall or reverse proxy); see docs/INTEGRATION_GUIDE.md section 6.");
        return MethodToolCallbackProvider.builder()
                .toolObjects(aiCatalogMcpTools)
                .build();
    }
}
