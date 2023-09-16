plugins {
    kotlin("jvm") version "1.9.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.5.1"
    jacoco
}

jacoco {
    toolVersion = "0.8.8"
}

group = "org.livid"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val kotestVersion = "5.7.2"

dependencies {
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation(kotlin("test"))
}

tasks.test {
    extensions.configure(JacocoTaskExtension::class) {
        destinationFile = file("$buildDir/jacoco/jacoco.exec")
    }

    finalizedBy("jacocoTestReport")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)
    }

    finalizedBy("jacocoTestCoverageVerification")
}

tasks.jacocoTestCoverageVerification {
//    violationRules {
//        rule {
//            element = "CLASS"
//            limit {
//                minimum = "0.50".toBigDecimal()
//            }
//        }
//    }
}

kotlin {
    jvmToolchain(8)
}

val testCoverage by tasks.registering {
    group = "verification"
    description = "Runs the unit tests with coverage"

    dependsOn(
        ":test",
        ":jacocoTestReport",
        ":jacocoTestCoverageVerification"
    )

    tasks["jacocoTestReport"].mustRunAfter(tasks["test"])
    tasks["jacocoTestCoverageVerification"].mustRunAfter(tasks["jacocoTestReport"])
}
