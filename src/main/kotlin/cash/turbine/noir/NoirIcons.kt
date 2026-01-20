package cash.turbine.noir

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object NoirIcons {
    @JvmField
    val FILE: Icon = IconLoader.getIcon("/icons/noir.svg", NoirIcons::class.java)

    @JvmField
    val TEST: Icon = IconLoader.getIcon("/icons/test.svg", NoirIcons::class.java)
}
