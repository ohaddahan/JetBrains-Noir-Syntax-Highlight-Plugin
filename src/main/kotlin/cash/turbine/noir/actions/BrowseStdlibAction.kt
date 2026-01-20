package cash.turbine.noir.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem
import cash.turbine.noir.stdlib.NoirStdlibFileSystem
import java.io.File

class BrowseStdlibAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val stdlibRoot = NoirStdlibFileSystem.findStdlibRoot()
        if (stdlibRoot == null) {
            Messages.showWarningDialog(
                project,
                "Could not find Noir standard library.\n\nMake sure nargo is installed and in your PATH.",
                "Stdlib Not Found"
            )
            return
        }

        val libFile = File(stdlibRoot, "lib.nr")
        if (libFile.exists()) {
            openFile(project, libFile)
        } else {
            val stdlibDir = File(stdlibRoot)
            val firstNrFile = stdlibDir.listFiles()?.firstOrNull { it.extension == "nr" }
            if (firstNrFile != null) {
                openFile(project, firstNrFile)
            } else {
                Messages.showInfoMessage(
                    project,
                    "Stdlib found at: $stdlibRoot",
                    "Noir Standard Library"
                )
            }
        }
    }

    private fun openFile(project: Project, file: File) {
        val virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file)
        if (virtualFile != null) {
            FileEditorManager.getInstance(project).openFile(virtualFile, true)
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }
}
