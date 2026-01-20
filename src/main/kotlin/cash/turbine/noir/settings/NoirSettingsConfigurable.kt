package cash.turbine.noir.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class NoirSettingsConfigurable : Configurable {
    private var nargoPathField: TextFieldWithBrowseButton? = null
    private var enableLspCheckBox: JBCheckBox? = null
    private var lightweightModeCheckBox: JBCheckBox? = null

    override fun getDisplayName(): String = "Noir"

    override fun createComponent(): JComponent {
        val settings = NoirSettings.getInstance()

        nargoPathField = TextFieldWithBrowseButton().apply {
            addBrowseFolderListener(
                "Select Nargo Executable",
                "Select the path to the nargo executable",
                null,
                FileChooserDescriptorFactory.createSingleFileDescriptor()
            )
            text = settings.nargoPath
        }

        enableLspCheckBox = JBCheckBox("Enable Language Server", settings.enableLsp)
        lightweightModeCheckBox = JBCheckBox("Lightweight Mode (disables completions, signature help, code actions)", settings.lightweightMode)

        return panel {
            group("Nargo Configuration") {
                row("Nargo path:") {
                    cell(nargoPathField!!)
                        .comment("Leave empty to auto-detect from PATH")
                }
            }
            group("Language Server") {
                row {
                    cell(enableLspCheckBox!!)
                }
                row {
                    cell(lightweightModeCheckBox!!)
                }
            }
        }
    }

    override fun isModified(): Boolean {
        val settings = NoirSettings.getInstance()
        return nargoPathField?.text != settings.nargoPath ||
                enableLspCheckBox?.isSelected != settings.enableLsp ||
                lightweightModeCheckBox?.isSelected != settings.lightweightMode
    }

    override fun apply() {
        val settings = NoirSettings.getInstance()
        settings.nargoPath = nargoPathField?.text ?: ""
        settings.enableLsp = enableLspCheckBox?.isSelected ?: true
        settings.lightweightMode = lightweightModeCheckBox?.isSelected ?: false
    }

    override fun reset() {
        val settings = NoirSettings.getInstance()
        nargoPathField?.text = settings.nargoPath
        enableLspCheckBox?.isSelected = settings.enableLsp
        lightweightModeCheckBox?.isSelected = settings.lightweightMode
    }

    override fun disposeUIResources() {
        nargoPathField = null
        enableLspCheckBox = null
        lightweightModeCheckBox = null
    }
}
