package com.squareup.wire.gradle

import groovy.transform.CompileStatic

@CompileStatic
class ClassPaths {
  static List<File> plugin() {
    ClassPaths.class
        .classLoader
        .getResourceAsStream("plugin-classpath.txt")
        .readLines()
        .collect { new File(it) }
  }

  static String string(List<File> classpath) {
    classpath
        .collect { it.absolutePath.replace('\\', '\\\\') } // escape backslashes in Windows paths
        .collect { "'$it'" }
        .join(", ")
  }
}
