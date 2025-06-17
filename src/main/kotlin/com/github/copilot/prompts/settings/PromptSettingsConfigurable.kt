package com.github.copilot.prompts.settings

import com.github.copilot.prompts.model.PromptCategory
import com.github.copilot.prompts.model.PromptTemplate
import com.github.copilot.prompts.service.PromptService
import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.*
import com.intellij.ui.table.JBTable
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.Dimension
import java.util.*
import javax.swing.*
import javax.swing.table.AbstractTableModel

class PromptSettingsConfigurable : Configurable {
    
    private val promptService = PromptService.getInstance()
    private var customPrompts = mutableListOf<PromptTemplate>()
    private lateinit var promptTable: JBTable
    private lateinit var addButton: JButton
    private lateinit var editButton: JButton
    private lateinit var deleteButton: JButton
    
    override fun getDisplayName(): String = "Copilot Prompts"
    
    override fun createComponent(): JComponent {
        val panel = JPanel(BorderLayout())
        
        // Load current custom prompts
        customPrompts = promptService.getAllPrompts()
            .filter { it.isCustom }
            .toMutableList()
        
        // Create table
        promptTable = JBTable(CustomPromptTableModel(customPrompts))
        promptTable.selectionModel.addListSelectionListener { updateButtons() }
        
        val scrollPane = JScrollPane(promptTable).apply {
            preferredSize = Dimension(600, 300)
        }
        
        // Create buttons
        addButton = JButton("Add").apply {
            addActionListener { addPrompt() }
        }
        
        editButton = JButton("Edit").apply {
            addActionListener { editPrompt() }
            isEnabled = false
        }
        
        deleteButton = JButton("Delete").apply {
            addActionListener { deletePrompt() }
            isEnabled = false
        }
        
        val buttonPanel = JPanel().apply {
            add(addButton)
            add(editButton)
            add(deleteButton)
        }
        
        panel.add(JLabel("Custom Prompts:"), BorderLayout.NORTH)
        panel.add(scrollPane, BorderLayout.CENTER)
        panel.add(buttonPanel, BorderLayout.SOUTH)
        
        return panel
    }
    
    private fun updateButtons() {
        val hasSelection = promptTable.selectedRow >= 0
        editButton.isEnabled = hasSelection
        deleteButton.isEnabled = hasSelection
    }
    
    private fun addPrompt() {
        val dialog = PromptEditDialog(null)
        if (dialog.showAndGet()) {
            val newPrompt = dialog.getPrompt()
            if (newPrompt != null) {
                customPrompts.add(newPrompt)
                (promptTable.model as CustomPromptTableModel).fireTableDataChanged()
            }
        }
    }
    
    private fun editPrompt() {
        val selectedRow = promptTable.selectedRow
        if (selectedRow >= 0 && selectedRow < customPrompts.size) {
            val prompt = customPrompts[selectedRow]
            val dialog = PromptEditDialog(prompt)
            if (dialog.showAndGet()) {
                val editedPrompt = dialog.getPrompt()
                if (editedPrompt != null) {
                    customPrompts[selectedRow] = editedPrompt
                    (promptTable.model as CustomPromptTableModel).fireTableDataChanged()
                }
            }
        }
    }
    
    private fun deletePrompt() {
        val selectedRow = promptTable.selectedRow
        if (selectedRow >= 0 && selectedRow < customPrompts.size) {
            val result = JOptionPane.showConfirmDialog(
                promptTable,
                "Are you sure you want to delete this prompt?",
                "Delete Prompt",
                JOptionPane.YES_NO_OPTION
            )
            
            if (result == JOptionPane.YES_OPTION) {
                customPrompts.removeAt(selectedRow)
                (promptTable.model as CustomPromptTableModel).fireTableDataChanged()
            }
        }
    }
    
    override fun isModified(): Boolean {
        val currentCustomPrompts = promptService.getAllPrompts().filter { it.isCustom }
        return customPrompts != currentCustomPrompts
    }
    
