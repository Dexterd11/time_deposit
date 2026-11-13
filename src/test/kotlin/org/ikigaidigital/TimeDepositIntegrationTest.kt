package org.ikigaidigital

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal
import java.time.LocalDate

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class TimeDepositIntegrationTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun resetDatabase() {
        jdbcTemplate.update("DELETE FROM withdrawals")
        jdbcTemplate.update("DELETE FROM time_deposits")
        jdbcTemplate.update(
            "INSERT INTO time_deposits (id, plan_type, balance, days) VALUES (?, ?, ?, ?)",
            1, "basic", BigDecimal("1200.00"), 60
        )
        jdbcTemplate.update(
            "INSERT INTO time_deposits (id, plan_type, balance, days) VALUES (?, ?, ?, ?)",
            2, "student", BigDecimal("1200.00"), 365
        )
        jdbcTemplate.update(
            "INSERT INTO withdrawals (amount, date, time_deposit_id) VALUES (?, ?, ?)",
            BigDecimal("25.50"), LocalDate.of(2026, 1, 15), 1
        )
    }

    @Test
    fun flyway_createsBothRequiredTables() {
        val migrationCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM flyway_schema_history WHERE version = '1'",
            Int::class.java
        )!!
        val tableCount = jdbcTemplate.queryForObject(
            """
            SELECT COUNT(*)
            FROM information_schema.tables
            WHERE table_schema = 'public'
              AND table_name IN ('time_deposits', 'withdrawals')
            """.trimIndent(),
            Int::class.java
        )!!

        assertThat(migrationCount).isEqualTo(1)
        assertThat(tableCount).isEqualTo(2)
    }

    @Test
    fun getTimeDeposits_returnsDepositsAndNestedWithdrawals() {
        mockMvc.perform(get("/api/time-deposits"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", hasSize<Any>(2)))
            .andExpect(jsonPath("$[0].id", equalTo(1)))
            .andExpect(jsonPath("$[0].planType", equalTo("basic")))
            .andExpect(jsonPath("$[0].balance", equalTo(1200.0)))
            .andExpect(jsonPath("$[0].withdrawals", hasSize<Any>(1)))
            .andExpect(jsonPath("$[0].withdrawals[0].amount", equalTo(25.50)))
            .andExpect(jsonPath("$[0].withdrawals[0].date", equalTo("2026-01-15")))
    }

    @Test
    fun putBalances_updatesAndPersistsEveryBalance() {
        mockMvc.perform(put("/api/time-deposits/balances"))
            .andExpect(status().isNoContent)

        val basicBalance = jdbcTemplate.queryForObject(
            "SELECT balance FROM time_deposits WHERE id = 1",
            BigDecimal::class.java
        )!!
        val studentBalance = jdbcTemplate.queryForObject(
            "SELECT balance FROM time_deposits WHERE id = 2",
            BigDecimal::class.java
        )!!

        assertThat(basicBalance).isEqualByComparingTo("1201.00")
        assertThat(studentBalance).isEqualByComparingTo("1203.00")
    }

    companion object {
        @Container
        @ServiceConnection
        @JvmStatic
        val postgres = PostgreSQLContainer<Nothing>("postgres:16-alpine").apply {
            withDatabaseName("time_deposit_test")
            withUsername("time_deposit_test")
            withPassword("time_deposit_test")
        }
    }
}
