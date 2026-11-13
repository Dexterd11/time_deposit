package org.ikigaidigital.application

import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.TimeDepositCalculator
import org.ikigaidigital.application.port.TimeDepositRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TimeDepositService(
    private val repository: TimeDepositRepository,
    private val calculator: TimeDepositCalculator
) {
    @Transactional(readOnly = true)
    fun findAll(): List<TimeDepositReadModel> = repository.findAll()

    @Transactional
    fun updateBalances() {
        val deposits = repository.findAll().map { it.deposit }
        calculator.updateBalance(deposits)
        repository.saveAll(deposits)
    }
}
