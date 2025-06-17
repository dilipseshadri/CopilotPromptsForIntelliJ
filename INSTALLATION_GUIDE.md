# CopilotPromptsForIntelliJ - Installation Guide

This guide explains how to install the Copilot Prompts plugin in IntelliJ IDEA with all dependencies bundled for offline use.

## ✅ READY FOR INSTALLATION

The plugin has been successfully built and is ready for installation in IntelliJ IDEA. All dependencies are bundled and no internet connection is required.

## Overview

This plugin provides predefined prompt templates for GitHub Copilot chat, with all dependencies bundled to work completely offline without requiring internet access for dependency downloads.

## Prerequisites

- IntelliJ IDEA (Community or Ultimate Edition)
- Java 17 or higher
- No internet connection required after initial setup

## Installation Methods

### Method 1: Install Pre-built Plugin JAR (Recommended)

1. **Download the Plugin JAR**
   - Use the pre-built `copilot-prompt-templates-1.0.0.jar` from the `build/libs/` directory
   - This JAR contains all dependencies bundled (104 KB - completely self-contained)

2. **Install in IntelliJ IDEA**
   - Open IntelliJ IDEA
   - Go to `File` → `Settings` (or `IntelliJ IDEA` → `Preferences` on macOS)
   - Navigate to `Plugins`
   - Click the gear icon (⚙️) and select `Install Plugin from Disk...`
   - Browse and select the `copilot-prompt-templates-1.0.0.jar` file
   - Click `OK` and restart IntelliJ IDEA

### Method 2: Build from Source (Offline)

If you need to build the plugin yourself:

1. **Ensure Dependencies are Bundled**
   ```bash
   ./gradlew prepareOfflineDeps
   ```

2. **Build the Plugin**
   ```bash
   ./gradlew build
   ```

3. **Install the Generated JAR**
   - The plugin JAR will be created in `build/libs/`
   - Follow the installation steps from Method 1

### Method 3: Completely Offline Build

For environments with no internet access:

1. **Use the Self-Contained Build**
   ```bash
   ./gradlew -b build-simple.gradle.kts build
   ```

2. **Alternative Java-Only Build**
   ```bash
   ./gradlew -b build-java-only.gradle.kts build
   ```

## Plugin Features

Once installed, the plugin provides:

- **Tool Window**: Access via `View` → `Tool Windows` → `Copilot Prompts`
- **Quick Actions**: 
  - `Ctrl+Shift+P` (Windows/Linux) or `Cmd+Shift+P` (macOS) - Show Prompt Templates
  - `Ctrl+Alt+P` (Windows/Linux) or `Cmd+Alt+P` (macOS) - Quick Prompt Access
- **Menu Access**: `Tools` → `Copilot Prompts`

## Bundled Dependencies

The plugin includes all necessary dependencies:

- `kotlin-stdlib-1.9.10.jar` (1.7 MB)
- `kotlin-stdlib-common-1.9.10.jar` (225 KB)
- `kotlin-stdlib-jdk8-1.9.10.jar` (965 bytes)
- `annotations-13.0.jar` (17 KB)

**Total size**: ~104 KB (completely self-contained)

## Offline Compatibility

This plugin is designed to work completely offline:

- ✅ No internet required for installation
- ✅ No external dependency downloads
- ✅ All libraries bundled in the plugin JAR
- ✅ Fallback implementations for missing IntelliJ components
- ✅ Graceful degradation when GitHub Copilot is not available

## Troubleshooting

### Plugin Not Loading

1. **Check Java Version**
   - Ensure you're using Java 17 or higher
   - Check in IntelliJ: `Help` → `About`

2. **Verify Plugin Installation**
   - Go to `Settings` → `Plugins`
   - Ensure "Copilot Prompt Templates" is listed and enabled

3. **Check Logs**
   - Go to `Help` → `Show Log in Explorer/Finder`
   - Look for any error messages related to the plugin

### Missing Features

1. **GitHub Copilot Integration**
   - The plugin works independently of GitHub Copilot
   - If Copilot is not installed, the plugin provides standalone functionality

2. **Tool Window Not Visible**
   - Go to `View` → `Tool Windows` → `Copilot Prompts`
   - Or use the keyboard shortcut `Ctrl+Shift+P`

## File Structure

```
CopilotPromptsForIntelliJ/
├── build/libs/                             # Built plugin JARs
│   └── copilot-prompt-templates-1.0.0.jar # Main plugin JAR (104 KB)
├── libs/                                 # Bundled dependencies
│   ├── kotlin-stdlib-1.9.10.jar
│   ├── kotlin-stdlib-common-1.9.10.jar
│   ├── kotlin-stdlib-jdk8-1.9.10.jar
│   └── annotations-13.0.jar
├── src/main/resources/META-INF/
│   └── plugin.xml                       # Plugin configuration
└── src/main/kotlin/                     # Plugin source code
```

## Advanced Configuration

### Offline Mode

To force completely offline operation, set in `gradle.properties`:
```properties
offline=true
```

### Custom Build

For custom builds without IntelliJ SDK:
```bash
./gradlew -b build-offline.gradle.kts build
```

## Support

For issues or questions:

1. Check the bundled dependencies in the `libs/` directory
2. Verify the plugin JAR contains all necessary files
3. Ensure IntelliJ IDEA version compatibility
4. Review the installation logs for any errors

## Version Information

- **Plugin Version**: 1.0.0
- **Kotlin Version**: 1.9.10
- **Java Target**: 17
- **IntelliJ Platform**: 2023.1+

---

**Note**: This plugin is designed to be completely self-contained and work offline. All dependencies are bundled, and no internet connection is required after installation.