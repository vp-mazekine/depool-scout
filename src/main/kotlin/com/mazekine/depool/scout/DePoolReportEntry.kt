package com.mazekine.depool.scout

import java.math.BigDecimal
import java.time.LocalDateTime

data class DePoolReportEntry(
    val depool: String,
    val stakeholder: String,
    var ordinaryStake: BigDecimal? = null,
    var lockedStake: LockedOrVestingStakeEntry? = null,
    var vestingStake: LockedOrVestingStakeEntry? = null,
)

data class LockedOrVestingStakeEntry(
    var remainingAmount: BigDecimal,
    var withdrawalAmount: BigDecimal,
    var endsOn: LocalDateTime,
    var owner: String,
    var beneficiary: String,
)