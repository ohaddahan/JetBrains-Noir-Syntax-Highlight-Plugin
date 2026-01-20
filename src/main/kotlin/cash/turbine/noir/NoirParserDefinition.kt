package cash.turbine.noir

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class NoirParserDefinition : ParserDefinition {

    override fun createLexer(project: Project?): Lexer = NoirLexer()

    override fun createParser(project: Project?): PsiParser = NoirParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = STRINGS

    override fun createElement(node: ASTNode): PsiElement = NoirPsiElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = NoirFile(viewProvider)

    companion object {
        val FILE = IFileElementType(NoirLanguage)
        val COMMENTS = TokenSet.create(NoirTokenTypes.LINE_COMMENT, NoirTokenTypes.BLOCK_COMMENT)
        val STRINGS = TokenSet.create(NoirTokenTypes.STRING)
    }
}
