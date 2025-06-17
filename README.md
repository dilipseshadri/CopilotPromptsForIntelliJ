# Copilot Prompt Templates

A JetBrains IntelliJ IDEA plugin that provides predefined prompt templates for GitHub Copilot chat, making it easier to interact with Copilot using common development scenarios.

## Features

- **Predefined Prompt Templates**: Access a curated collection of prompts for common development tasks
- **Categorized Prompts**: Organized by categories like Code Review, Debugging, Documentation, Testing, etc.
- **Quick Access**: Use keyboard shortcuts or the tool window for fast prompt selection
- **Favorites System**: Mark frequently used prompts as favorites for quick access
- **Custom Prompts**: Create and manage your own custom prompt templates
- **Search & Filter**: Easily find prompts using search and category filters
- **Seamless Integration**: Automatically opens GitHub Copilot Chat and copies prompts to clipboard

## Installation

1. Download the plugin from the JetBrains Plugin Repository (when published)
2. Or build from source:
   ```bash
   ./gradlew buildPlugin
   ```
3. Install the generated plugin file in IntelliJ IDEA

## Usage

### Quick Access
- **Ctrl+Shift+P** (Cmd+Shift+P on Mac): Open the full prompt selector dialog
- **Ctrl+Alt+P** (Cmd+Alt+P on Mac): Open quick prompt popup with favorites

### Tool Window
- Access the "Copilot Prompts" tool window from the right sidebar
- Browse, search, and filter prompts
- Send prompts directly to GitHub Copilot Chat

### Menu Access
- Go to **Tools → Copilot Prompts → Show Prompt Templates**

## Default Prompt Categories

### Code Review
- General code review for best practices and improvements
- Security-focused code review
- Performance optimization review

### Debugging
- Error analysis and troubleshooting
- Step-by-step debugging assistance
- Root cause analysis

### Documentation
- Generate comprehensive code documentation
- API documentation creation
- README and guide generation

### Testing
- Unit test generation
- Test case creation for edge cases
- Testing strategy recommendations

### Refactoring
- Code structure improvements
- SOLID principles application
- Code cleanup and optimization

### Architecture
- Design pattern identification
- System architecture analysis
- Component relationship explanation

### Performance
- Performance bottleneck identification
- Optimization suggestions
- Profiling recommendations

### Security
- Security vulnerability assessment
- Secure coding practices
- Authentication and authorization review

## Customization

### Adding Custom Prompts
1. Go to **File → Settings → Tools → Copilot Prompts**
2. Click "Add" to create a new custom prompt
3. Fill in the title, description, category, and prompt text
4. Save your changes

### Managing Favorites
- Click the star (★) icon next to any prompt to mark it as a favorite
- Favorites appear first in the quick prompt popup
- Use the tool window to easily manage your favorite prompts

## Development

### Building the Plugin
```bash
./gradlew buildPlugin
```

### Running in Development
```bash
./gradlew runIde
```

### Testing
```bash
./gradlew test
```

## Requirements

- IntelliJ IDEA 2023.2 or later
- GitHub Copilot plugin installed and configured

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

If you encounter any issues or have suggestions for improvement, please:
1. Check the existing issues on GitHub
2. Create a new issue with detailed information
3. Include your IntelliJ IDEA version and plugin version

## Changelog

### Version 1.0.0
- Initial release
- 8 predefined prompt categories with multiple templates
- Tool window integration
- Quick access shortcuts
- Custom prompt management
- Favorites system
- Search and filter functionality
- GitHub Copilot Chat integration