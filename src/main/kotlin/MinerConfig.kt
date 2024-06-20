package com.mazekine.everscale.addressMiner

import com.mazekine.everscale.addressMiner.com.mazekine.everscale.addressMiner.WeighsAlgorithm

data class MinerConfig(
    val target: String,
    val type: ContractType,
    val owners: List<String>,
    val reqConfirms: Int,
    val lifetime: Long = 86400,
    val processes: Int = 100,
    val weighsAlgorithm: WeighsAlgorithm,
    val endpoint: String,
    val startFromPk: String?,
)
