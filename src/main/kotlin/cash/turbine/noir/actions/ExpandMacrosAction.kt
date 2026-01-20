package cash.turbine.noir.actions

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.ide.scratch.ScratchFileService
import com.intellij.ide.scratch.ScratchRootType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.util.Key
import cash.turbine.noir.NoirLanguage
import cash.turbine.noir.settings.NoirProjectSettings
import java.io.File

class ExpandMacrosAction : AnAction("Expand Macros") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        if (file.extension != "nr") {
            return
        }

        val projectSettings = NoirProjectSettings.getInstance(project)
        val nargoPath = projectSettings.getEffectiveNargoPath().ifBlank { "nargo" }

        ProgressManager.getInstance().run(object : Task.Backgroundable(project, "Expanding Macros...", true) {
            override fun run(indicator: ProgressIndicator) {
                try {
                    val commandLine = GeneralCommandLine(nargoPath, "expand")
                        .withWorkDirectory(File(project.basePath ?: "."))
                        .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)

                    val output = StringBuilder()
                    val processHandler = OSProcessHandler(commandLine)

                    processHandler.addProcessListener(object : ProcessAdapter() {
                        override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                            output.append(event.text)
                        }
                    })

                    processHandler.startNotify()
                    processHandler.waitFor()

                    val expandedCode = output.toString()

                    ApplicationManager.getApplication().invokeLater {
                        val scratchFile = ScratchRootType.getInstance().createScratchFile(
                            project,
                            "expanded_${file.nameWithoutExtension}.nr",
                            NoirLanguage,
                            expandedCode
                        )

                        if (scratchFile != null) {
                            FileEditorManager.getInstance(project).openFile(scratchFile, true)
                        }
                    }
                } catch (ex: Exception) {
                    LOG.warn("Failed to expand macros", ex)
                }
            }
        })
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabledAndVisible = e.project != null && file?.extension == "nr"
    }

    companion object {
        private val LOG = Logger.getInstance(ExpandMacrosAction::class.java)
    }
}
