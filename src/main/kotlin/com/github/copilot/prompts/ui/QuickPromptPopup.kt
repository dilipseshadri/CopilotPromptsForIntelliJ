package com.github.copilot.prompts.ui

import com.github.copilot.prompts.model.PromptTemplate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.PopupStep
import com.intellij.openapi.ui.popup.util.BaseListPopupStep
import com.intellij.ui.popup.list.ListPopupImpl
import javax.swing.Icon

object QuickPromptPopup {
    
    fun show(
        project: Project,
        prompts: List<PromptTemplate>,
        selectedText: String?,
        editor: Editor?
    ) {
        if (prompts.isEmpty()) return
        
        val popup = JBPopupFactory.getInstance().createListPopup(
            PromptPopupStep(prompts, project, selectedText)
        )
        
        if (editor != null) {
            popup.showInBestPositionFor(editor)
        } else {
            popup.showCenteredInCurrentWindow(project)
        }
    }
    
    private class PromptPopupStep(
        private val prompts: List<PromptTemplate>,
        private val project: Project,
        private val selectedText: String?
    ) : BaseListPopupStep<PromptTemplate>("Quick Prompts", prompts) {
        
        override fun getTextFor(value: PromptTemplate): String {
            return "${if (value.isFavorite) "★ " else ""}${value.title}"
        }
        
        override fun getIconFor(value: PromptTemplate): Icon? {
            return null // Could add category icons here
        }
        
        override fun onChosen(selectedValue: PromptTemplate, finalChoice: Boolean): PopupStep<*>? {
            if (finalChoice) {
                CopilotIntegration.sendPrompt(project, selectedValue, selectedText)
            }
            return PopupStep.FINAL_CHOICE
        }
        
        override fun isSpeedSearchEnabled(): Boolean = true
        
        override fun getSpeedSearchFilter(): com.intellij.openapi.ui.popup.SpeedSearchFilter<PromptTemplate>? {
            return null // Speed search will be handled by default implementation
        }
    }
}