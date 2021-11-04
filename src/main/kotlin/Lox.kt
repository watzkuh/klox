import java.io.File
import java.nio.charset.Charset
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

    fun runFile(path: String) {
        exec(File(path).readText(Charset.defaultCharset()))
        if (hadError) {
            exitProcess(65)
        }
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
        for (token in tokens) {
            println(token)
        }
    }

    companion object Error {
        private var hadError = false

        fun error(line: Int, message: String) {
            report(line, "", message)
        }

        private fun report(line: Int, where: String, message: String) {
            println("[line $line] Error$where: $message")
            hadError = true
        }
    }
}