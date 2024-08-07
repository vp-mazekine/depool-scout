package com.mazekine.depool.scout

data class AppConfig(
    val network: NetworkConfig,
)

data class NetworkConfig(
    val endpoint: String = "https://mainnet.evercloud.dev",
)
