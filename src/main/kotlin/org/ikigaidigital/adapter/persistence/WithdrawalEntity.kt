package org.ikigaidigital.adapter.persistence

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "withdrawals")
class WithdrawalEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val amount: BigDecimal,
    val date: LocalDate,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_deposit_id", nullable = false)
    val timeDeposit: TimeDepositEntity
)
