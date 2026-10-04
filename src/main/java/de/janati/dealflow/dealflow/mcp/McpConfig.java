package de.janati.dealflow.mcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfig {

    @Bean
    ToolCallbackProvider dealflowToolCallbacks(DealflowTools tools) {
        return MethodToolCallbackProvider.builder().toolObjects(tools).build();
    }
}