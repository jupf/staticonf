package de.jupf.staticonf

/**
 * This file includes the Splitter interface (for splitting a config entry)
 * and the standard splitter implementation which splits a config entry at a given char.
 *
 * @created 07.04.2016
 * @author jpf
 */
interface Splitter {
    fun split(entryString: String): Pair<String,String>
}


class DefaultSplitter(val splitString: String) : Splitter {
    override fun split(entryString: String): Pair<String,String> {
        val splittedEntry = entryString.split(splitString)
        return Pair(splittedEntry[0].trim(), splittedEntry[1].trim())
    }

    override fun toString(): String{
        return "DefaultSplitter(splitString='$splitString')"
    }
}

