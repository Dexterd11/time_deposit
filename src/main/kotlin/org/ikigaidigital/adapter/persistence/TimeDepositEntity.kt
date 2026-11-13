package org.ikigaidigital.adapter.persistence

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "time_deposits")
class TimeDepositEntity(
    @Id
    val id: Int,
    val planType: String,
    var balance: BigDecimal,
    val days: Int,
    @OneToMany(mappedBy = "timeDeposit", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val withdrawals: MutableList<WithdrawalEntity> = mutableListOf()
)
