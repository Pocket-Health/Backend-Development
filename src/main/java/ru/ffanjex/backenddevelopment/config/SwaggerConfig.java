package ru.ffanjex.backenddevelopment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .servers(
                        List.of(
                                new Server().url("https://pockethealth.nmasalkin.ru").description("Production server"),
                                new Server().url("http://localhost:8087").description("Local development server")
                        )
                )
                .info(new Info()
                        .title("Backend API")
                        .description("API documentation for the medical application")
                        .version("1.0.0")
                );
    }
}
