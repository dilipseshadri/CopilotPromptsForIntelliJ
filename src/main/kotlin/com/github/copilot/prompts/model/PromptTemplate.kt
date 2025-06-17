package com.github.copilot.prompts.model

data class PromptTemplate(
    val id: String,
    val title: String,
    val description: String,
    val prompt: String,
    val category: PromptCategory,
    val isFavorite: Boolean = false,
    val isCustom: Boolean = false
)

enum class PromptCategory(val displayName: String) {
    CODE_REVIEW("Code Review"),
    DEBUGGING("Debugging"),
    DOCUMENTATION("Documentation"),
    REFACTORING("Refactoring"),
    TESTING("Testing"),
    ARCHITECTURE("Architecture"),
    PERFORMANCE("Performance"),
    SECURITY("Security"),
    CUSTOM("Custom")
}

object DefaultPrompts {
    val templates = listOf(
        PromptTemplate(
            id = "code-review-general",
            title = "General Code Review",
            description = "Review the selected code for best practices, potential issues, and improvements",
            prompt = "Please review this code for:\n- Code quality and best practices\n- Potential bugs or issues\n- Performance improvements\n- Readability and maintainability\n- Security considerations\n\nProvide specific suggestions with examples where applicable.",
            category = PromptCategory.CODE_REVIEW
        ),
        PromptTemplate(
            id = "debug-error",
            title = "Debug Error",
            description = "Help debug an error or exception",
            prompt = "I'm encountering an error in this code. Please help me:\n- Identify the root cause of the error\n- Explain why this error is occurring\n- Provide a step-by-step solution to fix it\n- Suggest ways to prevent similar errors in the future",
            category = PromptCategory.DEBUGGING
        ),
        PromptTemplate(
            id = "generate-docs",
            title = "Generate Documentation",
            description = "Generate comprehensive documentation for code",
            prompt = "Please generate comprehensive documentation for this code including:\n- Clear description of what the code does\n- Parameter descriptions and types\n- Return value description\n- Usage examples\n- Any important notes or warnings\n\nFormat it appropriately for the language (JSDoc, Javadoc, etc.)",
            category = PromptCategory.DOCUMENTATION
        ),
        PromptTemplate(
            id = "refactor-improve",
            title = "Refactor for Improvement",
            description = "Suggest refactoring improvements for better code quality",
            prompt = "Please suggest refactoring improvements for this code focusing on:\n- Code structure and organization\n- Reducing complexity and improving readability\n- Following SOLID principles\n- Eliminating code duplication\n- Improving naming conventions\n\nProvide the refactored code with explanations for each change.",
            category = PromptCategory.REFACTORING
        ),
        PromptTemplate(
            id = "write-tests",
            title = "Write Unit Tests",
            description = "Generate comprehensive unit tests for the selected code",
            prompt = "Please write comprehensive unit tests for this code including:\n- Test cases for normal/expected behavior\n- Edge cases and boundary conditions\n- Error handling scenarios\n- Mock dependencies where appropriate\n- Clear test descriptions and assertions\n\nUse the appropriate testing framework for the language.",
            category = PromptCategory.TESTING
        ),
        PromptTemplate(
            id = "explain-architecture",
            title = "Explain Architecture",
            description = "Explain the architectural patterns and design decisions",
            prompt = "Please analyze and explain the architecture of this code:\n- Identify design patterns being used\n- Explain the overall structure and organization\n- Describe the relationships between components\n- Highlight architectural strengths and potential improvements\n- Suggest alternative architectural approaches if applicable",
            category = PromptCategory.ARCHITECTURE
        ),
        PromptTemplate(
            id = "optimize-performance",
            title = "Optimize Performance",
            description = "Analyze and suggest performance optimizations",
            prompt = "Please analyze this code for performance optimization:\n- Identify potential performance bottlenecks\n- Suggest specific optimizations with examples\n- Explain the impact of each optimization\n- Consider time and space complexity\n- Recommend profiling strategies if needed",
            category = PromptCategory.PERFORMANCE
        ),
        PromptTemplate(
            id = "security-review",
            title = "Security Review",
            description = "Review code for security vulnerabilities and best practices",
            prompt = "Please conduct a security review of this code:\n- Identify potential security vulnerabilities\n- Check for common security anti-patterns\n- Suggest secure coding practices\n- Recommend input validation and sanitization\n- Highlight any authentication/authorization concerns",
            category = PromptCategory.SECURITY
        )
    )
}