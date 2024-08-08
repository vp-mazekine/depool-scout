@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.mazekine.depool.scout

import com.fasterxml.jackson.databind.JsonNode
import com.github.mm.coloredconsole.colored
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import network.everscale.BroxusDePoolV1
import network.everscale.BroxusDePoolV2
import network.everscale.DePoolParticipants
import network.everscale.DePoolV1
import network.everscale.DePoolV1_5
import network.everscale.DePoolV2
import network.everscale.DePoolV3
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.appender.ConsoleAppender
import org.apache.logging.log4j.core.config.Configurator
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory
import org.apache.logging.log4j.kotlin.KotlinLogger
import org.apache.logging.log4j.kotlin.logger
import tech.deplant.java4ever.binding.EverSdk
import tech.deplant.java4ever.binding.JsonContext
import tech.deplant.java4ever.binding.Net
import java.io.File
import java.math.BigDecimal
import java.math.BigInteger
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.system.exitProcess

//  Basic settings
const val DEBUG = false
const val DEPOOL_CACHE_FILE = "depoolCache.json"
const val STAKEHOLDER_CACHE_FILE = "stakeholderCache.json"

//  Utilities
val gsonParser: Gson =
    GsonBuilder()
        // .setStrictness(Strictness.LENIENT)
        .setLenient()
        .setPrettyPrinting()
        .create()
val logger = initLogger()

//  Shareable variables
val depoolCodeHashes =
    mapOf(
        "b4ad6c42427a12a65d9a0bffb0c2730dd9cdf830a086d94636dab7784e13eb38" to "DePoolV1",
        "21bb78ae9b56ce930adb89bd1e69bdc3dca40683bade9b9a4dfe265129d21220" to "DePoolV1_5",
        "a46c6872712ec49e481a7f3fc1f42469d8bd6ef3fae906aa5b9927e5a3fb3b6b" to "DePoolV2",
        "14e20e304f53e6da152eb95fffc993dbd28245a775d847eed043f7c78a503885" to "DePoolV3",
        "533adf8a5680849177b9f213f61c48dfd8d730597078670d2367a5eef77251fe" to "BroxusDePoolV1",
        "a21a2e175dc87549280812ecfad50e860d60262fc2d1de6c2230a0108b762934" to "BroxusDePoolV2",
    )
var depools: MutableMap<String, MutableList<String>> = mutableMapOf()

//  Cache
var stakeholderCache: MutableMap<
    //  Stakeholder address
    String,
    //  Depool list cache
    MutableMap<
        //  Code hash
        String,
        //  Depool addresses
        MutableList<String>,
    >,
> = mutableMapOf()

