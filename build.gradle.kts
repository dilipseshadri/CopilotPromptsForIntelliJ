plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.10"
    id("org.jetbrains.intellij") version "1.17.2"
}

group = "com.github.copilot.prompts"
version = "1.0.0"

repositories {
    // Support both online and offline modes
    mavenLocal() // Check local cache first
    mavenCentral()
    gradlePluginPortal()
    
    // Fallback for offline mode
    flatDir {
        dirs("libs")
    }
}

dependencies {
    // Minimal dependencies for compilation
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    
    // Test dependencies
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

intellij {
    version.set("2023.2")
    type.set("IC") // IntelliJ IDEA Community Edition
    
    // Download sources for offline development
    downloadSources.set(true)
    
    plugins.set(listOf(
        // Note: GitHub Copilot plugin dependency will be optional
        // Users need to have it installed for full functionality
    ))
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("241.*")
        
        // Add offline compatibility note
        changeNotes.set("""
            <h3>Version 1.0.0</h3>
            <ul>
                <li>Complete prompt template system for GitHub Copilot</li>
                <li>Offline compatibility with fallback implementations</li>
                <li>Support for custom prompt categories and templates</li>
                <li>Quick access popup and tool window</li>
                <li>Settings panel for customization</li>
            </ul>
            
            <h4>Offline Mode Support</h4>
            <p>This plugin can work without internet access using fallback implementations.
            See OFFLINE_INSTALLATION.md for details.</p>
        """.trimIndent())
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
    
    // Custom task to prepare offline dependencies
    register("prepareOfflineDeps") {
        description = "Downloads and caches dependencies for offline use"
        group = "build setup"
        
        doLast {
            println("Preparing offline dependencies...")
            
            // Create libs directory
            val libsDir = file("libs")
            if (!libsDir.exists()) {
                libsDir.mkdirs()
            }
            
            // Copy runtime dependencies to libs directory
            try {
                configurations.runtimeClasspath.get().forEach { file ->
                    if (file.exists() && file.name.endsWith(".jar")) {
                        val targetFile = File(libsDir, file.name)
                        if (!targetFile.exists()) {
                            file.copyTo(targetFile)
                            println("Cached: ${file.name}")
                        }
                    }
                }
                println("Offline dependencies prepared in libs/ directory")
            } catch (e: Exception) {
                println("Warning: Could not cache all dependencies - ${e.message}")
            }
        }
    }
    
    // Task to test offline functionality
    register<JavaExec>("runOfflineDemo") {
        description = "Run the offline demo application"
        group = "application"
        
        dependsOn("classes")
        classpath = sourceSets.main.get().runtimeClasspath
        mainClass.set("com.github.copilot.prompts.OfflineMain")
        
        doFirst {
            println("Starting offline demo...")
            println("This demonstrates the plugin functionality without IntelliJ IDEA")
        }
    }
    
    // Enhanced build task that handles offline scenarios
    build {
        doLast {
            println("\n" + "=".repeat(60))
            println("Build completed successfully!")
            println("=".repeat(60))
            println("Plugin JAR: build/libs/${project.name}-${project.version}.jar")
            println("Distribution: build/distributions/")
            println("\nFor offline installation, see: OFFLINE_INSTALLATION.md")
            println("To test offline: ./gradlew runOfflineDemo")
            println("=".repeat(60))
        }
    }
}