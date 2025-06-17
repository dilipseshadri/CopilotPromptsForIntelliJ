# Project Structure

This document outlines the structure of the Copilot Prompt Templates IntelliJ plugin.

## Directory Structure

```
copilot-prompt-templates/
├── build.gradle.kts                 # Gradle build configuration
├── settings.gradle.kts              # Gradle settings
├── gradle.properties               # Gradle properties
├── gradle/wrapper/                 # Gradle wrapper files
├── src/
│   └── main/
│       ├── kotlin/com/github/copilot/prompts/
│       │   ├── action/             # Action classes for menu items and shortcuts
│       │   │   ├── QuickPromptAction.kt
│       │   │   └── ShowPromptsAction.kt
│       │   ├── model/              # Data models
│       │   │   └── PromptTemplate.kt
│       │   ├── service/            # Application services
│       │   │   └── PromptService.kt
│       │   ├── settings/           # Settings/configuration UI
│       │   │   └── PromptSettingsConfigurable.kt
│       │   ├── toolwindow/         # Tool window implementation
│       │   │   └── CopilotPromptsToolWindowFactory.kt
│       │   └── ui/                 # UI components
│       │       ├── CopilotIntegration.kt
│       │       ├── PromptSelectorDialog.kt
│       │       └── QuickPromptPopup.kt
│       └── resources/
│           ├── META-INF/
│           │   ├── plugin.xml      # Plugin descriptor
│           │   ├── copilot-integration.xml
│           │   └── pluginIcon.svg  # Plugin icon
│           └── icons/
│               └── copilot-prompts.svg
├── build/                          # Build output (generated)
│   └── distributions/
│       └── copilot-prompt-templates-1.0.0.zip
├── README.md                       # Main documentation
├── INSTALLATION.md                 # Installation guide
├── DEMO.md                        # Feature demonstration
├── CHANGELOG.md                   # Version history
├── LICENSE                        # MIT License
└── PROJECT_STRUCTURE.md           # This file
```

## Key Components

### Core Classes

#### `PromptTemplate.kt`
- Data class representing a prompt template
- Contains predefined prompts organized by category
- Defines prompt categories (Code Review, Debugging, etc.)

#### `PromptService.kt`
- Application service for managing prompts
- Handles persistence of custom prompts and favorites
- Provides search and filtering functionality
- Implements state management for user preferences

### Actions

#### `ShowPromptsAction.kt`
- Main action for opening the prompt selector dialog
- Triggered by Ctrl+Shift+P shortcut
- Handles selected text context

#### `QuickPromptAction.kt`
- Quick access action for favorite prompts
- Triggered by Ctrl+Alt+P shortcut
- Shows popup with favorite or top prompts

### UI Components

#### `PromptSelectorDialog.kt`
- Main dialog for browsing and selecting prompts
- Features search, filtering, and preview
- Handles prompt selection and sending to Copilot

#### `QuickPromptPopup.kt`
- Lightweight popup for quick prompt access
- Shows favorite prompts or top prompts
- Supports speed search

#### `CopilotPromptsToolWindowFactory.kt`
- Creates and manages the tool window
- Provides persistent access to prompts
- Integrates with IDE sidebar

#### `CopilotIntegration.kt`
- Handles integration with GitHub Copilot Chat
- Manages clipboard operations as fallback
- Shows notifications to user

### Settings

#### `PromptSettingsConfigurable.kt`
- Settings panel for managing custom prompts
- Add, edit, delete custom prompts
- Integrates with IDE settings system

### Configuration

#### `plugin.xml`
- Main plugin descriptor
- Defines extensions, actions, and dependencies
- Configures tool windows and settings

#### `copilot-integration.xml`
- Optional integration configuration
- Loaded when GitHub Copilot plugin is available

## Build System

### Gradle Configuration
- Uses Gradle with Kotlin DSL
- IntelliJ Platform Gradle Plugin for plugin development
- Kotlin plugin for Kotlin source compilation
- Targets IntelliJ IDEA 2023.2+

### Key Gradle Tasks
- `./gradlew build` - Build the plugin
- `./gradlew buildPlugin` - Create distribution ZIP
- `./gradlew runIde` - Run plugin in development IDE
- `./gradlew publishPlugin` - Publish to JetBrains Marketplace

## Plugin Features

### Predefined Prompts
- 8 categories with multiple prompts each
- Comprehensive coverage of common development tasks
- Professional, detailed prompt templates

### User Interface
- Tool window integration
- Keyboard shortcuts
- Search and filtering
- Favorites system
- Settings panel

### Integration
- GitHub Copilot Chat integration
- Clipboard fallback
- Selected code context
- Notification system

### Persistence
- Custom prompts storage
- Favorites management
- User preferences
- Settings synchronization

## Development Notes

### Dependencies
- IntelliJ Platform SDK
- Kotlin standard library (provided by platform)
- Optional GitHub Copilot plugin integration

### Compatibility
- IntelliJ IDEA 2023.2+
- Java 17+
- Kotlin 1.9+

### Architecture Patterns
- Service-oriented architecture
- MVC pattern for UI components
- Observer pattern for state management
- Factory pattern for component creation

This structure provides a clean separation of concerns and follows IntelliJ Platform development best practices.