fun main(args: Array<String>) {
    var useDepoolCache = false
    var useStakeholderCache = false
    var stakeholderAddress = ""
    var config: AppConfig? = null

    val usage = """
USAGE:
    java -jar depoolScoutFat.jar [PARAMS]

PARAMS:
    -usc, --use-stakeholder-cache   Use the cached* list of depools filtered by stakeholder (if available).
                                    This cache has a priority vs. ordinary depool cache.
    -udc, --use-depool-cache        Use the list of depools from cache* file (if available).
    -c,  --config           JSON configuration file
    -s,  --stakeholder      Stakeholder to search for. If not specified, full scanning will be performed
    
NOTES:
    *   Cache files are rebuilt during the full depool scanning without stakeholder specified.
        """

    if (args.isEmpty()) {
        println(usage)
        exitProcess(-1)
    }

    var i = 0

    while (i < args.size) {
        when (args[i]) {
            "-udc", "--use-depool-cache" -> {
                useDepoolCache = true
            }

            "-usc", "--use-stakeholder-cache" -> {
                useStakeholderCache = true
            }

            "-c", "--config" -> {
                if (args.lastIndex == i) {
                    logger.error("Configuration file not specified")
                    exitProcess(-1)
                } else {
                    if (!File(args[i + 1]).exists()) {
                        logger.error("Configuration file does not exist")
                        exitProcess(-1)
                    } else {
                        config = gsonParser.fromJson(File(args[i + 1]).readText(), AppConfig::class.java)
                        i++
                    }
                }
            }

            "-s", "--stakeholder" -> {
                if (args.lastIndex == i) {
                    logger.error("Stakeholder address not specified")
                    exitProcess(-1)
                } else {
                    stakeholderAddress = args[i + 1]
                    i++
                }
            }

            else -> {
                logger.error("Unknown configuration parameter ${args[i]}")
                exitProcess(-1)
            }
        }

        i++
    }

    if (config == null) {
        logger.error("Configuration file not specified")
        exitProcess(-1)
    }

    /*for (s in listOf(
        "DePoolV1",
        "DePoolV1_5",
        "DePoolV2",
        "DePoolV3",
        "BroxusDePoolV1",
        "BroxusDePoolV2",
    )) {
        val contract = "contracts/$s"
        ContractWrapper.generate(
            ContractAbi.ofResource("$contract.abi.json").abiContract,
            Tvc.ofResource("$contract.tvc"),
            Path.of("src/main/java"),
            s,
            s,
            "${s}Template",
            "network.everscale",
            "network.everscale",
            false,
            arrayOf<String>(),
        )
    }*/

    EverSdk.load()
    val contextId: Int =
        if (config.network.endpoint.trim() == "") {
            EverSdk.createDefault()
        } else {
            EverSdk.createWithEndpoint(config.network.endpoint)
        }

    loadDepoolsList(contextId, useDepoolCache, useStakeholderCache, stakeholderAddress)

    val fileName =
        "reports/" +
            if (stakeholderAddress == "") {
                "all"
            } else {
                stakeholderAddress.replace(":", "_")
            } +
            "_" + System.currentTimeMillis() + ".tsv"
    File("reports").mkdirs()
    val report = File(fileName)
    report.appendText(
        listOf(
            "depool",
            "stakeholder",
            "ordinary_stake_total",
            "locked_stake_remaining",
            "locked_stake_withdrawal_value",
            "locked_stake_donor",
            "locked_stake_beneficiary",
            "locked_stake_ends_on",
            "vesting_stake_remaining",
            "vesting_stake_withdrawal_value",
            "vesting_stake_donor",
            "vesting_stake_beneficiary",
            "vesting_stake_ends_on",
        ).joinToString("\t") +
            "\n",
    )

    val ds = DecimalFormatSymbols(Locale.US)
    val df = DecimalFormat("##0.00", ds)

    depools.forEach { (hash, addresses) ->
        if (stakeholderAddress == "") {
            logger.info("Analyzing ${depoolCodeHashes[hash]} depools...")
        } else {
            logger.info("Looking for participant $stakeholderAddress in ${depoolCodeHashes[hash]} depools...")
        }

        addresses.forEach { address ->
            analyzeDepool(contextId, hash, address, stakeholderAddress).forEach { v ->
                report.appendText(
                    listOf(
                        v.depool,
                        v.stakeholder,
                        df.format(v.ordinaryStake ?: 0),
                        df.format(v.lockedStake?.remainingAmount ?: 0),
                        df.format(v.lockedStake?.withdrawalAmount ?: 0),
                        v.lockedStake?.owner ?: "",
                        v.lockedStake?.beneficiary ?: "",
                        v.lockedStake?.endsOn,
                        df.format(v.vestingStake?.remainingAmount ?: 0),
                        df.format(v.vestingStake?.withdrawalAmount ?: 0),
                        v.vestingStake?.owner ?: "",
                        v.vestingStake?.beneficiary ?: "",
                        v.vestingStake?.endsOn,
                    ).joinToString("\t") + "\n",
                )
            }
        }
        println()
    }

    logger.info("Report has been saved to $fileName")

    if(stakeholderAddress == "") {
        File(STAKEHOLDER_CACHE_FILE).writeText(
            gsonParser.toJson(stakeholderCache)
        )

        logger.info("Cache files updated")
    }
}

private fun String.mask(symbols: Int = 4): String =
    (
        this.substring(0, symbols) + "..." +
            this.substring(this.length - symbols, this.length)
    )

fun initLogger(): KotlinLogger {
    val configBuilder = ConfigurationBuilderFactory.newConfigurationBuilder()

    configBuilder.let {
        it.setStatusLevel(if (DEBUG) Level.DEBUG else Level.INFO)

        val layout =
            it
                .newLayout("PatternLayout")
                .addAttribute(
                    "pattern",
                    "[%d{yyyy-MM-dd HH:mm:ss}] %highlight{[%level{length=1}]} %c{1.} --> %highlight{%msg%n%throwable}",
                ).addAttribute("disableAnsi", "false")

        //  Console output
        it.add(
            it.newAppender("Console", "Console").add(layout).addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT),
        )

        //  Configure root logger
        it.add(
            it.newRootLogger(if (DEBUG) Level.DEBUG else Level.INFO).let { rl ->
                rl.add(
                    it.newAppenderRef("Console"),
                )
            },
        )
    }

    try {
        Configurator.initialize(configBuilder.build(true))
        println("Logger initialized successfully.")
    } catch (e: Exception) {
        println(
            "Logger crashed while initializing.\n" +
                e.message + "\n" +
                e.stackTrace.joinToString("\n"),
        )
    }

    return logger("MainKt")
}

