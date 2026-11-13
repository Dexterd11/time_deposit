package org.ikigaidigital.application

import org.ikigaidigital.TimeDeposit
import java.math.BigDecimal
import java.time.LocalDate

data class WithdrawalReadModel(
    val id: Int,
    val amount: BigDecimal,
    val date: LocalDate
)

data class TimeDepositReadModel(
    val deposit: TimeDeposit,
    val withdrawals: List<WithdrawalReadModel>
)
