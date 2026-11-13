package org.ikigaidigital.adapter.web

import org.ikigaidigital.application.WithdrawalReadModel
import java.math.BigDecimal
import java.time.LocalDate

data class WithdrawalResponse(
    val id: Int,
    val amount: BigDecimal,
    val date: LocalDate
) {
    companion object {
        fun from(withdrawal: WithdrawalReadModel) = WithdrawalResponse(
            id = withdrawal.id,
            amount = withdrawal.amount,
            date = withdrawal.date
        )
    }
}
