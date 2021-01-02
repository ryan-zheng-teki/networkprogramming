
dependencies {
    implementation("io.netty:netty-all:4.1.50.Final")
}

jar {
    manifest {
        attributes(
                'Main-Class': 'com.qsol.nettyclient.NettyClient'
        )
    }
    from {
        configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }
    }
}