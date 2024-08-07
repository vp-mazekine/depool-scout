package com.mazekine.depool.scout

import java.math.BigDecimal

data class DePoolStakes(
    val ordinaryStake: BigDecimal,
    val lockedStake: BigDecimal,
    val vestingStake: BigDecimal,
)
