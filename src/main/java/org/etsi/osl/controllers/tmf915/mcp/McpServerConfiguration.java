package org.etsi.osl.controllers.tmf915.mcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfiguration {

    @Bean
    public ToolCallbackProvider aiCatalogToolCallbacks(AiCatalogMcpTools aiCatalogMcpTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(aiCatalogMcpTools)
                .build();
    }
}
