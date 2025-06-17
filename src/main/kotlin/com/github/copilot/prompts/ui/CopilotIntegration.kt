package com.github.copilot.prompts.ui

import com.github.copilot.prompts.model.PromptTemplate
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

object CopilotIntegration {
    
    fun sendPrompt(project: Project, prompt: PromptTemplate, selectedText: String?) {
        val fullPrompt = buildString {
            append(prompt.prompt)
            
            if (!selectedText.isNullOrBlank()) {
                appendLine()
                appendLine()
                appendLine("```")
                appendLine(selectedText)
                appendLine("```")
            }
        }
        
        // Try to integrate with GitHub Copilot Chat
        val success = tryOpenCopilotChat(project, fullPrompt)
        
        if (!success) {
            // Fallback: Copy to clipboard and show notification
            copyToClipboard(fullPrompt)
            showNotification(
                project,
                "Prompt copied to clipboard",
                "The prompt has been copied to your clipboard. Paste it in GitHub Copilot Chat.",
                NotificationType.INFORMATION
            )
        }
    }
    
    private fun tryOpenCopilotChat(project: Project, prompt: String): Boolean {
        return try {
            // Try to find and open GitHub Copilot Chat tool window
            val toolWindowManager = ToolWindowManager.getInstance(project)
            val copilotToolWindow = toolWindowManager.getToolWindow("GitHub Copilot Chat")
            
            if (copilotToolWindow != null) {
                ApplicationManager.getApplication().invokeLater {
                    copilotToolWindow.activate {
                        // Copy prompt to clipboard for easy pasting
                        copyToClipboard(prompt)
                        showNotification(
                            project,
                            "Copilot Chat opened",
                            "The prompt has been copied to clipboard. Paste it in the chat.",
                            NotificationType.INFORMATION
                        )
                    }
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
    
    private fun copyToClipboard(text: String) {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val selection = StringSelection(text)
        clipboard.setContents(selection, null)
    }
    
    private fun showNotification(
        project: Project,
        title: String,
        content: String,
        type: NotificationType
    ) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("Copilot Prompts")
            .createNotification(title, content, type)
            .notify(project)
    }
}