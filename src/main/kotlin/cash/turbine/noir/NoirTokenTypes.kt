package cash.turbine.noir

import com.intellij.psi.tree.IElementType

object NoirTokenTypes {
    val KEYWORD = IElementType("NOIR_KEYWORD", NoirLanguage)
    val TYPE = IElementType("NOIR_TYPE", NoirLanguage)
    val IDENTIFIER = IElementType("NOIR_IDENTIFIER", NoirLanguage)
    val STRING = IElementType("NOIR_STRING", NoirLanguage)
    val NUMBER = IElementType("NOIR_NUMBER", NoirLanguage)
    val BOOLEAN = IElementType("NOIR_BOOLEAN", NoirLanguage)
    val LINE_COMMENT = IElementType("NOIR_LINE_COMMENT", NoirLanguage)
    val BLOCK_COMMENT = IElementType("NOIR_BLOCK_COMMENT", NoirLanguage)
    val OPERATOR = IElementType("NOIR_OPERATOR", NoirLanguage)
    val BRACE = IElementType("NOIR_BRACE", NoirLanguage)
    val ATTRIBUTE = IElementType("NOIR_ATTRIBUTE", NoirLanguage)
    val WHITESPACE = IElementType("NOIR_WHITESPACE", NoirLanguage)
    val BAD_CHARACTER = IElementType("NOIR_BAD_CHARACTER", NoirLanguage)
}
