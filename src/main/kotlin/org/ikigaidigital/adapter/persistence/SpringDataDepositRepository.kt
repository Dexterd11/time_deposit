package org.ikigaidigital.adapter.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface SpringDataDepositRepository : JpaRepository<TimeDepositEntity, Int>
