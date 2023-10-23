package br.com.morsesystems.location.application.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private final String title = "Location API";
    private final String version;
    private final String contactName = "Diego Campos";
    private final String contactMail = "kamposdiego@outlook.com";

    public OpenApiConfig(@Value("${app.version}") String version) {
        this.version = version;
    }

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title(title)
                        .version(version)
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name(contactName)
                                .email(contactMail)));
    }

}
