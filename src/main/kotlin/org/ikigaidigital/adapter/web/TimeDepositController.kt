package org.ikigaidigital.adapter.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.ikigaidigital.application.TimeDepositService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/time-deposits")
@Tag(name = "Time deposits")
class TimeDepositController(
    private val service: TimeDepositService
) {
    @GetMapping
    @Operation(summary = "Retrieve all time deposits")
    fun findAll(): List<TimeDepositResponse> = service.findAll().map(TimeDepositResponse::from)

    @PutMapping("/balances")
    @Operation(summary = "Update balances for all time deposits")
    fun updateBalances(): ResponseEntity<Void> {
        service.updateBalances()
        return ResponseEntity.noContent().build()
    }
}
