package com.mazekine.everscale.addressMiner

import com.github.mm.coloredconsole.colored
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import com.mazekine.everscale.addressMiner.com.mazekine.everscale.addressMiner.WeighsAlgorithm
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import network.everscale.SetcodeMultisigTemplate
import tech.deplant.java4ever.binding.Crypto
import tech.deplant.java4ever.binding.EverSdk
import tech.deplant.java4ever.framework.Account
import tech.deplant.java4ever.framework.Credentials
import java.io.File
import java.math.BigInteger
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import kotlin.system.exitProcess

val bestMatch = Match("", Credentials.NONE, 0.0F, "", "")
val gson: Gson =
    GsonBuilder()
        .setStrictness(Strictness.LENIENT)
        .setPrettyPrinting()
        .create()
val logName = "addressMiner_log_" + System.currentTimeMillis() + ".log"
var balanceAvailable = false

sealed class Command

data object Pause : Command()

data object Quit : Command()

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

    val config = gson.fromJson(File(configFile).readText(), MinerConfig::class.java)

    val target = checkEverAddressMask(config.target.lowercase())
    checkContractSupport(config.type)

    checkOwners(config.owners)
    val ownersBI = config.owners.map { o -> BigInteger(o, 16) }.toTypedArray()

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
    val contextId: Int
    if (config.endpoint.trim() == "") {
        contextId = EverSdk.createDefault()
    } else {
        contextId = EverSdk.createWithEndpoint(config.endpoint)
        balanceAvailable = true
    }

    println("Heap size: " + "# ### ###".format(Runtime.getRuntime().totalMemory()))

    runBlocking {
        config.startFromPk?.let {
            if (it.matches(Regex("[a-f0-9]{64}"))) {
                getCorrelationPairs(
                    it,
                    10_000,
                    contextId,
                    ownersBI,
                    config.reqConfirms,
                    config.lifetime,
                )
                return@runBlocking
            }
        }

        val commandChannel = Channel<Command>()
        val job =
            launch(Dispatchers.Default) {
                runSearch(
                    commandChannel,
                    target.split(":").last(),
                    contextId,
                    ownersBI,
                    config.reqConfirms,
                    config.lifetime,
                    config.processes,
                    config.weighsAlgorithm,
                )
            }

        launch {
            val scanner = Scanner(System.`in`)
            while (true) {
                when (scanner.nextLine()) {
                    "p" -> commandChannel.send(Pause)
                    "q" -> {
                        commandChannel.send(Quit)
                        break
                    }
                }
            }
        }

        job.join()
        commandChannel.close()
    }
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

private fun checkLifetime(lifetime: Long) {
    require(lifetime > 0) { "ERROR: Lifetime must be greater than zero" }
}

private fun assessMatch(
    target: String,
    value: String,
    weighs: List<Float>? = null,
): Pair<Float, String> {
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

    return Pair(matchScore, matchingSymbols) // matchingSymbols.count { c -> c != '-' }.toFloat() / target.length
}

private fun getNormalizedWeights(
    quantity: Int,
    algorithm: WeighsAlgorithm,
): List<Float> {
    val raw: MutableList<Float> = mutableListOf()

    when (algorithm) {
        WeighsAlgorithm.TARGET_FIRST -> for (i in 1..quantity) {
            val q = quantity.toFloat()
            val t = i.toFloat()
            raw.add((q - t) / q)
        }

        WeighsAlgorithm.TARGET_MIDDLE -> for (i in 1..quantity) {
            raw.add(
                -4 * (i.toFloat() - (1 + quantity.toFloat()) / 2).pow(2) / (quantity.toFloat() - 1).pow(2) + 1,
            )
        }

        WeighsAlgorithm.TARGET_LAST -> for (i in 1..quantity) {
            val q = quantity.toFloat()
            val t = i.toFloat()
            raw.add(t / q)
        }

        WeighsAlgorithm.TARGET_ENDS -> for (i in 1..quantity) {
            raw.add(
                4 * (i.toFloat() - (1 + quantity.toFloat()) / 2).pow(2) / (quantity.toFloat() - 1).pow(2),
            )
        }

        WeighsAlgorithm.EQUAL -> repeat(quantity) { raw.add(1.0F) }
    }

    val total = raw.sum()

    return raw
        .map { item ->
            item / total
        }.toMutableList()
}

private fun getPublicKey(
    contextId: Int,
    pkHex: String,
): String = Crypto.naclBoxKeypairFromSecretKey(contextId, pkHex).get().publicKey

