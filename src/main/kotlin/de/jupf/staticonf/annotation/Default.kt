package de.jupf.staticonf.annotation

/**
 * This annotation is for defining default values if a config entry is not found.
 *
 * @created 07.04.2016
 * @author jpf
 */
@Target(AnnotationTarget.PROPERTY)
annotation class Default(val value: String)