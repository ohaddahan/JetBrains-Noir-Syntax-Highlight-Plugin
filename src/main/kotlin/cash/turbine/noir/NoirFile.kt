package cash.turbine.noir

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class NoirFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, NoirLanguage) {
    override fun getFileType(): FileType = NoirFileType
    override fun toString(): String = "Noir File"
}
