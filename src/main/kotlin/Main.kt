package com.mazekine.everscale.addressMiner

import network.everscale.SetcodeMultisigTemplate
import tech.deplant.java4ever.framework.Credentials
import tech.deplant.java4ever.framework.Sdk
import java.math.BigInteger

fun main(args: Array<String>) {
    val sdk = Sdk.DEFAULT()

    /*ContractWrapper.generate(
        ContractAbi.ofResource("SetcodeMultisig.abi.json").abiContract,
        Tvc.ofResource("SetcodeMultisig.tvc"),
        Path.of("src/main/java/generated"),
        "SetcodeMultisig",
        "network.everscale.SetcodeMultisig",
        "network.everscale.SetcodeMultisigTemplate",
        false,
        arrayOf<String>()
    )*/

    val walletTemplate = SetcodeMultisigTemplate()
    val deployHandle = walletTemplate.prepareDeploy(sdk, Credentials.NONE, arrayOf<BigInteger>(), 3, 36000)
    println(deployHandle.toAddress())
}
