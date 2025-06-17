package com.github.copilot.prompts.fallback

import javax.swing.*
import java.awt.Component
import java.awt.event.ActionEvent

/**
 * Fallback implementations for IntelliJ Platform SDK classes
 * These are used when the plugin is built without IntelliJ dependencies
 * for offline compilation or testing purposes.
 */

// Project stub
open class Project {
    companion object {
        fun getDefault(): Project = Project()
    }
}

// Action stub
abstract class AnAction(text: String? = null) {
    abstract fun actionPerformed(e: ActionEvent)
    
    open fun update(e: ActionEvent) {
        // Default implementation
    }
}

// Dialog wrapper stub
abstract class DialogWrapper(project: Project?, canBeParent: Boolean = true) : JDialog() {
    companion object {
        const val OK_EXIT_CODE = 0
        const val CANCEL_EXIT_CODE = 1
    }
    
    protected abstract fun createCenterPanel(): JComponent?
    
    protected open fun createActions(): Array<Action> {
        return arrayOf(
            object : AbstractAction("OK") {
                override fun actionPerformed(e: ActionEvent) {
                    close(OK_EXIT_CODE)
                }
            },
            object : AbstractAction("Cancel") {
                override fun actionPerformed(e: ActionEvent) {
                    close(CANCEL_EXIT_CODE)
                }
            }
        )
    }
    
    protected fun close(exitCode: Int) {
        dispose()
    }
    
    fun init() {
        val centerPanel = createCenterPanel()
        if (centerPanel != null) {
            add(centerPanel)
        }
        pack()
    }
    
    abstract class DialogWrapperAction(name: String) : AbstractAction(name) {
        protected abstract fun doAction(e: ActionEvent?)
        
        override fun actionPerformed(e: ActionEvent) {
            doAction(e)
        }
    }
}

// Configurable stub
interface Configurable {
    fun getDisplayName(): String
    fun createComponent(): JComponent?
    fun isModified(): Boolean
    fun apply()
    fun reset()
}

// Tool window factory stub
interface ToolWindowFactory {
    fun createToolWindowContent(project: Project, toolWindow: ToolWindow)
}

// Tool window stub
class ToolWindow {
    private val contentManager = ContentManager()
    
    fun getContentManager(): ContentManager = contentManager
}

// Content manager stub
class ContentManager {
    fun addContent(content: Content) {
        // Stub implementation
    }
}

// Content stub
class Content(component: JComponent, displayName: String, isPinned: Boolean) {
    companion object {
        fun getInstance(): ContentFactory = ContentFactory()
    }
}

// Content factory stub
class ContentFactory {
    companion object {
        fun getInstance(): ContentFactory = ContentFactory()
    }
    
    fun createContent(component: JComponent, displayName: String, isPinned: Boolean): Content {
        return Content(component, displayName, isPinned)
    }
}

// UI components stubs
class JBTextField(text: String = "") : JTextField(text)
class JBTextArea(text: String = "") : JTextArea(text)
class JBList<T> : JList<T>()
class JBTable : JTable()

// Popup stubs
object JBPopupFactory {
    fun getInstance(): JBPopupFactory = this
    
    fun createListPopup(step: PopupStep<*>): ListPopup {
        return ListPopup()
    }
}

interface PopupStep<T>
abstract class BaseListPopupStep<T>(title: String, values: List<T>) : PopupStep<T>

class ListPopup {
    fun showInBestPositionFor(editor: Any) {
        // Stub implementation
    }
}

// Editor stub
class Editor

// Notification stubs
object Notifications {
    fun Bus(): NotificationBus = NotificationBus()
}

class NotificationBus {
    fun notify(notification: Notification) {
        // Stub implementation - could show a JOptionPane
        JOptionPane.showMessageDialog(null, notification.content, notification.title, JOptionPane.INFORMATION_MESSAGE)
    }
}

class Notification(
    val groupId: String,
    val title: String,
    val content: String,
    val type: NotificationType
)

enum class NotificationType {
    INFORMATION, WARNING, ERROR
}

// Clipboard stub
object CopyPasteManager {
    fun getInstance(): CopyPasteManager = this
    
    fun setContents(transferable: java.awt.datatransfer.Transferable) {
        val clipboard = java.awt.Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(transferable, null)
    }
}

// JBUI stub
object JBUI {
    object Borders {
        fun empty(size: Int = 0): javax.swing.border.Border {
            return javax.swing.BorderFactory.createEmptyBorder(size, size, size, size)
        }
        
        fun empty(top: Int, left: Int, bottom: Int, right: Int): javax.swing.border.Border {
            return javax.swing.BorderFactory.createEmptyBorder(top, left, bottom, right)
        }
    }
    
    fun size(width: Int, height: Int): java.awt.Dimension {
        return java.awt.Dimension(width, height)
    }
    
    fun insets(top: Int, left: Int = top, bottom: Int = top, right: Int = left): java.awt.Insets {
        return java.awt.Insets(top, left, bottom, right)
    }
}

// Persistent state component stub
interface PersistentStateComponent<T> {
    fun getState(): T?
    fun loadState(state: T)
}

// Service manager stub
object ServiceManager {
    fun <T> getService(serviceClass: Class<T>): T? {
        return null
    }
}

// Application manager stub
object ApplicationManager {
    fun getApplication(): Application = Application()
}

class Application {
    fun <T> getService(serviceClass: Class<T>): T? {
        return null
    }
}