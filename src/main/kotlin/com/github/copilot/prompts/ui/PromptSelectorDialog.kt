package com.github.copilot.prompts.ui

import com.github.copilot.prompts.model.PromptCategory
import com.github.copilot.prompts.model.PromptTemplate
import com.github.copilot.prompts.service.PromptService
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.*
import com.intellij.ui.table.JBTable
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.table.AbstractTableModel

class PromptSelectorDialog(
    private val project: Project,
    private val selectedText: String?
) : DialogWrapper(project) {
    
    private val promptService = PromptService.getInstance()
    private var filteredPrompts = promptService.getAllPrompts()
    
    private val searchField = JBTextField()
    private val categoryCombo = JComboBox<PromptCategory>()
    private val promptTable = JBTable()
    private val promptPreview = JBTextArea()
    private val favoriteButton = JButton("★")
    
    init {
        title = "Copilot Prompt Templates"
        setSize(800, 600)
        init()
        setupUI()
        updateTable()
    }
    
    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout())
        
        // Top panel with search and filter
        val topPanel = JPanel(BorderLayout()).apply {
            border = JBUI.Borders.empty(5)
            
            val searchPanel = JPanel(BorderLayout()).apply {
                add(JLabel("Search: "), BorderLayout.WEST)
                add(searchField, BorderLayout.CENTER)
            }
            
            val filterPanel = JPanel(BorderLayout()).apply {
                add(JLabel("Category: "), BorderLayout.WEST)
                add(categoryCombo, BorderLayout.CENTER)
            }
            
            add(searchPanel, BorderLayout.WEST)
            add(filterPanel, BorderLayout.EAST)
        }
        
        // Center panel with table and preview
        val centerPanel = JSplitPane(JSplitPane.HORIZONTAL_SPLIT).apply {
            leftComponent = JScrollPane(promptTable).apply {
                preferredSize = Dimension(400, 400)
            }
            
            rightComponent = JPanel(BorderLayout()).apply {
                val previewPanel = JPanel(BorderLayout()).apply {
                    border = JBUI.Borders.empty(5)
                    add(JLabel("Preview:"), BorderLayout.NORTH)
                    add(JScrollPane(promptPreview), BorderLayout.CENTER)
                }
                
                val buttonPanel = JPanel().apply {
                    add(favoriteButton)
                }
                
                add(previewPanel, BorderLayout.CENTER)
                add(buttonPanel, BorderLayout.SOUTH)
            }
            
            setDividerLocation(400)
        }
        
        panel.add(topPanel, BorderLayout.NORTH)
        panel.add(centerPanel, BorderLayout.CENTER)
        
        return panel
    }
    
    private fun setupUI() {
        // Setup category combo
        categoryCombo.addItem(null) // "All categories"
        PromptCategory.values().forEach { categoryCombo.addItem(it) }
        
        // Setup search field
        searchField.addActionListener { filterPrompts() }
        
        // Setup category combo
        categoryCombo.addActionListener { filterPrompts() }
        
        // Setup table
        promptTable.selectionModel.addListSelectionListener { updatePreview() }
        promptTable.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (e.clickCount == 2) {
                    sendPromptToCopilot()
                }
            }
        })
        
        // Setup preview
        promptPreview.isEditable = false
        promptPreview.lineWrap = true
        promptPreview.wrapStyleWord = true
        
        // Setup favorite button
        favoriteButton.addActionListener { toggleFavorite() }
    }
    
    private fun updateTable() {
        promptTable.model = PromptTableModel(filteredPrompts)
        if (filteredPrompts.isNotEmpty()) {
            promptTable.setRowSelectionInterval(0, 0)
            updatePreview()
        }
    }
    
    private fun filterPrompts() {
        val searchQuery = searchField.text.trim()
        val selectedCategory = categoryCombo.selectedItem as? PromptCategory
        
        filteredPrompts = promptService.getAllPrompts().filter { prompt ->
            val matchesSearch = searchQuery.isEmpty() || 
                prompt.title.contains(searchQuery, ignoreCase = true) ||
                prompt.description.contains(searchQuery, ignoreCase = true) ||
                prompt.prompt.contains(searchQuery, ignoreCase = true)
            
            val matchesCategory = selectedCategory == null || prompt.category == selectedCategory
            
            matchesSearch && matchesCategory
        }
        
        updateTable()
    }
    
    private fun updatePreview() {
        val selectedRow = promptTable.selectedRow
        if (selectedRow >= 0 && selectedRow < filteredPrompts.size) {
            val prompt = filteredPrompts[selectedRow]
            promptPreview.text = buildString {
                appendLine("Title: ${prompt.title}")
                appendLine("Category: ${prompt.category.displayName}")
                appendLine("Description: ${prompt.description}")
                appendLine()
                appendLine("Prompt:")
                appendLine(prompt.prompt)
                
                if (!selectedText.isNullOrBlank()) {
                    appendLine()
                    appendLine("--- Selected Code ---")
                    appendLine(selectedText)
                }
            }
            
            favoriteButton.text = if (prompt.isFavorite) "★" else "☆"
        } else {
            promptPreview.text = ""
            favoriteButton.text = "☆"
        }
    }
    
    private fun toggleFavorite() {
        val selectedRow = promptTable.selectedRow
        if (selectedRow >= 0 && selectedRow < filteredPrompts.size) {
            val prompt = filteredPrompts[selectedRow]
            promptService.toggleFavorite(prompt.id)
            filterPrompts() // Refresh to update favorite status
        }
    }
    
    private fun sendPromptToCopilot() {
        val selectedRow = promptTable.selectedRow
        if (selectedRow >= 0 && selectedRow < filteredPrompts.size) {
            val prompt = filteredPrompts[selectedRow]
            CopilotIntegration.sendPrompt(project, prompt, selectedText)
            close(OK_EXIT_CODE)
        }
    }
    
    override fun createActions(): Array<Action> {
        return arrayOf(
            object : DialogWrapperAction("Send to Copilot") {
                override fun doAction(e: java.awt.event.ActionEvent?) {
                    sendPromptToCopilot()
                }
            },
            cancelAction
        )
    }
    
    private class PromptTableModel(private val prompts: List<PromptTemplate>) : AbstractTableModel() {
        private val columns = arrayOf("★", "Title", "Category", "Description")
        
        override fun getRowCount(): Int = prompts.size
        override fun getColumnCount(): Int = columns.size
        override fun getColumnName(column: Int): String = columns[column]
        
        override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
            val prompt = prompts[rowIndex]
            return when (columnIndex) {
                0 -> if (prompt.isFavorite) "★" else ""
                1 -> prompt.title
                2 -> prompt.category.displayName
                3 -> prompt.description
                else -> ""
            }
        }
        
        override fun getColumnClass(columnIndex: Int): Class<*> = String::class.java
    }
}