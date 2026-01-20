package cash.turbine.noir.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.platform.lsp.api.LspServerManager
import cash.turbine.noir.lsp.NoirLspServerSupportProvider

class RestartLspAction : AnAction("Restart Noir Language Server") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        LspServerManager.getInstance(project).stopAndRestartIfNeeded(
            NoirLspServerSupportProvider::class.java
        )
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }
}
