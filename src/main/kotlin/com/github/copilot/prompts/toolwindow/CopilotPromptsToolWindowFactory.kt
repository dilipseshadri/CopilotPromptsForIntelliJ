package com.github.copilot.prompts.toolwindow

import com.github.copilot.prompts.model.PromptCategory
import com.github.copilot.prompts.model.PromptTemplate
import com.github.copilot.prompts.service.PromptService
import com.github.copilot.prompts.ui.CopilotIntegration
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.*
import com.intellij.ui.content.ContentFactory
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.*

class CopilotPromptsToolWindowFactory : ToolWindowFactory {
    
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val toolWindowContent = CopilotPromptsToolWindowContent(project)
        val content = ContentFactory.getInstance().createContent(
            toolWindowContent.getContent(),
            "",
            false
        )
        toolWindow.contentManager.addContent(content)
    }
}

class CopilotPromptsToolWindowContent(private val project: Project) {
    
    private val promptService = PromptService.getInstance()
    private val searchField = JBTextField()
    private val categoryCombo = JComboBox<PromptCategory>()
    private val promptList = JBList<PromptTemplate>()
    private val promptPreview = JBTextArea()
    
    fun getContent(): JComponent {
        val panel = JPanel(BorderLayout())
        
        // Top panel with search and filter
        val topPanel = createTopPanel()
        
        // Center panel with list and preview
        val centerPanel = createCenterPanel()
        
        panel.add(topPanel, BorderLayout.NORTH)
        panel.add(centerPanel, BorderLayout.CENTER)
        
        setupComponents()
        updatePromptList()
        
        return panel
    }
    
    private fun createTopPanel(): JPanel {
        val panel = JPanel(GridBagLayout())
        panel.border = JBUI.Borders.empty(5)
        
        val gbc = GridBagConstraints()
        
        // Search field
        gbc.gridx = 0
        gbc.gridy = 0
        gbc.anchor = GridBagConstraints.WEST
        panel.add(JLabel("Search:"), gbc)
        
        gbc.gridx = 1
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.weightx = 1.0
        gbc.insets = JBUI.insets(0, 5, 0, 10)
        panel.add(searchField, gbc)
        
        // Category filter
        gbc.gridx = 2
        gbc.fill = GridBagConstraints.NONE
        gbc.weightx = 0.0
        gbc.insets = JBUI.insets(0, 0, 0, 5)
        panel.add(JLabel("Category:"), gbc)
        
        gbc.gridx = 3
        gbc.insets = JBUI.insets(0, 0, 0, 0)
        panel.add(categoryCombo, gbc)
        
        return panel
    }
    
    private fun createCenterPanel(): JComponent {
        val panel = JSplitPane(JSplitPane.VERTICAL_SPLIT)
        
        // Prompt list
        val listPanel = JPanel(BorderLayout()).apply {
            border = JBUI.Borders.empty(5)
            add(JLabel("Prompts:"), BorderLayout.NORTH)
            add(JScrollPane(promptList), BorderLayout.CENTER)
        }
        
        // Preview and action panel
        val previewPanel = JPanel(BorderLayout()).apply {
            border = JBUI.Borders.empty(5)
            
            val previewLabel = JLabel("Preview:")
            val scrollPane = JScrollPane(promptPreview).apply {
                preferredSize = JBUI.size(300, 150)
            }
            
            val buttonPanel = JPanel().apply {
                val sendButton = JButton("Send to Copilot").apply {
                    addActionListener { sendSelectedPrompt() }
                }
                val favoriteButton = JButton("Toggle Favorite").apply {
                    addActionListener { toggleFavorite() }
                }
                
                add(sendButton)
                add(favoriteButton)
            }
            
            add(previewLabel, BorderLayout.NORTH)
            add(scrollPane, BorderLayout.CENTER)
            add(buttonPanel, BorderLayout.SOUTH)
        }
        
        panel.topComponent = listPanel
        panel.bottomComponent = previewPanel
        panel.setDividerLocation(200)
        
        return panel
    }
    
    private fun setupComponents() {
        // Setup category combo
        categoryCombo.addItem(null) // "All categories"
        PromptCategory.values().forEach { categoryCombo.addItem(it) }
        categoryCombo.renderer = object : DefaultListCellRenderer() {
            override fun getListCellRendererComponent(
                list: JList<*>?,
                value: Any?,
                index: Int,
                isSelected: Boolean,
                cellHasFocus: Boolean
            ): java.awt.Component {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
                text = (value as? PromptCategory)?.displayName ?: "All Categories"
                return this
            }
        }
        
        // Setup search field
        searchField.addActionListener { updatePromptList() }
        
        // Setup category combo
        categoryCombo.addActionListener { updatePromptList() }
        
        // Setup prompt list
        promptList.cellRenderer = object : DefaultListCellRenderer() {
            override fun getListCellRendererComponent(
                list: JList<*>?,
                value: Any?,
                index: Int,
                isSelected: Boolean,
                cellHasFocus: Boolean
            ): java.awt.Component {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
                val prompt = value as? PromptTemplate
                if (prompt != null) {
                    text = "${if (prompt.isFavorite) "★ " else ""}${prompt.title}"
                }
                return this
            }
        }
        
        promptList.addListSelectionListener { updatePreview() }
        
        // Setup preview
        promptPreview.isEditable = false
        promptPreview.lineWrap = true
        promptPreview.wrapStyleWord = true
    }
    
    private fun updatePromptList() {
        val searchQuery = searchField.text.trim()
        val selectedCategory = categoryCombo.selectedItem as? PromptCategory
        
        val filteredPrompts = promptService.getAllPrompts().filter { prompt ->
            val matchesSearch = searchQuery.isEmpty() || 
                prompt.title.contains(searchQuery, ignoreCase = true) ||
                prompt.description.contains(searchQuery, ignoreCase = true) ||
                prompt.prompt.contains(searchQuery, ignoreCase = true)
            
            val matchesCategory = selectedCategory == null || prompt.category == selectedCategory
            
            matchesSearch && matchesCategory
        }
        
        promptList.setListData(filteredPrompts.toTypedArray())
        
        if (filteredPrompts.isNotEmpty()) {
            promptList.selectedIndex = 0
            updatePreview()
        }
    }
    
    private fun updatePreview() {
        val selectedPrompt = promptList.selectedValue
        if (selectedPrompt != null) {
            promptPreview.text = buildString {
                appendLine("Title: ${selectedPrompt.title}")
                appendLine("Category: ${selectedPrompt.category.displayName}")
                appendLine("Description: ${selectedPrompt.description}")
                appendLine()
                appendLine("Prompt:")
                appendLine(selectedPrompt.prompt)
            }
        } else {
            promptPreview.text = ""
        }
    }
    
    private fun sendSelectedPrompt() {
        val selectedPrompt = promptList.selectedValue
        if (selectedPrompt != null) {
            CopilotIntegration.sendPrompt(project, selectedPrompt, null)
        }
    }
    
    private fun toggleFavorite() {
        val selectedPrompt = promptList.selectedValue
        if (selectedPrompt != null) {
            promptService.toggleFavorite(selectedPrompt.id)
            updatePromptList() // Refresh to show updated favorite status
        }
    }
}