@OptIn(ExperimentalStdlibApi::class)
private fun incrementPrivateKey(
    pkHex: String,
    increment: Int,
): String {
    val result =
        BigInteger(pkHex, 16)
            .add(increment.toBigInteger())
            .toString(16)

    return "0".repeat(64 - result.length) + result
}

private fun String.mask(symbols: Int = 4): String =
    (
        this.substring(0, symbols) + "..." +
            this.substring(this.length - symbols, this.length)
    )

private fun appendLog(iteration: Int) {
    val formatter = SimpleDateFormat("yyyy.MM.dd hh:ss")
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = System.currentTimeMillis()

    File(logName).appendText(
        "[${formatter.format(calendar.time)}] " +
            "[$iteration] " +
            "New best match found: " + "%.2f%%".format(bestMatch.score * 100) + "\n" +
            "--------------------------------------------------------------\n" +
            gson.toJson(bestMatch) + "\n\n",
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun runSearch(
    commandChannel: Channel<Command>,
    target: String,
    contextId: Int,
    owners: Array<BigInteger>,
    reqConfirms: Int,
    lifetime: Long,
    processes: Int,
    weighsAlgorithm: WeighsAlgorithm,
) {
    val scope = CoroutineScope(Dispatchers.Default)
    val walletTemplate = SetcodeMultisigTemplate()
    val weighs = getNormalizedWeights(64, weighsAlgorithm)

    var iteration: Int = 0
    var lastResult: String = "0:0000000000000000000000000000000000000000000000000000000000000000"

    val job =
        scope.launch scope@{
            while (true) {
                if (!commandChannel.isEmpty) {
                    when (commandChannel.receive()) {
                        is Pause -> {
                            commandChannel.receive() //  Wait to unpause
                        }

                        is Quit -> {
                            return@scope
                        }

                        else -> {}
                    }
                }

                iteration++
                // colored { print(".".cyan) }
                colored { print("[0:$lastResult]".cyan + "\r") }

                repeat(processes) { i ->
                    launch process@{
                        try {
                            val keys = Credentials.ofRandom(contextId)
                            val deployAddress =
                                walletTemplate
                                    .prepareDeploy(
                                        contextId,
                                        0,
                                        keys,
                                        owners,
                                        reqConfirms,
                                        lifetime,
                                    ).toAddress()

                            var result =
                                deployAddress
                                    .toString()
                                    .split(":")
                                    .last()

                            result = "0".repeat(result.length - 64) + result

                            lastResult = result

                            val (matchScore, matchMask) = assessMatch(target, result, weighs)

                            if (matchScore > bestMatch.score) {
                                bestMatch.address = result
                                bestMatch.score = matchScore
                                bestMatch.credentials = keys
                                bestMatch.mask = matchMask
                                if (balanceAvailable) {
                                    bestMatch.balance =
                                        Account.ofAddress(contextId, deployAddress).balance
                                }

                                println(
                                    "\n[I$iteration] New best match found: " + "%.2f%%".format(matchScore * 100) + "\n" +
                                        gson.toJson(
                                            bestMatch,
                                        ),
                                )

                                appendLog(iteration)

                                if (target == result) {
                                    println("Target found in $iteration iterations!")
                                    exitProcess(0)
                                }
                            }
                        } catch (e: Error) {
                            colored {
                                println("ERROR: $e".red)
                            }
                        }
                    }
                }

                delay(100)
            }
        }

    job.join()
}

private suspend fun getCorrelationPairs(
    startPK: String,
    number: Int,
    contextId: Int,
    owners: Array<BigInteger>,
    reqConfirms: Int,
    lifetime: Long,
) {
    var pk: String = startPK
    var keys: Credentials
    var addr: String
    val walletTemplate = SetcodeMultisigTemplate()
    val file = File(System.currentTimeMillis().toString() + "_addressMiner_corr.log")

    for (i in 0..number) {
        keys = Credentials(getPublicKey(contextId, pk), pk)
        addr =
            walletTemplate
                .prepareDeploy(
                    contextId,
                    0,
                    keys,
                    owners,
                    reqConfirms,
                    lifetime,
                ).toAddress()
                .toString()

        file.appendText("$pk,$addr,${BigInteger(pk, 16)},${BigInteger(addr.split(":").last(), 16)}")

        pk = incrementPrivateKey(pk, 1)

        if (i.mod(100) == 0) {
            colored { print(".") }
        }
    }
}
