package de.jupf.staticonf.example

import de.jupf.staticonf.annotation.Default
import de.jupf.staticonf.StatiConf
import java.util.*

/**
 * This is an example for using StatiConf.
 *
 * @created 07.04.2016
 * @author jpf
 */
fun main(args: Array<String>) {
    println(Config.globalID)
    println(Config.localID)
    println(Config.serviceDiscovery)
    println(Config.ports)

    Config.ports = listOf(5000, 5001, 5555)

    Config.newEntry = "I'm new!"

    println(Config.newEntry)

    Config.statiConf.addType("java.util.UUID", { uuid -> UUID.fromString(uuid) })
}

object Config {
    val statiConf = StatiConf("src/main/resources/config.file",delimiter = "=", docChar = '#', listSeparator = ";")

    var globalID: UUID by statiConf
    var localID: Int by statiConf

    @Default("false") val serviceDiscovery: Boolean by statiConf

    var ports: List<Int> by statiConf.withDefault(arrayListOf(9000))

    var newEntry: String by statiConf
}