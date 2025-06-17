package com.github.copilot.prompts.service

import com.github.copilot.prompts.model.DefaultPrompts
import com.github.copilot.prompts.model.PromptCategory
import com.github.copilot.prompts.model.PromptTemplate
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "CopilotPromptsSettings",
    storages = [Storage("copilot-prompts.xml")]
)
class PromptService : PersistentStateComponent<PromptService.State> {
    
    data class State(
        var customPrompts: MutableList<PromptTemplate> = mutableListOf(),
        var favoritePromptIds: MutableSet<String> = mutableSetOf(),
        var hiddenPromptIds: MutableSet<String> = mutableSetOf()
    )
    
    private var state = State()
    
    companion object {
        fun getInstance(): PromptService {
            return ApplicationManager.getApplication().getService(PromptService::class.java)
        }
    }
    
    override fun getState(): State = state
    
    override fun loadState(state: State) {
        XmlSerializerUtil.copyBean(state, this.state)
    }
    
    fun getAllPrompts(): List<PromptTemplate> {
        val defaultPrompts = DefaultPrompts.templates.map { template ->
            template.copy(
                isFavorite = state.favoritePromptIds.contains(template.id)
            )
        }.filterNot { state.hiddenPromptIds.contains(it.id) }
        
        return defaultPrompts + state.customPrompts
    }
    
    fun getPromptsByCategory(category: PromptCategory): List<PromptTemplate> {
        return getAllPrompts().filter { it.category == category }
    }
    
    fun getFavoritePrompts(): List<PromptTemplate> {
        return getAllPrompts().filter { it.isFavorite }
    }
    
    fun addCustomPrompt(prompt: PromptTemplate) {
        state.customPrompts.add(prompt.copy(isCustom = true))
    }
    
    fun updateCustomPrompt(prompt: PromptTemplate) {
        val index = state.customPrompts.indexOfFirst { it.id == prompt.id }
        if (index >= 0) {
            state.customPrompts[index] = prompt.copy(isCustom = true)
        }
    }
    
    fun removeCustomPrompt(promptId: String) {
        state.customPrompts.removeIf { it.id == promptId }
    }
    
    fun toggleFavorite(promptId: String) {
        if (state.favoritePromptIds.contains(promptId)) {
            state.favoritePromptIds.remove(promptId)
        } else {
            state.favoritePromptIds.add(promptId)
        }
    }
    
    fun hidePrompt(promptId: String) {
        state.hiddenPromptIds.add(promptId)
    }
    
    fun showPrompt(promptId: String) {
        state.hiddenPromptIds.remove(promptId)
    }
    
    fun getPromptById(id: String): PromptTemplate? {
        return getAllPrompts().find { it.id == id }
    }
    
    fun searchPrompts(query: String): List<PromptTemplate> {
        val lowercaseQuery = query.lowercase()
        return getAllPrompts().filter { prompt ->
            prompt.title.lowercase().contains(lowercaseQuery) ||
            prompt.description.lowercase().contains(lowercaseQuery) ||
            prompt.prompt.lowercase().contains(lowercaseQuery) ||
            prompt.category.displayName.lowercase().contains(lowercaseQuery)
        }
    }
}