private fun loadDepoolsList(
    contextId: Int,
    useDepoolCache: Boolean = false,
    useStakeholderCache: Boolean = false,
    stakeholder: String = ""
) {
    val searchDepoolQuery =
        """
        query {
          accounts(
            filter: {
              code_hash: {
                eq: "{1}"
              }
              id: {
                gt: "{2}"
              }
            }
            orderBy: {
                path: "id"
            }
            limit: 100
          ) {
            id
          }
        }
        """.trimIndent()

    if (
        useStakeholderCache &&
        (stakeholder != "") &&
        File(STAKEHOLDER_CACHE_FILE).exists()
    ) {
        try {
            logger.info("Loading depool list from stakeholder cache...")
            val mapType = object : TypeToken<MutableMap<String, MutableMap<String, MutableList<String>>>>() {}.type
            stakeholderCache = gsonParser.fromJson(File(STAKEHOLDER_CACHE_FILE).readText(), mapType)

            logger.info("Cache loaded. Looking for stakeholder...")
            if(!stakeholderCache.containsKey(stakeholder)) {
                logger.warn("Stakeholder not found. Trying other options...")
            } else {
                depools = stakeholderCache[stakeholder]!!
                logger.info("Depool list loaded from cache")
                return
            }
        } catch (e: Exception) {
            logger.error("Error loading depool list from cache. Trying other options...\n" + e.message)
        }
    }

    if (useDepoolCache && File(DEPOOL_CACHE_FILE).exists()) {
        try {
            logger.info("Loading depool list from cache...")
            val mapType = object : TypeToken<MutableMap<String, MutableList<String>>>() {}.type
            depools = gsonParser.fromJson(File(DEPOOL_CACHE_FILE).readText(), mapType)
            logger.info("Depool list loaded from cache")
            return
        } catch (e: Exception) {
            val newFileName = DEPOOL_CACHE_FILE + "." + System.currentTimeMillis() + ".old"
            logger.error("Unable to load depools cache. Renaming to $newFileName...")
            File(DEPOOL_CACHE_FILE).renameTo(File(newFileName))
        }
    }

    depoolCodeHashes.forEach { (hash, label) ->
        logger.info("Looking for $label contracts...")
        val queryVars: JsonNode = JsonContext.SDK_JSON_MAPPER().valueToTree(mutableMapOf<String, String>())
        var lastAddress = ""
        var result =
            gsonParser.fromJson(
                Net
                    .query(contextId, searchDepoolQuery.replace("{1}", hash).replace("{2}", lastAddress), queryVars)
                    .join()
                    .result
                    .toString(),
                DepoolListResponse::class.java,
            )

        while (result.dt.accounts.isNotEmpty()) {
            logger.info("Processing ${result.dt.accounts.size} entries...")

            try {
                result.dt.accounts.forEach { acc ->
                    if (!depools.containsKey(hash)) {
                        depools[hash] = mutableListOf()
                    }

                    depools[hash]!!.add(acc.id)
                }
            } catch (e: Exception) {
                logger.error("Error while processing: " + e.message)
            }

            lastAddress =
                result.dt.accounts
                    .last()
                    .id

            result =
                gsonParser.fromJson(
                    Net
                        .query(contextId, searchDepoolQuery.replace("{1}", hash).replace("{2}", lastAddress), queryVars)
                        .join()
                        .result
                        .toString(),
                    DepoolListResponse::class.java,
                )
        }
    }

    File(DEPOOL_CACHE_FILE).writeText(gsonParser.toJson(depools))
}

