package org.ikigaidigital.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfiguration {
    @Bean
    fun timeDepositOpenApi() = OpenAPI()
        .info(
            Info()
                .title("Time Deposit API")
                .description("API for retrieving time deposits and updating their balances")
                .version("v1")
        )
}
