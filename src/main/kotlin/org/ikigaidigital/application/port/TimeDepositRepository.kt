package org.ikigaidigital.application.port

import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.application.TimeDepositReadModel

interface TimeDepositRepository {
    fun findAll(): List<TimeDepositReadModel>
    fun saveAll(deposits: List<TimeDeposit>)
}
