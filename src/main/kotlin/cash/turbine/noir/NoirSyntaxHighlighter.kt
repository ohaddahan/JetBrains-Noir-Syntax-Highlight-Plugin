package cash.turbine.noir

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

class NoirSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer = NoirLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return when (tokenType) {
            NoirTokenTypes.KEYWORD -> KEYWORD_KEYS
            NoirTokenTypes.TYPE -> TYPE_KEYS
            NoirTokenTypes.IDENTIFIER -> IDENTIFIER_KEYS
            NoirTokenTypes.STRING -> STRING_KEYS
            NoirTokenTypes.NUMBER -> NUMBER_KEYS
            NoirTokenTypes.BOOLEAN -> KEYWORD_KEYS
            NoirTokenTypes.LINE_COMMENT -> COMMENT_KEYS
            NoirTokenTypes.BLOCK_COMMENT -> COMMENT_KEYS
            NoirTokenTypes.OPERATOR -> OPERATOR_KEYS
            NoirTokenTypes.BRACE -> BRACE_KEYS
            NoirTokenTypes.ATTRIBUTE -> ATTRIBUTE_KEYS
            NoirTokenTypes.BAD_CHARACTER -> BAD_CHARACTER_KEYS
            else -> EMPTY_KEYS
        }
    }

    companion object {
        val KEYWORD = createTextAttributesKey("NOIR_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val TYPE = createTextAttributesKey("NOIR_TYPE", DefaultLanguageHighlighterColors.CLASS_NAME)
        val IDENTIFIER = createTextAttributesKey("NOIR_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)
        val STRING = createTextAttributesKey("NOIR_STRING", DefaultLanguageHighlighterColors.STRING)
        val NUMBER = createTextAttributesKey("NOIR_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val COMMENT = createTextAttributesKey("NOIR_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val OPERATOR = createTextAttributesKey("NOIR_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val BRACE = createTextAttributesKey("NOIR_BRACE", DefaultLanguageHighlighterColors.BRACES)
        val ATTRIBUTE = createTextAttributesKey("NOIR_ATTRIBUTE", DefaultLanguageHighlighterColors.METADATA)
        val BAD_CHARACTER = createTextAttributesKey("NOIR_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)

        private val KEYWORD_KEYS = arrayOf(KEYWORD)
        private val TYPE_KEYS = arrayOf(TYPE)
        private val IDENTIFIER_KEYS = arrayOf(IDENTIFIER)
        private val STRING_KEYS = arrayOf(STRING)
        private val NUMBER_KEYS = arrayOf(NUMBER)
        private val COMMENT_KEYS = arrayOf(COMMENT)
        private val OPERATOR_KEYS = arrayOf(OPERATOR)
        private val BRACE_KEYS = arrayOf(BRACE)
        private val ATTRIBUTE_KEYS = arrayOf(ATTRIBUTE)
        private val BAD_CHARACTER_KEYS = arrayOf(BAD_CHARACTER)
        private val EMPTY_KEYS = emptyArray<TextAttributesKey>()
    }
}
