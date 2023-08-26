package br.com.sicredi.coopvote.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Cooperative Voting Manager")
                .version("0.0.1")
                .description(
                    "API to manage cooperative voting sessions. Features agenda creation, vote tallying, and session handling."));
  }
}
