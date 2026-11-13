package org.ikigaidigital

import org.assertj.core.api.Assertions.assertThat
import org.ikigaidigital.application.TimeDepositReadModel
import org.ikigaidigital.application.TimeDepositService
import org.ikigaidigital.application.port.TimeDepositRepository
import org.junit.jupiter.api.Test

class TimeDepositServiceTest {
    @Test
    fun findAll_returnsReadModelsFromRepositoryPort() {
        val deposit = TimeDeposit(1, "basic", 1200.00, 45)
        val readModel = TimeDepositReadModel(deposit, emptyList())
        val repository = InMemoryTimeDepositRepository(mutableListOf(readModel))
        val service = TimeDepositService(repository, TimeDepositCalculator())

        val result = service.findAll()

        assertThat(result).containsExactly(readModel)
    }

    @Test
    fun updateBalances_calculatesAndPersistsEveryDepositThroughRepositoryPort() {
        val deposits = listOf(
            TimeDepositReadModel(TimeDeposit(1, "basic", 1200.00, 45), emptyList()),
            TimeDepositReadModel(TimeDeposit(2, "student", 1200.00, 365), emptyList())
        )
        val repository = InMemoryTimeDepositRepository(deposits.toMutableList())
        val service = TimeDepositService(repository, TimeDepositCalculator())

        service.updateBalances()

        assertThat(repository.saved.map { it.balance })
            .containsExactly(1201.00, 1203.00)
    }

    private class InMemoryTimeDepositRepository(
        private val readModels: MutableList<TimeDepositReadModel>
    ) : TimeDepositRepository {
        val saved = mutableListOf<TimeDeposit>()

        override fun findAll() = readModels

        override fun saveAll(deposits: List<TimeDeposit>) {
            saved.addAll(deposits)
        }
    }
}
