package cash.turbine.noir

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType

class NoirLexer : LexerBase() {
    private var buffer: CharSequence = ""
    private var bufferEnd: Int = 0
    private var tokenStart: Int = 0
    private var tokenEnd: Int = 0
    private var tokenType: IElementType? = null

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.bufferEnd = endOffset
        this.tokenStart = startOffset
        this.tokenEnd = startOffset
        advance()
    }

    override fun getState(): Int = 0
    override fun getTokenType(): IElementType? = tokenType
    override fun getTokenStart(): Int = tokenStart
    override fun getTokenEnd(): Int = tokenEnd
    override fun getBufferSequence(): CharSequence = buffer
    override fun getBufferEnd(): Int = bufferEnd

    override fun advance() {
        tokenStart = tokenEnd
        if (tokenStart >= bufferEnd) {
            tokenType = null
            return
        }

        val c = buffer[tokenStart]

        when {
            c == '/' && tokenStart + 1 < bufferEnd && buffer[tokenStart + 1] == '/' -> {
                tokenEnd = findEndOfLine()
                tokenType = NoirTokenTypes.LINE_COMMENT
            }
            c == '/' && tokenStart + 1 < bufferEnd && buffer[tokenStart + 1] == '*' -> {
                tokenEnd = findEndOfBlockComment()
                tokenType = NoirTokenTypes.BLOCK_COMMENT
            }
            c == '"' -> {
                tokenEnd = findEndOfString()
                tokenType = NoirTokenTypes.STRING
            }
            c == 'f' && tokenStart + 1 < bufferEnd && buffer[tokenStart + 1] == '"' -> {
                tokenEnd = findEndOfFString()
                tokenType = NoirTokenTypes.STRING
            }
            c == 'r' && tokenStart + 1 < bufferEnd && buffer[tokenStart + 1] == '#' -> {
                tokenEnd = findEndOfRawString()
                tokenType = NoirTokenTypes.STRING
            }
            c == 'r' && tokenStart + 1 < bufferEnd && buffer[tokenStart + 1] == '"' -> {
                tokenEnd = findEndOfSimpleRawString()
                tokenType = NoirTokenTypes.STRING
            }
            c == '#' && tokenStart + 1 < bufferEnd && buffer[tokenStart + 1] == '[' -> {
                tokenEnd = findEndOfAttribute()
                tokenType = NoirTokenTypes.ATTRIBUTE
            }
            c.isDigit() || (c == '-' && tokenStart + 1 < bufferEnd && buffer[tokenStart + 1].isDigit()) -> {
                tokenEnd = findEndOfNumber()
                tokenType = NoirTokenTypes.NUMBER
            }
            c == '0' && tokenStart + 1 < bufferEnd && buffer[tokenStart + 1] == 'x' -> {
                tokenEnd = findEndOfHexNumber()
                tokenType = NoirTokenTypes.NUMBER
            }
            c.isLetter() || c == '_' -> {
                tokenEnd = findEndOfIdentifier()
                val word = buffer.subSequence(tokenStart, tokenEnd).toString()
                tokenType = when {
                    word in CONTROL_KEYWORDS -> NoirTokenTypes.KEYWORD
                    word in OTHER_KEYWORDS -> NoirTokenTypes.KEYWORD
                    word in BUILTIN_TYPES -> NoirTokenTypes.TYPE
                    word == "true" || word == "false" -> NoirTokenTypes.BOOLEAN
                    word.firstOrNull()?.isUpperCase() == true -> NoirTokenTypes.TYPE
                    else -> NoirTokenTypes.IDENTIFIER
                }
            }
            c in "{}[]()<>" -> {
                tokenEnd = tokenStart + 1
                tokenType = NoirTokenTypes.BRACE
            }
            c in "+-*/%=!&|^~<>?:;,.@" -> {
                tokenEnd = tokenStart + 1
                tokenType = NoirTokenTypes.OPERATOR
            }
            c.isWhitespace() -> {
                tokenEnd = findEndOfWhitespace()
                tokenType = NoirTokenTypes.WHITESPACE
            }
            else -> {
                tokenEnd = tokenStart + 1
                tokenType = NoirTokenTypes.BAD_CHARACTER
            }
        }
    }

    private fun findEndOfLine(): Int {
        var pos = tokenStart + 2
        while (pos < bufferEnd && buffer[pos] != '\n') pos++
        if (pos < bufferEnd) pos++
        return pos
    }

    private fun findEndOfBlockComment(): Int {
        var pos = tokenStart + 2
        var depth = 1
        while (pos < bufferEnd - 1 && depth > 0) {
            if (buffer[pos] == '/' && buffer[pos + 1] == '*') {
                depth++
                pos += 2
            } else if (buffer[pos] == '*' && buffer[pos + 1] == '/') {
                depth--
                pos += 2
            } else {
                pos++
            }
        }
        return pos
    }

    private fun findEndOfString(): Int {
        var pos = tokenStart + 1
        while (pos < bufferEnd) {
            when (buffer[pos]) {
                '\\' -> pos += 2
                '"' -> return pos + 1
                else -> pos++
            }
        }
        return pos
    }

    private fun findEndOfFString(): Int {
        var pos = tokenStart + 2
        while (pos < bufferEnd) {
            when (buffer[pos]) {
                '\\' -> pos += 2
                '"' -> return pos + 1
                else -> pos++
            }
        }
        return pos
    }

    private fun findEndOfRawString(): Int {
        var hashCount = 0
        var pos = tokenStart + 1
        while (pos < bufferEnd && buffer[pos] == '#') {
            hashCount++
            pos++
        }
        if (pos >= bufferEnd || buffer[pos] != '"') return pos
        pos++
        while (pos < bufferEnd) {
            if (buffer[pos] == '"') {
                var endHashes = 0
                var checkPos = pos + 1
                while (checkPos < bufferEnd && buffer[checkPos] == '#' && endHashes < hashCount) {
                    endHashes++
                    checkPos++
                }
                if (endHashes == hashCount) return checkPos
            }
            pos++
        }
        return pos
    }

    private fun findEndOfSimpleRawString(): Int {
        var pos = tokenStart + 2
        while (pos < bufferEnd && buffer[pos] != '"') pos++
        return if (pos < bufferEnd) pos + 1 else pos
    }

    private fun findEndOfAttribute(): Int {
        var pos = tokenStart + 2
        var depth = 1
        while (pos < bufferEnd && depth > 0) {
            when (buffer[pos]) {
                '[' -> depth++
                ']' -> depth--
            }
            pos++
        }
        return pos
    }

    private fun findEndOfNumber(): Int {
        var pos = tokenStart
        if (pos < bufferEnd && buffer[pos] == '-') pos++
        while (pos < bufferEnd && (buffer[pos].isDigit() || buffer[pos] == '.')) pos++
        return pos
    }

    private fun findEndOfHexNumber(): Int {
        var pos = tokenStart + 2
        while (pos < bufferEnd && buffer[pos].isHexDigit()) pos++
        return pos
    }

    private fun Char.isHexDigit(): Boolean = this in '0'..'9' || this in 'a'..'f' || this in 'A'..'F'

    private fun findEndOfIdentifier(): Int {
        var pos = tokenStart
        while (pos < bufferEnd && (buffer[pos].isLetterOrDigit() || buffer[pos] == '_')) pos++
        return pos
    }

    private fun findEndOfWhitespace(): Int {
        var pos = tokenStart
        while (pos < bufferEnd && buffer[pos].isWhitespace()) pos++
        return pos
    }

    companion object {
        val CONTROL_KEYWORDS = setOf(
            "fn", "impl", "trait", "type", "mod", "use", "struct", "if", "else",
            "for", "loop", "while", "enum", "match", "break", "continue", "return"
        )

        val OTHER_KEYWORDS = setOf(
            "global", "comptime", "quote", "unsafe", "unconstrained", "pub",
            "crate", "mut", "self", "in", "as", "let", "where"
        )

        val BUILTIN_TYPES = setOf(
            "u1", "u8", "u16", "u32", "u64", "u128",
            "i1", "i8", "i16", "i32", "i64", "i128",
            "str", "bool", "Field", "field"
        )
    }
}
