package de.jupf.staticonf

import de.jupf.staticonf.annotation.Default
import java.io.File
import java.util.*
import kotlin.reflect.KProperty
import kotlin.reflect.KType

/**
 * This class represents a config file. Properties can be delegated to it, to back the properties by the config file.
 *
 * @created 07.04.2016
 * @author jpf
 */
class StatiConf(val path: String, val docChar: Char = '#', delimiter: String = "=", val listSeparator: String = ";",
                val parser: Parser = BasicTypeStringParser(), val splitter: Splitter = DefaultSplitter(delimiter)) {
    private val entryMap: MutableMap<String, String> = LinkedHashMap()
    private var fileContent: MutableList<String> = ArrayList()

    init {
        readFileEntries()
    }

    private fun readFileEntries() {
        val bufferedReader = File(path).bufferedReader()
        bufferedReader.useLines { block ->
            entryMap.putAll(block.associate {
                fileContent.add(it)
                if (it.startsWith('#') || it.length == 0)
                    Pair("#", "#")
                else
                    splitter.split(it)
            })
        }
    }

    private fun updateMap() {
        entryMap.clear()
        fileContent.forEach { line ->
            entryMap.putAll( line.associate {
                if (line.startsWith('#') || line.length == 0)
                    Pair("#", "#")
                else
                    splitter.split(line)
            })
        }
    }


    operator fun <V> getValue(thisRef: Any?, property: KProperty<*>): V {
        var defaultValue: String? = null
        property.annotations.forEach {
            if (it is Default)
                defaultValue = it.value
        }

        if (entryMap[property.name] == null) {
            if (defaultValue != null)
                return parseEntryOrList(defaultValue!!, property.returnType)

            throw IllegalArgumentException("There is no entry in the config file with such a name.")
        }

        return parseEntryOrList(entryMap[property.name]!!, property.returnType)
    }

    private fun <V> parseEntryOrList(entryValue: String, returnType: KType): V {
        if (returnType.toString().startsWith("kotlin.collections.List"))
            return parser.parseEntryList(entryValue, returnType.toString(), listSeparator)
        return parser.parseEntry(entryValue, returnType.toString())
    }

    operator fun <V> setValue(thisRef: Any?, property: KProperty<*>, value: V) {
        var lineChanged = false
        fileContent = fileContent.map { line ->
            if (!line.startsWith('#') && line.length != 0
                    && splitter.split(line).first.equals(property.name)) {
                lineChanged = true
                createConfigLine(property, value)
            } else
                line
        }.toMutableList()
        if (!lineChanged)
            fileContent.add(createConfigLine(property, value))

        File(path).bufferedWriter().use {
            it.write(fileContent.joinToString (separator = "\n", transform = { it }))
            it.flush()
        }

        updateMap()
    }

    private fun <V> createConfigLine(property: KProperty<*>, value: V): String {
        if (property.returnType.toString().startsWith("kotlin.collections.List"))
            return "${property.name} = ${value.toString().substring(1..value.toString().length - 2).replace(",", " $listSeparator ")}"
        return "${property.name} = ${value.toString()}"
    }

    fun addType(typeName: String, parse: (String) -> Any) {
        parser.addType(typeName, parse)
    }
}