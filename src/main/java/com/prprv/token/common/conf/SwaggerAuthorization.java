package com.prprv.token.common.conf;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * 添加Swagger全局认证按钮
 * @author Yoooum
 */
@Configuration
public class SwaggerAuthorization {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .schemaRequirement(HttpHeaders.AUTHORIZATION, this.securityScheme())
                .addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION));
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT")
                .name(HttpHeaders.AUTHORIZATION)
                .in(SecurityScheme.In.HEADER);
    }
}
