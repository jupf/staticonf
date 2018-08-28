package de.jupf.staticonf

import java.util.*

/**
 * This file includes the Parser interface for parsing a config file entry value. Also it implements the
 * default parser which stores functions to convert entry values from strings to desired object types
 *
 * @created 07.04.2016
 * @author jpf
 */
interface Parser {
    fun <R> parseEntry(configValue: String, returnType: String): R
    fun <R> parseEntryList(configValueList: String, returnType: String, listSeparator: String): R
    fun addType(typeName: String, parse: (String) -> Any)
}

@Suppress("UNCHECKED_CAST")
class BasicTypeStringParser : Parser {
    private val parserMap: MutableMap<String, (String) -> Any> = LinkedHashMap()

    init {
        parserMap.put("kotlin.String", { s: String -> s })
        parserMap.put("kotlin.Byte", { s: String -> s.toByte() })
        parserMap.put("kotlin.Short", { s: String -> s.toShort() })
        parserMap.put("kotlin.Int", { s: String -> s.toInt() })
        parserMap.put("kotlin.Long", { s: String -> s.toLong() })
        parserMap.put("kotlin.Float", { s: String -> s.toFloat() })
        parserMap.put("kotlin.Double", { s: String -> s.toDouble() })
        parserMap.put("kotlin.Boolean", { s: String -> s.toBoolean() })
        parserMap.put("java.util.UUID", { s: String -> UUID.fromString(s) })
    }

    override fun <R> parseEntry(configValue: String, returnType: String): R {
        return parserMap[returnType]?.invoke(configValue) as R
                ?: throw IllegalArgumentException("The delegated property has an unsupported type.")
    }

    override fun <R> parseEntryList(configValueList: String, returnType: String, listSeparator: String): R {
        val listType = returnType.substring(returnType.indexOf('<')+1..returnType.length - 2)
        val entryList: MutableList<Any> = ArrayList()
        configValueList.split(listSeparator).forEach {
            entryList.add(parseEntry(it.trim(),listType))
        }

        return entryList as R
    }

    override fun addType(typeName: String, parse: (String) -> Any) {
        parserMap[typeName] = parse
    }
}