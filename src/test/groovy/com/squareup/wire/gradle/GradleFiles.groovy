package com.squareup.wire.gradle

import groovy.transform.CompileStatic

@CompileStatic
class GradleFiles {
  static final RUNTIME = "com.squareup.wire:wire-runtime:2.1.1"

  static String emptyJava(String classpathString) {
    """
buildscript {
  dependencies {
    classpath files(${classpathString})
  }
}

apply plugin: 'java'
apply plugin: 'com.squareup.wire'
    """
  }

  static String javaWithRuntime(String classpathString) {
    """
buildscript {
  dependencies {
    classpath files(${classpathString})
  }
}

apply plugin: 'java'
apply plugin: 'com.squareup.wire'

repositories {
  jcenter()
}

dependencies {
  compile '$RUNTIME'
}
    """
  }

  static String androidWithRuntime(String classpathString) {
    """
buildscript {
  repositories {
    jcenter()
  }

  dependencies {
    classpath "com.android.tools.build:gradle:1.5.0"
    classpath files(${classpathString})
  }
}

apply plugin: 'com.android.application'
apply plugin: 'com.squareup.wire'

android {
  compileSdkVersion 23
  buildToolsVersion "23.0.2"
}

repositories {
  jcenter()
}

dependencies {
  compile '$RUNTIME'
}
    """
  }

  static String androidWithSupportAnnotations(String classpathString) {
    """
buildscript {
  repositories {
    jcenter()
  }

  dependencies {
    classpath "com.android.tools.build:gradle:1.5.0"
    classpath files(${classpathString})
  }
}

apply plugin: 'com.android.application'
apply plugin: 'com.squareup.wire'

android {
  compileSdkVersion 23
  buildToolsVersion "23.0.2"

  sourceSets.main.wire.android = true
}

repositories {
  jcenter()
}

dependencies {
  compile '$RUNTIME'
  provided 'com.android.support:support-annotations:23.2.0'
}
    """
  }
}
