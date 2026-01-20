package cash.turbine.noir.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@Service(Service.Level.PROJECT)
@State(
    name = "NoirProjectSettings",
    storages = [Storage(StoragePathMacros.WORKSPACE_FILE)]
)
class NoirProjectSettings : PersistentStateComponent<NoirProjectSettings> {
    var nargoPathOverride: String = ""

    override fun getState(): NoirProjectSettings = this

    override fun loadState(state: NoirProjectSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    fun getEffectiveNargoPath(): String {
        return nargoPathOverride.ifBlank { NoirSettings.getInstance().nargoPath }
    }

    companion object {
        fun getInstance(project: Project): NoirProjectSettings =
            project.getService(NoirProjectSettings::class.java)
    }
}
