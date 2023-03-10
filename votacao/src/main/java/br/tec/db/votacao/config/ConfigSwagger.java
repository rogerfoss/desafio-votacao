package br.tec.db.votacao.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigSwagger {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Votação para assembleias da cooperativa")
                        .contact(new Contact()
                                .name("Roger Siqueira")
                                .email("roger.siqueira@db.tec.br"))
                        .description("API para gerenciamento de sessões de votação")
                        .version("1.0.0"));
    }
}
