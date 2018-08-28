package de.jupf.staticonf

import java.io.File
import kotlin.reflect.KProperty

class DefaultValuePropertyDelegate<V>(private val defaultValue: V, private val statiConf: StatiConf) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): V {
        if (statiConf.entryMap[property.name] == null) {
            return defaultValue
        }

        return statiConf.parseEntryOrList(statiConf.entryMap[property.name]!!, property.returnType)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: V) {
        statiConf.setValue(thisRef, property, value)
    }
}