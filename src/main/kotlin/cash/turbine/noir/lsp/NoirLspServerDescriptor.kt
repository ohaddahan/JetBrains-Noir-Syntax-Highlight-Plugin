package cash.turbine.noir.lsp

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor
import cash.turbine.noir.settings.NoirProjectSettings
import java.io.File

class NoirLspServerDescriptor(project: Project) : ProjectWideLspServerDescriptor(project, "Noir") {

    override fun isSupportedFile(file: VirtualFile): Boolean {
        return file.extension == "nr"
    }

    override fun createCommandLine(): GeneralCommandLine {
        val nargoPath = findNargoPath()
        LOG.info("Starting Noir LSP with nargo at: $nargoPath")

        return GeneralCommandLine(nargoPath, "lsp").apply {
            withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            workDirectory = File(project.basePath ?: ".")
        }
    }

    private fun findNargoPath(): String {
        val projectSettings = NoirProjectSettings.getInstance(project)
        val effectivePath = projectSettings.getEffectiveNargoPath()

        if (effectivePath.isNotBlank()) {
            return effectivePath
        }

        return findNargoInPath() ?: "nargo"
    }

    private fun findNargoInPath(): String? {
        val pathEnv = System.getenv("PATH") ?: return null
        val pathSeparator = File.pathSeparator
        val executableName = if (System.getProperty("os.name").lowercase().contains("win")) "nargo.exe" else "nargo"

        for (dir in pathEnv.split(pathSeparator)) {
            val file = File(dir, executableName)
            if (file.exists() && file.canExecute()) {
                return file.absolutePath
            }
        }
        return null
    }

    companion object {
        private val LOG = Logger.getInstance(NoirLspServerDescriptor::class.java)
    }
}
