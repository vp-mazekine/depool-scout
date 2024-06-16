package com.mazekine.everscale.addressMiner

import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import network.everscale.SetcodeMultisigTemplate
import tech.deplant.java4ever.binding.EverSdk
import tech.deplant.java4ever.framework.Credentials
import java.io.File
import java.math.BigInteger
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println(
            """
USAGE:
    java -jar everscale-address-miner.jar <CONFIG>

CONFIG:
    JSON configuration file.
        """,
        )
        exitProcess(-1)
    }

    val configFile = args[0]

    if (!File(configFile).exists()) {
        println("ERROR: Configuration file does not exist")
        exitProcess(-1)
    }

    val gson =
        GsonBuilder()
            .setStrictness(Strictness.LENIENT)
            .setPrettyPrinting()
            .create()

    val config = gson.fromJson(File(configFile).readText(), MinerConfig::class.java)

    val target = checkEverAddressMask(config.target.lowercase())
    checkContractSupport(config.type)
    checkOwners(config.owners)
    checkConfirmations(config.owners, config.reqConfirms)
    checkLifetime(config.lifetime)

    /*
    ContractWrapper.generate(
        ContractAbi.ofResource("SetcodeMultisig.abi.json").abiContract,
        Tvc.ofResource("SetcodeMultisig.tvc"),
        Path.of("src/main/java"),
        "SetcodeMultisig",
        "SetcodeMultisig",
        "SetcodeMultisigTemplate",
        "network.everscale",
        "network.everscale",
        false,
        arrayOf<String>(),
    )*/

    EverSdk.load()
    val contextId = EverSdk.createDefault()
    val keys = Credentials.ofRandom(contextId)

    val walletTemplate = SetcodeMultisigTemplate()
    val deployHandle =
        walletTemplate.prepareDeploy(
            contextId,
            0,
            keys,
            arrayOf<BigInteger>(),
            3,
            36000,
        )

    val result = deployHandle.toAddress()
    val result2 = result.toString().split(":").last()

    println(result2)
    println("Match: " + "%.2f%%".format(assessMatch(target, result2, getNormalizedWeights(result2.length)) * 100))
}

private fun checkEverAddressMask(mask: String): String {
    require(mask.isNotEmpty()) { "ERROR: Address target may not be empty" }

    val parts = mask.split(':')
    require(parts.size <= 2) { "ERROR: Incorrect address format" }

    var targetMask = parts.last()
    require(targetMask.length <= 64) { "ERROR: Incorrect address length" }
    require(targetMask.matches(Regex("[a-z0-9]+"))) { "ERROR: Incorrect symbols in address" }

    targetMask += "0".repeat(64 - targetMask.length) //  In case the mask is not full, we suppose it has trailing zeroes at the end

    return targetMask
}

private fun checkContractSupport(contractType: ContractType) {
    require(contractType == ContractType.SETCODE_2_2) { "ERROR: Only SetcodeMultisig v. 2.2 is supported" }
}

private fun checkOwners(owners: List<String>) {
}

private fun checkConfirmations(
    owners: List<String>,
    reqConfirms: Int,
) {
    require(reqConfirms > 0) { "ERROR: Confirmations number must be a positive integer" }
    require(owners.size >= reqConfirms) { "ERROR: Confirmations number must be equal or less to owners count" }
}

private fun checkLifetime(lifetime: Int) {
    require(lifetime > 0) { "ERROR: Lifetime must be greater than zero" }
}

private fun assessMatch(
    target: String,
    value: String,
    weighs: List<Float>? = null,
): Float {
    var matchingSymbols: String = ""
    var matchScore: Float = 0F
    value.mapIndexed { index, c ->
        if (c == target[index]) {
            matchingSymbols += c
            matchScore += weighs?.get(index) ?: (1F / target.length)
        } else {
            matchingSymbols += "-"
        }
    }
    println(matchingSymbols)

    return matchScore // matchingSymbols.count { c -> c != '-' }.toFloat() / target.length
}

private fun getNormalizedWeights(
    quantity: Int,
    reverse: Boolean = false,
): List<Float> {
    val raw: MutableList<Int> = mutableListOf()
    val from = if (reverse) quantity - 1 else 0
    val to = if (reverse) 0 else quantity - 1
    val step = if (reverse) -1 else 1
    for (i in from..to step step) {
        raw.add(i)
    }
    val total = raw.sum()

    return raw
        .map { item ->
            item.toFloat() / total
        }.toMutableList()
}
