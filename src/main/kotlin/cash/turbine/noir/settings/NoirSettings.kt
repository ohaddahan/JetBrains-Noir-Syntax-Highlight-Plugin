package cash.turbine.noir.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@Service(Service.Level.APP)
@State(
    name = "NoirSettings",
    storages = [Storage("NoirSettings.xml")]
)
class NoirSettings : PersistentStateComponent<NoirSettings> {
    var nargoPath: String = ""
    var enableLsp: Boolean = true
    var lightweightMode: Boolean = false

    override fun getState(): NoirSettings = this

    override fun loadState(state: NoirSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): NoirSettings =
            ApplicationManager.getApplication().getService(NoirSettings::class.java)
    }
}
