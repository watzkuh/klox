class Interpreter : Expr.Visitor<Any?> {
    override fun visitBinaryExpr(expr: Expr.Binary): Any {
        val left = evaluate(expr.left)
        val right = evaluate(expr.right)

        return when (expr.operator.type) {
            TokenType.MINUS -> {
                return (left as Double) - (right as Double)
            }
            TokenType.PLUS -> {
                if (left is Double && right is Double) {
                    return left + right
                } else if (left is String && right is String) {
                    return left + right
                }
            }
            TokenType.SLASH -> {
                return (left as Double) / (right as Double)
            }
            TokenType.STAR -> {
                return (left as Double) * (right as Double)
            }
            TokenType.BANG_EQUAL -> {
                return !isEqual(left, right)
            }
            TokenType.EQUAL_EQUAL -> {
                return isEqual(left, right)
            }
            TokenType.GREATER -> {
                return (left as Double) > (right as Double)
            }
            TokenType.GREATER_EQUAL -> {
                return (left as Double) >= (right as Double)
            }
            TokenType.LESS -> {
                return (left as Double) < (right as Double)
            }
            TokenType.LESS_EQUAL -> {
                return (left as Double) <= (right as Double)
            }
            else -> {}
        }
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): Any? {
        return evaluate(expr.expression)
    }

    override fun visitLiteralExpr(expr: Expr.Literal): Any? {
        return expr.value
    }

    override fun visitUnaryExpr(expr: Expr.Unary): Any? {
        val right = evaluate(expr.right)

        when (expr.operator.type) {
            TokenType.BANG -> return !isTruthy(right)
            TokenType.MINUS -> return (right as Double).unaryMinus()
            else -> {}
        }

        return null
    }

    private fun isTruthy(value: Any?): Boolean {
        if (value == null) return false
        if (value is Boolean) return value
        return true
    }

    private fun isEqual(a: Any?, b: Any?): Boolean {
        if (a == null && b == null) return true
        if (a == null) return false

        return a == b
    }

    private fun evaluate(expr: Expr): Any? {
        return expr.accept(this)
    }

}