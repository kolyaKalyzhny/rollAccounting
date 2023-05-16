import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
//    kotlin("kapt") version "1.8.0"
//    kotlin("jvm") version "1.8.20"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}
//dependencies {
//    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
//}

kotlin {
    jvm {
        jvmToolchain(11)
//        jvmToolchain(19)
        withJava()

//        compilations.all {
//            kotlinOptions {
//                jvmTarget = "19"
//            }
//        }
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("com.fazecast:jSerialComm:[2.0.0,3.0.0)")
                implementation("org.jetbrains.compose.material:material-icons-extended-desktop:1.3.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.4")
                implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.0")

                implementation("org.tinylog:tinylog-api:2.3.2")
                runtimeOnly("org.tinylog:tinylog-impl:2.3.2")

                // OKHttp
                implementation("com.squareup.okhttp3:okhttp:4.10.0")

                // GSON
                implementation("com.google.code.gson:gson:2.10.1")

                // ZXing
                implementation("com.google.zxing:core:3.4.1")
                implementation("com.google.zxing:javase:3.4.1")

                // Kotlin-Logging

                // DI
                implementation("io.insert-koin:koin-core:3.4.0")
//                kapt("com.google.dagger:dagger-compiler:2.x")

            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.mockk:mockk:1.12.0")
//                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0-RC3")

                // DI
                implementation("io.insert-koin:koin-test:3.4.0")
            }

        }

    }
    jvmToolchain(11)
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "RollAccounting"
            packageVersion = "1.0.0"
        }
    }
}
//dependencies {
//    implementation(kotlin("stdlib-jdk8"))
//}