package cash.turbine.noir

import com.intellij.testFramework.LexerTestCase

class NoirLexerTest : LexerTestCase() {

    fun testKeywords() {
        doTest(
            "fn struct if else for",
            """
            NOIR_KEYWORD ('fn')
            NOIR_WHITESPACE (' ')
            NOIR_KEYWORD ('struct')
            NOIR_WHITESPACE (' ')
            NOIR_KEYWORD ('if')
            NOIR_WHITESPACE (' ')
            NOIR_KEYWORD ('else')
            NOIR_WHITESPACE (' ')
            NOIR_KEYWORD ('for')
            """.trimIndent()
        )
    }

    fun testTypes() {
        doTest(
            "u8 u32 Field bool",
            """
            NOIR_TYPE ('u8')
            NOIR_WHITESPACE (' ')
            NOIR_TYPE ('u32')
            NOIR_WHITESPACE (' ')
            NOIR_TYPE ('Field')
            NOIR_WHITESPACE (' ')
            NOIR_TYPE ('bool')
            """.trimIndent()
        )
    }

    fun testLineComment() {
        doTest(
            "// this is a comment\n",
            "NOIR_LINE_COMMENT ('// this is a comment\\n')"
        )
    }

    fun testBlockComment() {
        doTest(
            "/* block comment */",
            """
            NOIR_BLOCK_COMMENT ('/* block comment */')
            """.trimIndent()
        )
    }

    fun testString() {
        doTest(
            "\"hello world\"",
            """
            NOIR_STRING ('"hello world"')
            """.trimIndent()
        )
    }

    fun testFString() {
        doTest(
            "f\"value: {x}\"",
            """
            NOIR_STRING ('f"value: {x}"')
            """.trimIndent()
        )
    }

    fun testNumbers() {
        doTest(
            "42 0x1a2b",
            """
            NOIR_NUMBER ('42')
            NOIR_WHITESPACE (' ')
            NOIR_NUMBER ('0')
            NOIR_IDENTIFIER ('x1a2b')
            """.trimIndent()
        )
    }

    fun testAttribute() {
        doTest(
            "#[test]",
            """
            NOIR_ATTRIBUTE ('#[test]')
            """.trimIndent()
        )
    }

    fun testBooleans() {
        doTest(
            "true false",
            """
            NOIR_BOOLEAN ('true')
            NOIR_WHITESPACE (' ')
            NOIR_BOOLEAN ('false')
            """.trimIndent()
        )
    }

    fun testFunction() {
        doTest(
            "fn main() {}",
            """
            NOIR_KEYWORD ('fn')
            NOIR_WHITESPACE (' ')
            NOIR_IDENTIFIER ('main')
            NOIR_BRACE ('(')
            NOIR_BRACE (')')
            NOIR_WHITESPACE (' ')
            NOIR_BRACE ('{')
            NOIR_BRACE ('}')
            """.trimIndent()
        )
    }

    override fun createLexer() = NoirLexer()

    override fun getDirPath() = ""
}
