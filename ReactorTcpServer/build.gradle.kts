/*
 * This file was generated by the Gradle 'init' task.
 */
plugins {
    java
}

dependencies {
    implementation("io.projectreactor.netty:reactor-netty:0.8.0.RELEASE")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.qsol.reactive.ReactorNettyServer"
    }
}
