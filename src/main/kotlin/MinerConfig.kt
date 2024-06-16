package com.mazekine.everscale.addressMiner

data class MinerConfig(
    val target: String,
    val type: ContractType,
    val owners: List<String>,
    val reqConfirms: Int,
    val lifetime: Int = 86400,
)
