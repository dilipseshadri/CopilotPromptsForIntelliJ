// Offline-compatible build script for CopilotPromptsForIntelliJ
// Use this when internet access is not available

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.10"
}

group = "com.github.copilot.prompts"
version = "1.0.0"

repositories {
    // Only use local repositories for offline builds
    mavenLocal()
    flatDir {
        dirs("libs", "gradle/libs")
    }
}

dependencies {
    // Minimal dependencies for offline compilation
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation(files("${System.getProperty("java.home")}/../lib/tools.jar"))
    
    // Test dependencies
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    
    // Create a simple JAR without IntelliJ plugin packaging
    jar {
        archiveBaseName.set("copilot-prompts-offline")
        from(sourceSets.main.get().output)
        
        manifest {
            attributes(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Main-Class" to "com.github.copilot.prompts.OfflineMain"
            )
        }
    }
    
    // Task to create a standalone JAR with all dependencies
    register<Jar>("fatJar") {
        archiveBaseName.set("copilot-prompts-standalone")
        from(sourceSets.main.get().output)
        
        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
        })
        
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        
        manifest {
            attributes(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Main-Class" to "com.github.copilot.prompts.OfflineMain"
            )
        }
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
        }
    }
}

// Add source sets for fallback implementations
sourceSets {
    main {
        kotlin {
            srcDirs("src/main/kotlin")
        }
        resources {
            srcDirs("src/main/resources")
        }
    }
}