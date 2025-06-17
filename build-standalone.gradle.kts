// Completely self-contained build script for CopilotPromptsForIntelliJ
// This build uses only bundled dependencies and requires no internet access

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.10"
}

group = "com.github.copilot.prompts"
version = "1.0.0"

repositories {
    // Only use local repositories - no internet required
    flatDir {
        dirs("libs")
    }
    // Fallback to system JDK libraries
    flatDir {
        dirs("${System.getProperty("java.home")}/lib")
    }
}

dependencies {
    // Use bundled Kotlin standard library from libs directory
    implementation(files("libs/kotlin-stdlib-1.9.10.jar"))
    implementation(files("libs/kotlin-stdlib-common-1.9.10.jar"))
    implementation(files("libs/annotations-13.0.jar"))
    
    // No external dependencies - everything is self-contained
}

// Configure source sets
sourceSets {
    main {
        kotlin {
            srcDirs("src/main/kotlin")
        }
        resources {
            srcDirs("src/main/resources")
        }
    }
    test {
        kotlin {
            srcDirs("src/test/kotlin")
        }
    }
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    
    // Create a fat JAR with all dependencies included
    register<Jar>("fatJar") {
        archiveBaseName.set("copilot-prompts-standalone")
        archiveClassifier.set("all")
        
        from(sourceSets.main.get().output)
        
        // Include all runtime dependencies
        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get().filter { 
                it.name.endsWith("jar") 
            }.map { zipTree(it) }
        })
        
        // Include bundled libraries
        from(zipTree("libs/kotlin-stdlib-1.9.10.jar"))
        from(zipTree("libs/kotlin-stdlib-common-1.9.10.jar"))
        from(zipTree("libs/annotations-13.0.jar"))
        
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        
        manifest {
            attributes(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Main-Class" to "com.github.copilot.prompts.OfflineMain"
            )
        }
    }
    
    // Create IntelliJ plugin JAR with all dependencies
    register<Jar>("pluginJar") {
        archiveBaseName.set("copilot-prompts-plugin")
        archiveClassifier.set("plugin")
        
        from(sourceSets.main.get().output)
        
        // Include bundled libraries
        from(zipTree("libs/kotlin-stdlib-1.9.10.jar"))
        from(zipTree("libs/kotlin-stdlib-common-1.9.10.jar"))
        from(zipTree("libs/annotations-13.0.jar"))
        
        // Include plugin.xml and other resources
        from("src/main/resources") {
            include("**/*")
        }
        
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        
        manifest {
            attributes(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Plugin-Class" to "com.github.copilot.prompts.CopilotPromptsPlugin"
            )
        }
    }
    
    // Task to verify all dependencies are bundled
    register("verifyOfflineDeps") {
        description = "Verify all dependencies are available offline"
        group = "verification"
        
        doLast {
            val libsDir = file("libs")
            val requiredJars = listOf(
                "kotlin-stdlib-1.9.10.jar",
                "kotlin-stdlib-common-1.9.10.jar",
                "annotations-13.0.jar"
            )
            
            println("Verifying offline dependencies...")
            var allPresent = true
            
            requiredJars.forEach { jar ->
                val jarFile = File(libsDir, jar)
                if (jarFile.exists()) {
                    println("✓ Found: $jar (${jarFile.length()} bytes)")
                } else {
                    println("✗ Missing: $jar")
                    allPresent = false
                }
            }
            
            if (allPresent) {
                println("✓ All required dependencies are bundled!")
            } else {
                throw GradleException("Some dependencies are missing. Run 'gradle prepareOfflineDeps' first.")
            }
        }
    }
    
    // Task to create complete offline package
    register<Zip>("createOfflinePackage") {
        description = "Create complete offline package with all dependencies"
        group = "distribution"
        
        dependsOn("fatJar", "pluginJar")
        
        archiveBaseName.set("copilot-prompts-offline")
        archiveVersion.set(project.version.toString())
        
        from(".") {
            include("src/**")
            include("libs/**")
            include("gradle/**")
            include("gradlew*")
            include("build-standalone.gradle.kts")
            include("*.md")
            include("*.properties")
        }
        
        from("build/libs") {
            include("*-all.jar")
            include("*-plugin.jar")
            into("dist")
        }
        
        doLast {
            println("Created offline package: ${archiveFile.get().asFile}")
            println("This package contains everything needed to build and run the plugin offline.")
        }
    }
    
    // Enhanced build task
    build {
        dependsOn("verifyOfflineDeps", "fatJar", "pluginJar")
        
        doLast {
            println("\n" + "=".repeat(70))
            println("SELF-CONTAINED BUILD COMPLETED SUCCESSFULLY!")
            println("=".repeat(70))
            println("Standalone JAR: build/libs/copilot-prompts-standalone-all-${project.version}.jar")
            println("Plugin JAR:     build/libs/copilot-prompts-plugin-plugin-${project.version}.jar")
            println("Offline Package: build/distributions/copilot-prompts-offline-${project.version}.zip")
            println("\nAll dependencies are bundled - no internet required!")
            println("=".repeat(70))
        }
    }
    
    // Task to run the standalone version
    register<JavaExec>("runStandalone") {
        description = "Run the standalone version with bundled dependencies"
        group = "application"
        
        dependsOn("fatJar")
        
        classpath = files("build/libs/copilot-prompts-standalone-all-${project.version}.jar")
        mainClass.set("com.github.copilot.prompts.OfflineMain")
        
        doFirst {
            println("Running standalone version with bundled dependencies...")
        }
    }
}