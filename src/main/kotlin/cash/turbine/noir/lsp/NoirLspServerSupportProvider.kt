package cash.turbine.noir.lsp

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerSupportProvider
import cash.turbine.noir.settings.NoirSettings

class NoirLspServerSupportProvider : LspServerSupportProvider {
    override fun fileOpened(
        project: Project,
        file: VirtualFile,
        serverStarter: LspServerSupportProvider.LspServerStarter
    ) {
        val settings = NoirSettings.getInstance()

        if (!settings.enableLsp) {
            return
        }

        if (file.extension == "nr") {
            serverStarter.ensureServerStarted(NoirLspServerDescriptor(project))
        }
    }
}
