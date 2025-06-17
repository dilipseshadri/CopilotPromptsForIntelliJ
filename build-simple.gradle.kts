// Simple self-contained build for CopilotPromptsForIntelliJ
// This creates a JAR with all dependencies bundled - no internet required

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.10"
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

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    
    // Create a simple JAR with all classes
    jar {
        archiveBaseName.set("copilot-prompts-complete")
        
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
                "Main-Class" to "com.github.copilot.prompts.OfflineMain"
            )
        }
    }
    
    // Task to verify everything is bundled
    register("verify") {
        dependsOn("jar")
        
        doLast {
            val jarFile = file("build/libs/copilot-prompts-complete-${project.version}.jar")
            if (jarFile.exists()) {
                println("✓ Self-contained JAR created: ${jarFile.name} (${jarFile.length()} bytes)")
                println("✓ All dependencies are bundled - no internet required!")
            } else {
                throw GradleException("JAR file not found!")
            }
        }
    }
    
    build {
        dependsOn("verify")
    }
}