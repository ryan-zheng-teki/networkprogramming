plugins {
    java
}

dependencies {
    implementation("io.projectreactor:reactor-core:3.3.8.RELEASE")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.qsol.reactive.ReactiveTutorial"
    }
}
