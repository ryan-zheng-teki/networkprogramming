plugins {
    java
}

dependencies {
    implementation("io.projectreactor:reactor-core:3.3.8.RELEASE")
    // https://mvnrepository.com/artifact/org.springframework/spring-core
    implementation("org.springframework:spring-core:5.3.3")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.qsol.reactive.ReactiveTutorial"
    }
}
