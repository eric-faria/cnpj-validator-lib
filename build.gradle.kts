plugins {
    kotlin("multiplatform") version "2.3.20"
    `maven-publish`
}

group = "com.ericfaria.lib"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)

    jvm()

    js(IR) {
        outputModuleName.set("@eric-faria/cnpj-validator-lib")

        nodejs()
        generateTypeScriptDefinitions()
        binaries.library()
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/eric-faria/cnpj-validator-lib")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
