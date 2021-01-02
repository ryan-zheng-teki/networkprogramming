plugins {
    java
}

dependencies {
    implementation("io.netty:netty-all:4.1.50.Final")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.qsol.reactive.NettyServer"
    }
}
