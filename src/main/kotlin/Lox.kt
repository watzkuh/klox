import java.io.File
import java.nio.charset.Charset
import kotlin.math.exp
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size > 1) {
        println("Usage: klox [script]")
        exitProcess(64)
    } else if (args.size == 1) {
        Lox().runFile(args[0])
    } else {
        Lox().runPrompt()
    }
}

class Lox {

    private final val interpreter = Interpreter()

    fun runFile(path: String) {
        exec(File(path).readText(Charset.defaultCharset()))
        if (hadError) exitProcess(65)
        if (hadRuntimeError) exitProcess(70)
    }

    fun runPrompt() {
        while (true) {
            print("> ")
            val line = readLine() ?: break
            exec(line)
            hadError = false
        }
    }

    private fun exec(source: String) {
        val scanner = Scanner(source)
        val tokens = scanner.scanTokens()
        val parser = Parser(tokens)
        val statements = parser.parse()

        if (hadError) return

        interpreter.interpret(statements)
    }

    companion object Error {
        private var hadError = false
        private var hadRuntimeError = false

        fun error(line: Int, message: String) {
            report(line, "", message)
        }

        fun error(token: Token, message: String) {
            if (token.type == TokenType.EOF) {
                report(token.line, " at end", message)
            } else {
                report(token.line, " at '" + token.lexeme + "'", message)
            }
        }

        fun runtimeError(error: RuntimeError) {
            println("${error.message} \n[line ${error.token.line}]")
        }

        private fun report(line: Int, where: String, message: String) {
            println("[line $line] Error$where: $message")
            hadError = true
        }
    }
}