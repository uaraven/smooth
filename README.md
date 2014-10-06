# Smooth #

v 0.1

Smooth is a lightweight library for when you don't need full guava. 
Our use case for it are Android applications. Given Android's limitation of number of methods in an APK 

### Features ###
It only have a handful of features:
  
  * Collection utilities, like static map/filter methods and factories.
  * Rich Iterable wrapper allowing functional treatment of iterables (filter, map, foreach, find, reduce).
  * Functional interfaces.
  * Scala-like Future with chaining and callbacks.
  * Option, Either and Try classes for safe programming.

### Using ###


####Maven####

```
#!xml

<dependencies>
...
  <dependency>
    <groupId>net.ninjacat</groupId>
    <artifactId>smooth</artifactId>
    <version>0.1</version>
  </dependency>
...
</dependencies>
```

#### Gradle ####

```
#!groovy

dependencies {
...
   compile group: 'net.ninjacat', name: 'smooth', version: '0.1'
...
}
```

