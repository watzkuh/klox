class AstPrinter : Expr.Visitor<String> {
    fun print(expr: Expr?): String {
        return expr?.accept(this) ?: ""
    }

    override fun visitBinaryExpr(expr: Expr.Binary): String {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right)
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): String {
        return parenthesize("group", expr.expression)
    }

    override fun visitLiteralExpr(expr: Expr.Literal): String {
        return expr.value?.toString() ?: "nil"
    }

    override fun visitUnaryExpr(expr: Expr.Unary): String {
        return parenthesize(expr.operator.lexeme, expr.right)
    }

    private fun parenthesize(name: String, vararg expressions: Expr): String {
        val builder = StringBuilder()
        builder.append('(').append(name)
        for (expr in expressions) {
            builder.append(' ')
            builder.append(expr.accept(this))
        }
        builder.append(')')
        return builder.toString()
    }
}