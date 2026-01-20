package cash.turbine.noir

import com.intellij.lang.Language

object NoirLanguage : Language("Noir") {
    private fun readResolve(): Any = NoirLanguage

    override fun getDisplayName(): String = "Noir"

    override fun isCaseSensitive(): Boolean = true
}
