package org.ikigaidigital.adapter.web

import org.ikigaidigital.application.TimeDepositReadModel

data class TimeDepositResponse(
    val id: Int,
    val planType: String,
    val balance: Double,
    val days: Int,
    val withdrawals: List<WithdrawalResponse>
) {
    companion object {
        fun from(readModel: TimeDepositReadModel) = TimeDepositResponse(
            id = readModel.deposit.id,
            planType = readModel.deposit.planType,
            balance = readModel.deposit.balance,
            days = readModel.deposit.days,
            withdrawals = readModel.withdrawals.map(WithdrawalResponse::from)
        )
    }
}
