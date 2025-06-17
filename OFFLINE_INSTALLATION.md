# Offline Installation Guide

This guide explains how to build and use the CopilotPromptsForIntelliJ plugin without internet access.

## Prerequisites

- Java 17 or higher
- Kotlin 1.9.10 (can be bundled with Gradle)
- Gradle 8.0+ (use the included Gradle wrapper)

## Building Offline

### Option 1: Standard Build with Cached Dependencies

If you have previously built the project with internet access, the dependencies should be cached locally:

```bash
# Use the standard build (will use cached dependencies)
./gradlew build

# Or force offline mode
./gradlew build --offline
```

### Option 2: Offline-Only Build

Use the special offline build script that doesn't require IntelliJ SDK:

```bash
# Build using offline configuration
./gradlew -b build-offline.gradle.kts build

# Create standalone JAR
./gradlew -b build-offline.gradle.kts fatJar
```

### Option 3: Manual Dependency Preparation

If you have internet access initially, prepare for offline use:

```bash
# Download and cache all dependencies
./gradlew prepareOfflineDeps

# Then you can build offline later
./gradlew build --offline
```

## Installation Methods

### Method 1: IntelliJ Plugin Installation (Recommended)

1. Build the plugin:
   ```bash
   ./gradlew buildPlugin
   ```

2. The plugin JAR will be created in `build/distributions/`

3. In IntelliJ IDEA:
   - Go to `File` → `Settings` → `Plugins`
   - Click the gear icon → `Install Plugin from Disk...`
   - Select the generated JAR file
   - Restart IntelliJ IDEA

### Method 2: Manual Installation

1. Build the plugin:
   ```bash
   ./gradlew build
   ```

2. Copy the built JAR to IntelliJ's plugins directory:
   - **Windows**: `%APPDATA%\JetBrains\IntelliJIdea2023.2\plugins\`
   - **macOS**: `~/Library/Application Support/JetBrains/IntelliJIdea2023.2/plugins/`
   - **Linux**: `~/.local/share/JetBrains/IntelliJIdea2023.2/plugins/`

3. Restart IntelliJ IDEA

### Method 3: Standalone Testing (No IntelliJ Required)

For testing the core functionality without IntelliJ:

```bash
# Build standalone version
./gradlew -b build-offline.gradle.kts fatJar

# Run standalone demo
java -jar build/libs/copilot-prompts-standalone-1.0.0.jar
```

## Offline Features

When running offline, the plugin provides:

### Core Functionality
- ✅ All prompt templates and categories
- ✅ Prompt management and organization
- ✅ Text processing and variable substitution
- ✅ Basic UI components

### Limited Functionality
- ⚠️ GitHub Copilot integration (requires Copilot plugin)
- ⚠️ Advanced IntelliJ UI components (fallback to basic Swing)
- ⚠️ IntelliJ-specific notifications (fallback to dialogs)

### Fallback Implementations
- Standard Swing components instead of IntelliJ UI
- System clipboard instead of IntelliJ clipboard manager
- JOptionPane dialogs instead of IntelliJ notifications
- Basic file operations instead of IntelliJ VFS

## Troubleshooting

### Build Issues

**Problem**: `Could not resolve dependencies`
```bash
# Solution: Use offline mode with cached dependencies
./gradlew build --offline
```

**Problem**: `Plugin 'org.jetbrains.intellij' not found`
```bash
# Solution: Use offline build script
./gradlew -b build-offline.gradle.kts build
```

### Runtime Issues

**Problem**: `ClassNotFoundException` for IntelliJ classes
- This is expected in offline mode
- The plugin automatically falls back to stub implementations
- Core functionality will still work

**Problem**: Plugin not loading in IntelliJ
- Ensure you're using a compatible IntelliJ version (2023.2+)
- Check that the plugin JAR is in the correct plugins directory
- Restart IntelliJ after installation

### Dependency Issues

**Problem**: Missing Kotlin standard library
```bash
# Solution: Ensure Kotlin stdlib is available
./gradlew -b build-offline.gradle.kts prepareOfflineDeps
```

## File Structure for Offline Use

```
CopilotPromptsForIntelliJ/
├── build-offline.gradle.kts    # Offline build configuration
├── libs/                       # Cached dependencies (created by prepareOfflineDeps)
├── src/main/kotlin/
│   ├── com/github/copilot/prompts/
│   │   ├── fallback/          # Stub implementations
│   │   ├── compat/            # Compatibility layer
│   │   └── OfflineMain.kt     # Standalone demo
└── gradle/wrapper/            # Gradle wrapper (works offline)
```

## Advanced Offline Setup

### Creating a Complete Offline Package

1. Prepare all dependencies:
   ```bash
   ./gradlew prepareOfflineDeps
   ./gradlew wrapper --gradle-version=8.4
   ```

2. Create offline package:
   ```bash
   tar -czf copilot-prompts-offline.tar.gz \
     src/ \
     build.gradle.kts \
     build-offline.gradle.kts \
     gradle/ \
     gradlew \
     gradlew.bat \
     libs/ \
     *.md
   ```

3. Transfer to offline environment and extract

4. Build without internet:
   ```bash
   ./gradlew -b build-offline.gradle.kts build --offline
   ```

## Compatibility Matrix

| Feature | Online Mode | Offline Mode | Fallback |
|---------|-------------|--------------|----------|
| Prompt Templates | ✅ Full | ✅ Full | N/A |
| IntelliJ UI | ✅ Native | ⚠️ Basic | Swing Components |
| Notifications | ✅ Native | ⚠️ Basic | JOptionPane |
| Clipboard | ✅ Native | ✅ Full | System Clipboard |
| File Operations | ✅ Native | ✅ Basic | Java IO |
| Plugin Updates | ✅ Available | ❌ Manual | Manual Installation |

## Support

For offline-specific issues:
1. Check the console output for error messages
2. Verify Java and Kotlin versions
3. Ensure all required files are present
4. Try the standalone demo first to test core functionality

The plugin is designed to gracefully degrade functionality when IntelliJ SDK components are not available, ensuring core features remain accessible in offline environments.