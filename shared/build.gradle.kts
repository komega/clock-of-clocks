/*
 * Copyright 2026 Khoa Omega
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.android.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvmToolchain(21)

    android {
        namespace = "io.github.komega.clockofclocks.shared"
        minSdk = 26
        compileSdk {
            version = release(37)
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.ui)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.kotlinx.datetime)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
}

compose.desktop {
    application {
        mainClass = "io.github.komega.clockofclocks.shared.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Deb, TargetFormat.Dmg, TargetFormat.Msi)
            packageName = "Clock Of Clocks"
            packageVersion = "1.0.0"
            copyright = "© 2026 Khoa Omega"

            windows {
                menu = true
                shortcut = true
                dirChooser = true
                upgradeUuid = "77678903-8903-4776-8903-776789034776"
                iconFile = file("src/jvmMain/resources/clock.ico")
            }

            macOS {
                bundleID = "io.github.komega.clockofclocks"
                dockName = "Clock Of Clocks"
                iconFile = file("src/jvmMain/resources/clock.icns")
            }

            linux {
                shortcut = true
                iconFile = file("src/jvmMain/resources/clock.png")
            }
        }
    }
}
