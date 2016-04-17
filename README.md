# StatiConf
[![Kotlin](https://img.shields.io/badge/Kotlin-1.0.1-blue.svg?style=flat) ](https://kotlinlang.org/)[![license](https://img.shields.io/badge/license-MIT-blue.svg?style=flat) ](LICENSE)[ ![Download](https://api.bintray.com/packages/jupf/maven/StatiConf/images/download.svg) ](https://bintray.com/jupf/maven/StatiConf/_latestVersion)  
This is a statically typed library for handling configuration files through properties in [Kotlin](https://kotlinlang.org).  
With StatiConf you can back a Kotlin property by a configuration file without taking care of any string parsing or stream handling.   

## Getting Started
The source/target compatibility is Java 1.6.  
__The Kotlin Reflect API is needed to be present for this library.__  
This library is uploaded to jCenter and Maven Central.  
Here is also an [example](src/main/kotlin/de/jupf/staticonf/example/example.kt) to look at.

### Gradle
```gradle
dependencies {
    compile 'io.github.jupf.staticonf:staticonf:1.0.0'
}
```

### Maven
```xml
<dependency>
  <groupId>io.github.jupf.staticonf</groupId>
  <artifactId>staticonf</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

## Loading from a Configuration File
To load a configuration file, first you need to construct a StatiConf object from it:  
```Kotlin
val statiConf = StatiConf("config.file")
```
Then you can delegate properties to it:
```Kotlin
val ports: List<Short> by statiConf
val localID: Int by statiConf
```
Now you can use this properties. They have the values parsed from the config.file.  
It is possible to use the `Kotlin.collections.List`. The standard separator in the config file is a `;`

## Saving to a Configuration File
To save to a configuration file, you can just use non-final properties:
```Kotlin
val statiConf = StatiConf("config.file")
var localID: Int by statiConf

...

localID = 2048 // This will change the line in the config file to the set value.
```
If the property does not exist in the config file a new line will be appended to the end with the set value.  

## Types in StatiConf
Out of the box, the following property types can be used for automatic parsing:
* All Kotlin basic types (e.g. Int, Float, Boolean, ...)
* java.util.UUID

### Adding new types
You can easily add new types to the parser. You just need a function that parses a string to the desired object:
```Kotlin
val statiConf = StatiConf("config.file")
statiConf.addType("java.util.UUID", { uuid -> UUID.fromString(uuid) })
```
If you save such a type to a property file the toString method is called to parse it into a string.  
With other words, your parsing function has to play nicely with the toString representation of the type!  

## Example Config File
```shell
# this is a UUID hex string
globalID = 38400000-8cf0-11bd-b23e-10b96e4ef00d

# local identifier
localID = 31337

# ports to connect to
ports = 5000 ;  5001 ;  5005

# decides if the service discovery is enabled
serviceDiscovery = true

```
