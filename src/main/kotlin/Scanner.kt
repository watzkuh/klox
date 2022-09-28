class Scanner(private val source: String) {

    private val tokens: MutableList<Token> = mutableListOf()
    private var start = 0
    private var current = 0
    private var line = 1

    private val keywords = mapOf(
        "and" to TokenType.AND,
        "class" to TokenType.CLASS,
        "else" to TokenType.ELSE,
        "false" to TokenType.FALSE,
        "for" to TokenType.FOR,
        "fun" to TokenType.FUN,
        "if" to TokenType.IF,
        "nil" to TokenType.NIL,
        "or" to TokenType.OR,
        "print" to TokenType.PRINT,
        "return" to TokenType.RETURN,
        "super" to TokenType.SUPER,
        "this" to TokenType.THIS,
        "true" to TokenType.TRUE,
        "var" to TokenType.VAR,
        "while" to TokenType.WHILE
    )

    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            start = current
            scanToken()
        }

        tokens.add(Token(TokenType.EOF, "", null, line))
        return tokens
    }

    private fun scanToken() {
        advance().let {
            when (it) {
                '(' -> addToken(TokenType.LEFT_PAREN)
                ')' -> addToken(TokenType.RIGHT_PAREN)
                '{' -> addToken(TokenType.LEFT_BRACE)
                '}' -> addToken(TokenType.RIGHT_BRACE)
                ',' -> addToken(TokenType.COMMA)
                '.' -> addToken(TokenType.DOT)
                '-' -> addToken(TokenType.MINUS)
                '+' -> addToken(TokenType.PLUS)
                ';' -> addToken(TokenType.SEMICOLON)
                '*' -> addToken(TokenType.STAR)
                '!' -> addToken(if (match('=')) TokenType.BANG_EQUAL else TokenType.BANG)
                '<' -> addToken(if (match('=')) TokenType.LESS_EQUAL else TokenType.LESS)
                '>' -> addToken(if (match('=')) TokenType.GREATER_EQUAL else TokenType.GREATER)
                '=' -> addToken(if (match('=')) TokenType.EQUAL_EQUAL else TokenType.EQUAL)
                '/' -> {
                    when (peek()) {
                        '/' -> while (peek() != '\n' && !isAtEnd()) advance()
                        '*' -> { // Begin of a block comment
                            advance()
                            while (!isAtEnd()) {
                                if (advance() == '*') {
                                    if (peek() == '/') {
                                        advance()
                                        break
                                    }
                                }
                            }
                        }
                        else -> addToken(TokenType.SLASH)
                    }
                }
                in listOf(' ', '\r', '\t') -> {
                }
                '\n' -> line++
                '"' -> string()
                else -> {
                    if (it.isDigit()) {
                        number()
                    } else if (isAlpha(it)) {
                        identifier()
                    } else {
                        Lox.error(line, "Unexpected character")
                    }
                }
            }
        }
    }

    private fun peek(): Char {
        if (isAtEnd()) return '\u0000'
        return source[current]
    }

    private fun peekNext(): Char {
        if (current + 1 >= source.length) return '\u0000'
        return source[current + 1]
    }

    private fun advance() = source[current++]

    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false
        if (source[current] != expected) return false

        current++
        return true
    }

    private fun isAtEnd() = current >= source.length

    private fun isAlpha(c: Char) = (c in 'a'..'z') || (c in 'A'..'Z') || c == '_'

    private fun isAlphaNumeric(c: Char) = c.isDigit() || isAlpha(c)

    private fun addToken(tokenType: TokenType) {
        addToken(tokenType, null)
    }

    private fun addToken(tokenType: TokenType, literal: Any?) {
        val text = source.substring(start, current)
        tokens.add(Token(tokenType, text, literal, line))
    }

    private fun string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                line++
            }
            advance()
        }
        if (isAtEnd()) {
            Lox.error(line, "Unterminated string")
            return
        }
        advance() // The closing "
        // Trim the surrounding quotes
        val value = source.substring(start + 1, current - 1)
        addToken(TokenType.STRING, value)
    }

    private fun number() {
        while (peek().isDigit()) advance()
        // Look for fractional part
        if (peek() == '.' && peekNext().isDigit()) {
            // Consume the "."
            advance()
            while (peek().isDigit()) advance()
        }
        addToken(TokenType.NUMBER, source.substring(start, current).toDouble())
    }

    private fun identifier() {
        while (isAlphaNumeric(peek())) advance()

        val text = source.substring(start, current)
        val type = keywords[text]
        addToken(type ?: TokenType.IDENTIFIER)
    }
}