private fun analyzeDepool(
    contextId: Int,
    depoolHash: String,
    depoolAddress: String,
    stakeholder: String = "",
): MutableList<DePoolReportEntry> {
    val depool: DePoolParticipants =
        when (depoolCodeHashes[depoolHash]) {
            "DePoolV1" -> DePoolV1(contextId, depoolAddress)
            "DePoolV1_5" -> DePoolV1_5(contextId, depoolAddress)
            "DePoolV2" -> DePoolV2(contextId, depoolAddress)
            "DePoolV3" -> DePoolV3(contextId, depoolAddress)
            "BroxusDePoolV1" -> BroxusDePoolV1(contextId, depoolAddress)
            "BroxusDePoolV2" -> BroxusDePoolV2(contextId, depoolAddress)
            else -> throw IllegalArgumentException("Unknown contract type")
        }

    val participants = depool.participants.get().participants

    val result: MutableList<DePoolReportEntry> = mutableListOf()

    participants.forEach { p ->
        if (stakeholder == "") {
            updateStakeholderCache(p.toString(), depoolHash, depoolAddress)
        }

        var matchFound = false

        //  Getting information about the participant
        val pInfo = depool.getParticipantInfo(p).get()

        //  Calculating ordinary stakes
        val ordinary =
            pInfo
                .stakes
                .values
                .fold(BigInteger.ZERO) { acc, value -> acc + value }
                .toBigDecimal(9)

        //  Calculating locked stakes
        var lockedRemaining = BigDecimal(0).setScale(9)
        var lockedWithdrawal = BigDecimal(0).setScale(9)
        var lockedEndsOn: Long = System.currentTimeMillis() / 1000
        val lockDonor = pInfo.lockDonor.toString()

        pInfo.locks
            .values
            .forEach { stake ->
                val lvs: LockedOrVestedStake =
                    gsonParser.fromJson(gsonParser.toJson(stake), LockedOrVestedStake::class.java)
                lockedRemaining += lvs.remainingAmount.toBigDecimal(9)
                lockedWithdrawal += lvs.withdrawalValue.toBigDecimal(9)
                if ((lvs.withdrawalPeriod + lvs.lastWithdrawalTime) > lockedEndsOn) {
                    lockedEndsOn = lvs.withdrawalPeriod + lvs.lastWithdrawalTime
                }
            }

        //  Calculating vesting stakes
        var vestingRemaining = BigDecimal(0).setScale(9)
        var vestingWithdrawal = BigDecimal(0).setScale(9)
        var vestingEndsOn: Long = System.currentTimeMillis() / 1000
        val vestingDonor = pInfo.vestingDonor.toString()

        pInfo.vestings
            .values
            .forEach { v ->
                val lvs: LockedOrVestedStake =
                    gsonParser.fromJson(gsonParser.toJson(v), LockedOrVestedStake::class.java)
                vestingRemaining += lvs.remainingAmount.toBigDecimal(9)
                vestingWithdrawal += lvs.withdrawalValue.toBigDecimal(9)
                if ((lvs.withdrawalPeriod + lvs.lastWithdrawalTime) > vestingEndsOn) {
                    vestingEndsOn = lvs.withdrawalPeriod + lvs.lastWithdrawalTime
                }
            }

        if (stakeholder.isEmpty() || p.toString() == stakeholder) {
            val entry =
                DePoolReportEntry(
                    depoolAddress,
                    p.toString(),
                    ordinary,
                    if (p.toString() == lockDonor && (lockedRemaining > 0.toBigDecimal())) {
                        LockedOrVestingStakeEntry(
                            lockedRemaining,
                            lockedWithdrawal,
                            LocalDateTime.ofEpochSecond(lockedEndsOn, 0, ZoneOffset.UTC),
                            lockDonor,
                            p.toString(),
                        )
                    } else {
                        null
                    },
                    if (vestingRemaining > 0.toBigDecimal()) {
                        LockedOrVestingStakeEntry(
                            lockedRemaining,
                            lockedWithdrawal,
                            LocalDateTime.ofEpochSecond(vestingEndsOn, 0, ZoneOffset.UTC),
                            vestingDonor,
                            p.toString(),
                        )
                    } else {
                        null
                    },
                )

            result.add(entry)
            colored { print(".".bright.green) }
            matchFound = true
        }

        //  If locked stake belongs to different account, record it there
        if (
            p.toString() != lockDonor &&
            (
                (stakeholder.isNotEmpty() && lockDonor == stakeholder) ||
                    stakeholder.isEmpty()
            ) &&
            lockedRemaining > 0.toBigDecimal()
        ) {
            if (stakeholder == "") {
                updateStakeholderCache(lockDonor, depoolHash, depoolAddress)
            }

            result.add(
                DePoolReportEntry(
                    depoolAddress,
                    lockDonor,
                    lockedStake =
                        LockedOrVestingStakeEntry(
                            lockedRemaining,
                            lockedWithdrawal,
                            LocalDateTime.ofEpochSecond(lockedEndsOn, 0, ZoneOffset.UTC),
                            lockDonor,
                            p.toString(),
                        ),
                ),
            )
            colored { print(".".bright.yellow) }
            matchFound = true
        }

        if(!matchFound) print(".")
    }

    return result
}

private fun updateStakeholderCache(
    stakeholder: String,
    depoolHash: String,
    depoolAddress: String,
) {
    //  If there is no such stakeholder in cache yet
    if (!stakeholderCache.containsKey(stakeholder)) {
        stakeholderCache[stakeholder] = mutableMapOf(depoolHash to mutableListOf(depoolAddress))
        return
    }

    val stakeholderCacheEntry = stakeholderCache[stakeholder]!!

    //  If there is no such depool hash for this stakeholder yet
    if (!stakeholderCacheEntry.containsKey(depoolHash)) {
        stakeholderCache[stakeholder]!![depoolHash] = mutableListOf(depoolAddress)
        return
    }

    val depoolList = stakeholderCacheEntry[depoolHash]!!

    //  If there is no such depool for this hash yet
    if (!depoolList.contains(depoolAddress)) {
        stakeholderCache[stakeholder]!![depoolHash]!!.add(depoolAddress)
    }

    //  Cache is up-to-date
}
