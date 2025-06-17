# Installation Guide

## Prerequisites

- IntelliJ IDEA 2023.2 or later
- GitHub Copilot plugin (optional but recommended for full functionality)

## Installation Methods

### Method 1: From Plugin File (Recommended for Testing)

1. Download the plugin file: `copilot-prompt-templates-1.0.0.zip`
2. Open IntelliJ IDEA
3. Go to **File → Settings** (or **IntelliJ IDEA → Preferences** on macOS)
4. Navigate to **Plugins**
5. Click the gear icon (⚙️) and select **Install Plugin from Disk...**
6. Select the downloaded `copilot-prompt-templates-1.0.0.zip` file
7. Click **OK** and restart IntelliJ IDEA

### Method 2: From Source (For Development)

1. Clone or download the source code
2. Open terminal in the project directory
3. Run: `./gradlew buildPlugin`
4. Install the generated plugin from `build/distributions/copilot-prompt-templates-1.0.0.zip`

## First Time Setup

1. After installation, restart IntelliJ IDEA
2. Open any project
3. You should see a new "Copilot Prompts" tool window on the right sidebar
4. Access prompts via:
   - **Ctrl+Shift+P** (Cmd+Shift+P on Mac): Full prompt selector
   - **Ctrl+Alt+P** (Cmd+Alt+P on Mac): Quick prompt popup
   - **Tools → Copilot Prompts → Show Prompt Templates**

## Configuration

1. Go to **File → Settings → Tools → Copilot Prompts**
2. Add your custom prompts
3. Manage existing prompts
4. Configure favorites

## Usage

1. Select code in the editor (optional)
2. Use one of the access methods above
3. Choose a prompt from the list
4. The prompt will be copied to clipboard and GitHub Copilot Chat will open (if available)
5. Paste the prompt in Copilot Chat

## Troubleshooting

### Plugin Not Appearing
- Ensure you're using IntelliJ IDEA 2023.2 or later
- Check that the plugin is enabled in Settings → Plugins
- Restart IntelliJ IDEA

### GitHub Copilot Integration Not Working
- Install the GitHub Copilot plugin from JetBrains Marketplace
- Ensure you're logged in to GitHub Copilot
- The plugin will fall back to copying prompts to clipboard if Copilot is not available

### Tool Window Not Visible
- Go to **View → Tool Windows → Copilot Prompts**
- Or use the keyboard shortcuts to access prompts directly

## Uninstallation

1. Go to **File → Settings → Plugins**
2. Find "Copilot Prompt Templates" in the installed plugins list
3. Click the gear icon and select **Uninstall**
4. Restart IntelliJ IDEA