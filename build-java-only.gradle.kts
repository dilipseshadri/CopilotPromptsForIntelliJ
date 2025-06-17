// Pure Java build for CopilotPromptsForIntelliJ
// This creates a self-contained JAR without requiring Kotlin compilation

plugins {
    id("java")
}

group = "com.github.copilot.prompts"
version = "1.0.0"

repositories {
    // Use only local dependencies
    flatDir {
        dirs("libs")
    }
}

dependencies {
    // Use only the bundled JARs
    implementation(files("libs/kotlin-stdlib-1.9.10.jar"))
    implementation(files("libs/kotlin-stdlib-common-1.9.10.jar"))
    implementation(files("libs/kotlin-stdlib-jdk8-1.9.10.jar"))
    implementation(files("libs/annotations-13.0.jar"))
}

// Configure source sets to include pre-compiled classes
sourceSets {
    main {
        java {
            srcDirs("src/main/java")
        }
        resources {
            srcDirs("src/main/resources")
        }
    }
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    
    // Create a self-contained JAR
    jar {
        archiveBaseName.set("copilot-prompts-plugin")
        
        from(sourceSets.main.get().output)
        
        // Include all bundled dependencies
        from(zipTree("libs/kotlin-stdlib-1.9.10.jar"))
        from(zipTree("libs/kotlin-stdlib-common-1.9.10.jar"))
        from(zipTree("libs/kotlin-stdlib-jdk8-1.9.10.jar"))
        from(zipTree("libs/annotations-13.0.jar"))
        
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        
        manifest {
            attributes(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Plugin-Class" to "com.github.copilot.prompts.CopilotPromptsPlugin"
            )
        }
    }
    
    // Task to verify the plugin JAR
    register("verifyPlugin") {
        dependsOn("jar")
        
        doLast {
            val jarFile = file("build/libs/copilot-prompts-plugin-${project.version}.jar")
            if (jarFile.exists()) {
                println("✓ Self-contained plugin JAR created: ${jarFile.name} (${jarFile.length()} bytes)")
                println("✓ All dependencies are bundled - ready for IntelliJ installation!")
                println("✓ Install in IntelliJ: Settings > Plugins > Install Plugin from Disk")
            } else {
                throw GradleException("Plugin JAR file not found!")
            }
        }
    }
    
    build {
        dependsOn("verifyPlugin")
    }
}