package com.github.copilot.prompts

import com.github.copilot.prompts.model.DefaultPrompts
import com.github.copilot.prompts.model.PromptTemplate
import com.github.copilot.prompts.service.PromptService
import com.github.copilot.prompts.compat.IntelliJCompat
import com.github.copilot.prompts.compat.isIntelliJAvailable
import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent

/**
 * Standalone main class for testing the plugin offline
 * This allows testing the core functionality without IntelliJ IDEA
 */
class OfflineMain {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("CopilotPromptsForIntelliJ - Offline Mode")
            println("IntelliJ SDK Available: ${isIntelliJAvailable}")
            
            // Set look and feel (using default for now)
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel())
            
            SwingUtilities.invokeLater {
                createAndShowGUI()
            }
        }
        
        private fun createAndShowGUI() {
            val frame = JFrame("Copilot Prompts - Offline Demo")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.size = Dimension(800, 600)
            frame.setLocationRelativeTo(null)
            
            val mainPanel = JPanel(BorderLayout())
            
            // Create header
            val headerPanel = JPanel(FlowLayout())
            headerPanel.add(JLabel("Copilot Prompts - Offline Demo"))
            headerPanel.border = IntelliJCompat.createEmptyBorder(10)
            mainPanel.add(headerPanel, BorderLayout.NORTH)
            
            // Create content area
            val contentPanel = createContentPanel()
            mainPanel.add(contentPanel, BorderLayout.CENTER)
            
            // Create footer with actions
            val footerPanel = createFooterPanel()
            mainPanel.add(footerPanel, BorderLayout.SOUTH)
            
            frame.add(mainPanel)
            frame.isVisible = true
        }
        
        private fun createContentPanel(): JComponent {
            val panel = JPanel(BorderLayout())
            panel.border = IntelliJCompat.createEmptyBorder(10)
            
            // Create prompt list
            val promptService = PromptService()
            val prompts = DefaultPrompts.templates
            
            val listModel = DefaultListModel<String>()
            prompts.forEach { prompt: PromptTemplate ->
                listModel.addElement("${prompt.category.displayName}: ${prompt.title}")
            }
            
            val promptList = JList(listModel)
            promptList.selectionMode = ListSelectionModel.SINGLE_SELECTION
            
            val scrollPane = JScrollPane(promptList)
            scrollPane.preferredSize = Dimension(300, 400)
            
            // Create detail area
            val detailArea = JTextArea()
            detailArea.isEditable = false
            detailArea.lineWrap = true
            detailArea.wrapStyleWord = true
            detailArea.text = "Select a prompt to see details..."
            
            val detailScrollPane = JScrollPane(detailArea)
            detailScrollPane.preferredSize = Dimension(450, 400)
            
            // Add selection listener
            promptList.addListSelectionListener { e ->
                if (!e.valueIsAdjusting) {
                    val selectedIndex = promptList.selectedIndex
                    if (selectedIndex >= 0 && selectedIndex < prompts.size) {
                        val prompt = prompts[selectedIndex]
                        detailArea.text = """
                            Title: ${prompt.title}
                            Category: ${prompt.category.displayName}
                            
                            Prompt:
                            ${prompt.prompt}
                            
                            Description: ${prompt.description}
                        """.trimIndent()
                    }
                }
            }
            
            // Create split pane
            val splitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, detailScrollPane)
            splitPane.dividerLocation = 300
            
            panel.add(splitPane, BorderLayout.CENTER)
            
            return panel
        }
        
        private fun createFooterPanel(): JComponent {
            val panel = JPanel(FlowLayout())
            panel.border = IntelliJCompat.createEmptyBorder(10)
            
            val testNotificationButton = JButton("Test Notification")
            testNotificationButton.addActionListener { e ->
                IntelliJCompat.showNotification(
                    "Test Notification",
                    "This is a test notification from the offline demo!",
                    "INFORMATION"
                )
            }
            
            val testClipboardButton = JButton("Test Clipboard")
            testClipboardButton.addActionListener { e ->
                val testText = "This text was copied from Copilot Prompts offline demo!"
                IntelliJCompat.copyToClipboard(testText)
                IntelliJCompat.showNotification(
                    "Clipboard Test",
                    "Text copied to clipboard: $testText",
                    "INFORMATION"
                )
            }
            
            val exitButton = JButton("Exit")
            exitButton.addActionListener { e ->
                System.exit(0)
            }
            
            panel.add(testNotificationButton)
            panel.add(testClipboardButton)
            panel.add(exitButton)
            
            return panel
        }
    }
}