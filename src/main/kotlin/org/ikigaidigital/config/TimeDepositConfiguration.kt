package org.ikigaidigital.config

import org.ikigaidigital.TimeDepositCalculator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TimeDepositConfiguration {
    @Bean
    fun timeDepositCalculator() = TimeDepositCalculator()
}
