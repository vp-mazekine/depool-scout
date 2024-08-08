# DePool Scout

[![JDK version](https://img.shields.io/badge/Java-22-green.svg)](https://shields.io/)
![GitHub License](https://img.shields.io/github/license/vp-mazekine/depool-scout)


**DePool Scout** helps to find all stakes (ordinary, locked and vesting) made by a specific stakeholder to Everscale DePools.

It operates as a console utility loading data from GraphQL via [Java4Ever Framework](https://github.com/deplant/java4ever-framework) by [Deplant](https://github.com/deplant/) team.

## Features
* Support of multiple DePool versions (v1, v1*, v2, v3, Broxus v1, Broxus v2)
* Search by stakeholder
* Compounded report of all stakes in all depools
* Caching for speeding up search

## Prerequisites
* [Java 22](https://adoptium.net/temurin/releases/?version=22)

## How to Use

Run the following command in console:

```shell
$> java -jar --enable-native-access=ALL-UNNAMED depoolScoutFat.jar [PARAMS]
```

### Parameters

| Parameter                             | Obligatory | Description                                                                                                                      |
|:--------------------------------------|:-----------|:---------------------------------------------------------------------------------------------------------------------------------|
| `-c`<br/>`--config` | Obligatory | JSON configuration file. See [example file](src/main/resources/testConfig.json).                                                 |
| `-usc`<br/>`--use-stakeholder-cache` | *Optional* | Use the cached* list of depools filtered by stakeholder (if available).<br/>This cache has a priority vs. ordinary depool cache. |
| `-udc`<br/>`--use-depool-cache` | *Optional* | Use the list of depools from cache* file (if available).                                                                         |
| `-s`<br/>`--stakeholder` | *Optional* |  Stakeholder to search for. If not specified, full scanning will be performed                                                    |

**Note**: Cache files are rebuilt during the full depool scanning without stakeholder specified.

### Report files

The utility saves report files to the `reports` folder in its running location.

### Caching

Cache files are stored in the running location:
* `depoolCache.json` stores the list of depools categorized per their code hash.
  * To activate the usage of depools cache instead of searching them each time via GraphQL, use the `-udc` parameter.
  * To update the cache, simply run the utility without using cache.
* `stakeholderCache.json` stores the mapping of depools per stakeholders for faster access in the future.
  * To speed up search by stakeholder, use the `-usc` parameter.
  * To rebuild the cache, run the full scanning of depools by omitting the target stakeholder address.

## License

The utility is licensed under [GPL 3.0 License](LICENSE).

## Bug reporting and feature requests  

Please use GitHub Issues to report a bug or request a feature.