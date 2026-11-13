package org.ikigaidigital.adapter.persistence

import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.application.TimeDepositReadModel
import org.ikigaidigital.application.WithdrawalReadModel
import org.ikigaidigital.application.port.TimeDepositRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class JpaTimeDepositAdapter(
    private val repository: SpringDataDepositRepository
) : TimeDepositRepository {
    override fun findAll(): List<TimeDepositReadModel> =
        repository.findAllByOrderByIdAsc().map { it.toReadModel() }

    override fun saveAll(deposits: List<TimeDeposit>) {
        val entitiesById = repository.findAllById(deposits.map { it.id }).associateBy { it.id }
        deposits.forEach { deposit ->
            entitiesById[deposit.id]?.balance = BigDecimal.valueOf(deposit.balance)
        }
        repository.saveAll(entitiesById.values)
    }

    private fun TimeDepositEntity.toReadModel() = TimeDepositReadModel(
        deposit = TimeDeposit(
            id = id,
            planType = planType,
            balance = balance.toDouble(),
            days = days
        ),
        withdrawals = withdrawals.map { WithdrawalReadModel(it.id, it.amount, it.date) }
    )
}
