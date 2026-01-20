package cash.turbine.noir

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object NoirFileType : LanguageFileType(NoirLanguage) {
    override fun getName(): String = "Noir"

    override fun getDescription(): String = "Noir source file"

    override fun getDefaultExtension(): String = "nr"

    override fun getIcon(): Icon = NoirIcons.FILE
}
