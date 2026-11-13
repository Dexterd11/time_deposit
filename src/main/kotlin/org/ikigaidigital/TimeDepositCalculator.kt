package org.ikigaidigital

import java.math.BigDecimal
import java.math.RoundingMode

private const val MONTHS_PER_YEAR = 12
private const val BASIC_RATE = 0.01
private const val STUDENT_RATE = 0.03
private const val PREMIUM_RATE = 0.05

class TimeDepositCalculator {

    fun updateBalance(xs: List<TimeDeposit>) {
        xs.forEach { deposit ->
            val interest = calculateInterest(deposit)
            // Keep original rounding behaviour for backward compatibility.
            // TODO: Consider using BigDecimal for monetary calculations in a future version.
            val roundedInterest = BigDecimal(interest)
                .setScale(2, RoundingMode.HALF_UP)
            // TODO: Prevent interest from being applied multiple times for the same period.
            deposit.balance += roundedInterest.toDouble()
        }
    }

    private fun calculateInterest(deposit: TimeDeposit): Double {
        if (deposit.days <= 30) return 0.0
        if (deposit.planType == "student" && deposit.days >= 366) return 0.0
        if (deposit.planType == "premium" && deposit.days <= 45) return 0.0

        val rate = when (deposit.planType) {
            "student" -> STUDENT_RATE
            "premium" -> PREMIUM_RATE
            "basic" -> BASIC_RATE
            else -> return 0.0
        }
        return deposit.balance * rate / MONTHS_PER_YEAR
    }

}