# How to Install CopilotPromptsForIntelliJ Plugin

## Quick Installation Steps

1. **Locate the Plugin JAR**
   - Find `copilot-prompt-templates-1.0.0.jar` in the `build/libs/` directory
   - This is a self-contained JAR file (104 KB) with all dependencies bundled

2. **Install in IntelliJ IDEA**
   - Open IntelliJ IDEA
   - Go to **File** → **Settings** (Windows/Linux) or **IntelliJ IDEA** → **Preferences** (macOS)
   - Navigate to **Plugins**
   - Click the **gear icon (⚙️)** and select **Install Plugin from Disk...**
   - Browse and select the `copilot-prompt-templates-1.0.0.jar` file
   - Click **OK** and **restart IntelliJ IDEA**

3. **Verify Installation**
   - After restart, go to **View** → **Tool Windows** → **Copilot Prompts**
   - Or use keyboard shortcut: **Ctrl+Shift+P** (Windows/Linux) or **Cmd+Shift+P** (macOS)

## Features

- **Predefined Prompt Templates**: Ready-to-use prompts for code review, debugging, documentation, etc.
- **Offline Operation**: No internet required - all dependencies bundled
- **IntelliJ Integration**: Native tool window and keyboard shortcuts
- **Extensible**: Add your own custom prompts

## Troubleshooting

- **Plugin not visible**: Check **Settings** → **Plugins** and ensure "Copilot Prompt Templates" is enabled
- **Tool window missing**: Go to **View** → **Tool Windows** → **Copilot Prompts**
- **Java version**: Requires Java 17 or higher

## File Details

- **Plugin JAR**: `build/libs/copilot-prompt-templates-1.0.0.jar`
- **Size**: 104 KB (completely self-contained)
- **Dependencies**: All bundled (Kotlin stdlib, annotations)
- **Compatibility**: IntelliJ IDEA 2023.1+

---

For detailed installation instructions and troubleshooting, see [INSTALLATION_GUIDE.md](INSTALLATION_GUIDE.md)