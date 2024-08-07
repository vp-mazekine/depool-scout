package com.mazekine.depool.scout

import java.math.BigInteger

data class LockedOrVestedStake(
    val remainingAmount: BigInteger,
    val lastWithdrawalTime: Long,
    val withdrawalPeriod: Long,
    val withdrawalValue: BigInteger,
    val owner: String,
)
