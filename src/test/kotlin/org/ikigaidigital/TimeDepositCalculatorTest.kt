package org.ikigaidigital

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test

class TimeDepositCalculatorTest {
    private val calculator = TimeDepositCalculator()

    @Test
    fun basicBeforeEligibility_doesNotEarnInterest() {
        val deposit = deposit(planType = "basic", days = 30)

        calculator.updateBalance(listOf(deposit))

        assertThat(deposit.balance).isEqualTo(1200.00)
    }

    @Test
    fun basicFirstEligibleDay_earnsOnePercentAnnualRateDividedByTwelve() {
        val deposit = deposit(planType = "basic", days = 31)

        calculator.updateBalance(listOf(deposit))

        assertThat(deposit.balance).isEqualTo(1201.00)
    }

    @Test
    fun basicAfterOneYear_continuesToEarnInterest() {
        val deposit = deposit(planType = "basic", days = 400)

        calculator.updateBalance(listOf(deposit))

        assertThat(deposit.balance).isEqualTo(1201.00)
    }

    @Test
    fun studentFirstEligibleDay_earnsThreePercentAnnualRateDividedByTwelve() {
        val deposit = deposit(planType = "student", days = 31)

        calculator.updateBalance(listOf(deposit))

        assertThat(deposit.balance).isEqualTo(1203.00)
    }

    @Test
    fun studentLastEligibleDay_earnsInterest() {
        val deposit = deposit(planType = "student", days = 365)

        calculator.updateBalance(listOf(deposit))

        assertThat(deposit.balance).isEqualTo(1203.00)
    }

    @Test
    fun studentExpired_doesNotEarnInterest() {
        val deposit = deposit(planType = "student", days = 366)

        calculator.updateBalance(listOf(deposit))

        assertThat(deposit.balance).isEqualTo(1200.00)
    }

    @Test
    fun premiumBeforeEligibility_doesNotEarnInterest() {
        val deposit = deposit(planType = "premium", days = 45)

        calculator.updateBalance(listOf(deposit))

        assertThat(deposit.balance).isEqualTo(1200.00)
    }

    @Test
    fun premiumFirstEligibleDay_earnsFivePercentAnnualRateDividedByTwelve() {
        val deposit = deposit(planType = "premium", days = 46)

        calculator.updateBalance(listOf(deposit))

        assertThat(deposit.balance).isEqualTo(1205.00)
    }

    @Test
    fun unknownPlanType_doesNotEarnInterest() {
        val deposit = deposit(planType = "unknown", days = 60)

        calculator.updateBalance(listOf(deposit))

        assertThat(deposit.balance).isEqualTo(1200.00)
    }

    @Test
    fun interest_isRoundedToTwoDecimalPlaces() {
        val deposit = deposit(planType = "basic", balance = 1000.00, days = 60)

        calculator.updateBalance(listOf(deposit))

        assertThat(deposit.balance).isEqualTo(1000.83)
    }

    @Test
    fun multipleDeposits_updatesEveryDepositAccordingToItsPlan() {
        val deposits = listOf(
            deposit(id = 1, planType = "basic", days = 31),
            deposit(id = 2, planType = "student", days = 365),
            deposit(id = 3, planType = "premium", days = 46),
            deposit(id = 4, planType = "unknown", days = 60),
            deposit(id = 5, planType = "basic", days = 30)
        )

        calculator.updateBalance(deposits)

        assertThat(deposits.map { it.balance })
            .containsExactly(1201.00, 1203.00, 1205.00, 1200.00, 1200.00)
    }

    @Test
    fun emptyList_doesNotThrow() {
        assertThatCode { calculator.updateBalance(emptyList()) }
            .doesNotThrowAnyException()
    }

    @Test
    fun repeatedInvocation_appliesInterestAgainToUpdatedBalance() {
        val deposit = deposit(planType = "basic", days = 60)

        calculator.updateBalance(listOf(deposit))
        calculator.updateBalance(listOf(deposit))

        assertThat(deposit.balance).isEqualTo(1202.00)
    }

    @Test
    fun uppercasePlanType_doesNotEarnInterest() {
        val deposit = deposit(planType = "BASIC", days = 60)

        calculator.updateBalance(listOf(deposit))

        assertThat(deposit.balance).isEqualTo(1200.00)
    }

    @Test
    fun interest_roundsUpWhenThirdDecimalIsAboveFive() {
        val deposit = deposit(
            planType = "basic",
            balance = 1004.00,
            days = 60
        )

        calculator.updateBalance(listOf(deposit))

        assertThat(deposit.balance).isEqualTo(1004.84)
    }

    private fun deposit(
        id: Int = 1,
        planType: String,
        balance: Double = 1200.00,
        days: Int
    ) = TimeDeposit(id, planType, balance, days)
}
