package com.mazekine.everscale.addressMiner

import tech.deplant.java4ever.framework.Credentials

data class Match(
    var address: String,
    var credentials: Credentials,
    var score: Float,
    var mask: String,
    var balance: String,
)