    override fun apply() {
        // Remove all existing custom prompts
        val existingCustomPrompts = promptService.getAllPrompts().filter { it.isCustom }
        existingCustomPrompts.forEach { promptService.removeCustomPrompt(it.id) }
        
        // Add new custom prompts
        customPrompts.forEach { promptService.addCustomPrompt(it) }
    }
    
    override fun reset() {
        customPrompts = promptService.getAllPrompts()
            .filter { it.isCustom }
            .toMutableList()
        (promptTable.model as CustomPromptTableModel).fireTableDataChanged()
    }
    
    private class CustomPromptTableModel(
        private val prompts: MutableList<PromptTemplate>
    ) : AbstractTableModel() {
        
        private val columns = arrayOf("Title", "Category", "Description")
        
        override fun getRowCount(): Int = prompts.size
        override fun getColumnCount(): Int = columns.size
        override fun getColumnName(column: Int): String = columns[column]
        
        override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
            val prompt = prompts[rowIndex]
            return when (columnIndex) {
                0 -> prompt.title
                1 -> prompt.category.displayName
                2 -> prompt.description
                else -> ""
            }
        }
    }
    
    private class PromptEditDialog(private val existingPrompt: PromptTemplate?) : JDialog() {
        
        private val titleField = JBTextField(existingPrompt?.title ?: "")
        private val descriptionField = JBTextField(existingPrompt?.description ?: "")
        private val categoryCombo = JComboBox(PromptCategory.values())
        private val promptArea = JBTextArea(existingPrompt?.prompt ?: "")
        private var result: PromptTemplate? = null
        
        init {
            title = if (existingPrompt != null) "Edit Prompt" else "Add Prompt"
            isModal = true
            setupUI()
            pack()
            setLocationRelativeTo(null)
            
            if (existingPrompt != null) {
                categoryCombo.selectedItem = existingPrompt.category
            }
        }
        
        private fun setupUI() {
            layout = BorderLayout()
            
            val formPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                border = JBUI.Borders.empty(10)
                
                add(createFieldPanel("Title:", titleField))
                add(createFieldPanel("Description:", descriptionField))
                add(createFieldPanel("Category:", categoryCombo))
                
                add(JLabel("Prompt:"))
                promptArea.rows = 10
                promptArea.lineWrap = true
                promptArea.wrapStyleWord = true
                add(JScrollPane(promptArea))
            }
            
            val buttonPanel = JPanel().apply {
                val okButton = JButton("OK").apply {
                    addActionListener { 
                        if (validateAndSave()) {
                            dispose()
                        }
                    }
                }
                
                val cancelButton = JButton("Cancel").apply {
                    addActionListener { dispose() }
                }
                
                add(okButton)
                add(cancelButton)
            }
            
            add(formPanel, BorderLayout.CENTER)
            add(buttonPanel, BorderLayout.SOUTH)
        }
        
        private fun createFieldPanel(label: String, component: JComponent): JPanel {
            return JPanel(BorderLayout()).apply {
                border = JBUI.Borders.empty(5, 0)
                add(JLabel(label), BorderLayout.WEST)
                add(component, BorderLayout.CENTER)
            }
        }
        
        private fun validateAndSave(): Boolean {
            val title = titleField.text.trim()
            val description = descriptionField.text.trim()
            val prompt = promptArea.text.trim()
            val category = categoryCombo.selectedItem as PromptCategory
            
            if (title.isEmpty() || description.isEmpty() || prompt.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
                )
                return false
            }
            
            val id = existingPrompt?.id ?: UUID.randomUUID().toString()
            
            result = PromptTemplate(
                id = id,
                title = title,
                description = description,
                prompt = prompt,
                category = category,
                isCustom = true
            )
            
            return true
        }
        
        fun showAndGet(): Boolean {
            isVisible = true
            return result != null
        }
        
        fun getPrompt(): PromptTemplate? = result
    }
}