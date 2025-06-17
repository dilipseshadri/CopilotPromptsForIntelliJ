package com.github.copilot.prompts.compat

/**
 * Compatibility layer for IntelliJ Platform SDK
 * Provides utility functions that work both with and without IntelliJ SDK
 */

// Check if IntelliJ Platform SDK is available
val isIntelliJAvailable: Boolean by lazy {
    try {
        Class.forName("com.intellij.openapi.project.Project")
        true
    } catch (e: ClassNotFoundException) {
        false
    }
}

// Utility functions for compatibility
object IntelliJCompat {
    
    fun showNotification(title: String, content: String, type: String = "INFORMATION") {
        if (isIntelliJAvailable) {
            try {
                // Use real IntelliJ notification system
                val notificationTypeClass = Class.forName("com.intellij.notification.NotificationType")
                val notificationType = when (type) {
                    "ERROR" -> notificationTypeClass.getField("ERROR").get(null)
                    "WARNING" -> notificationTypeClass.getField("WARNING").get(null)
                    else -> notificationTypeClass.getField("INFORMATION").get(null)
                }
                
                val notificationClass = Class.forName("com.intellij.notification.Notification")
                val notification = notificationClass.getConstructor(
                    String::class.java,
                    String::class.java,
                    String::class.java,
                    notificationTypeClass
                ).newInstance("CopilotPrompts", title, content, notificationType)
                
                val notificationsClass = Class.forName("com.intellij.notification.Notifications")
                val busField = notificationsClass.getField("Bus")
                val bus = busField.get(null)
                val notifyMethod = bus.javaClass.getMethod("notify", notificationClass)
                notifyMethod.invoke(bus, notification)
            } catch (e: Exception) {
                // Fallback to simple dialog
                javax.swing.JOptionPane.showMessageDialog(null, content, title, javax.swing.JOptionPane.INFORMATION_MESSAGE)
            }
        } else {
            // Use fallback notification
            javax.swing.JOptionPane.showMessageDialog(null, content, title, javax.swing.JOptionPane.INFORMATION_MESSAGE)
        }
    }
    
    fun copyToClipboard(text: String) {
        if (isIntelliJAvailable) {
            try {
                // Use IntelliJ clipboard manager
                val transferable = java.awt.datatransfer.StringSelection(text)
                val copyPasteManagerClass = Class.forName("com.intellij.ide.CopyPasteManager")
                val getInstance = copyPasteManagerClass.getMethod("getInstance")
                val manager = getInstance.invoke(null)
                val setContents = copyPasteManagerClass.getMethod("setContents", java.awt.datatransfer.Transferable::class.java)
                setContents.invoke(manager, transferable)
            } catch (e: Exception) {
                // Fallback to system clipboard
                copyToSystemClipboard(text)
            }
        } else {
            copyToSystemClipboard(text)
        }
    }
    
    private fun copyToSystemClipboard(text: String) {
        val transferable = java.awt.datatransfer.StringSelection(text)
        val clipboard = java.awt.Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(transferable, null)
    }
    
    fun createEmptyBorder(size: Int): javax.swing.border.Border {
        return if (isIntelliJAvailable) {
            try {
                val jbuiClass = Class.forName("com.intellij.util.ui.JBUI")
                val bordersClass = Class.forName("com.intellij.util.ui.JBUI\$Borders")
                val bordersField = jbuiClass.getField("Borders")
                val borders = bordersField.get(null)
                val emptyMethod = bordersClass.getMethod("empty", Int::class.java)
                emptyMethod.invoke(borders, size) as javax.swing.border.Border
            } catch (e: Exception) {
                javax.swing.BorderFactory.createEmptyBorder(size, size, size, size)
            }
        } else {
            javax.swing.BorderFactory.createEmptyBorder(size, size, size, size)
        }
    }
    
    fun createEmptyBorder(top: Int, left: Int, bottom: Int, right: Int): javax.swing.border.Border {
        return if (isIntelliJAvailable) {
            try {
                val jbuiClass = Class.forName("com.intellij.util.ui.JBUI")
                val bordersClass = Class.forName("com.intellij.util.ui.JBUI\$Borders")
                val bordersField = jbuiClass.getField("Borders")
                val borders = bordersField.get(null)
                val emptyMethod = bordersClass.getMethod("empty", Int::class.java, Int::class.java, Int::class.java, Int::class.java)
                emptyMethod.invoke(borders, top, left, bottom, right) as javax.swing.border.Border
            } catch (e: Exception) {
                javax.swing.BorderFactory.createEmptyBorder(top, left, bottom, right)
            }
        } else {
            javax.swing.BorderFactory.createEmptyBorder(top, left, bottom, right)
        }
    }
}