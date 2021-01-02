plugins {
    java
}

dependencies {
    implementation("io.netty:netty-all:4.1.50.Final")
}

java {
    sourceCompatibility=JavaVersion.VERSION_11
    targetCompatibility=JavaVersion.VERSION_11
}


tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.qsol.reactive.NettyClient"
    }
}
