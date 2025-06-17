package com.github.copilot.prompts.action

import com.github.copilot.prompts.ui.PromptSelectorDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project

class ShowPromptsAction : AnAction() {
    
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR)
        
        val selectedText = editor?.selectionModel?.selectedText
        
        val dialog = PromptSelectorDialog(project, selectedText)
        dialog.show()
    }
    
    override fun update(e: AnActionEvent) {
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null
    }
}