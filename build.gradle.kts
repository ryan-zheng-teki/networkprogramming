/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    java
    `maven-publish`
}

allprojects {
    group = "org.example"
    version = "1.0-SNAPSHOT"
}


subprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }
}