package org.ikigaidigital

data class TimeDeposit(
    val id: Int,
    // Keep String for backward compatibility.
    // TODO: Consider using PlanType enum in a future breaking version.
    val planType: String,
    var balance: Double,
    val days: Int
)
