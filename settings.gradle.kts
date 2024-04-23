import java.io.FileInputStream
import java.util.Properties

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            val githubProperties = Properties()
            githubProperties.load(FileInputStream(File("github.properties")))
            val gprUser = githubProperties.getProperty("USER")
            val token = githubProperties.getProperty("TOKEN")
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/paystore/pix-sdk")

            credentials {
                username = gprUser
                password = token
            }
        }

    }
}

rootProject.name = "app-smart-demo-pix"
include(":app")