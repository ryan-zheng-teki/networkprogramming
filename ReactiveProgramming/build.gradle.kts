dependencies {
    implementation group: 'junit', name: 'junit', version: '4.12'
    implementation group: 'io.projectreactor', name: 'reactor-core', version: '3.3.8.RELEASE'
}

jar {
    manifest {
        attributes(
                "Main-Class": "com.qsol.reactive.ReactiveTutorial"
        )
    }
}