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
}

object Config {
    var delegateConf = StatiConf("src/main/resources/config.file")

    var globalID: UUID by delegateConf
    var localID: Int by delegateConf

    @Default("false") val serviceDiscovery: Boolean by delegateConf

    var ports: List<Int> by delegateConf

    var newEntry: String by delegateConf
}