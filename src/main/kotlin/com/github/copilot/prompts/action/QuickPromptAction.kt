package com.github.copilot.prompts.action

import com.github.copilot.prompts.service.PromptService
import com.github.copilot.prompts.ui.QuickPromptPopup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project

class QuickPromptAction : AnAction() {
    
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR)
        
        val selectedText = editor?.selectionModel?.selectedText
        val promptService = PromptService.getInstance()
        val favoritePrompts = promptService.getFavoritePrompts()
        
        if (favoritePrompts.isEmpty()) {
            // If no favorites, show all prompts
            val allPrompts = promptService.getAllPrompts().take(10) // Show top 10
            QuickPromptPopup.show(project, allPrompts, selectedText, editor)
        } else {
            QuickPromptPopup.show(project, favoritePrompts, selectedText, editor)
        }
    }
    
    override fun update(e: AnActionEvent) {
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null
    }
}