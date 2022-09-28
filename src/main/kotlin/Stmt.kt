abstract class Stmt {

    interface Visitor<R> {
        fun visitExpressionStmt(stmt: Expression)
        fun visitPrintStmt(stmt: Print)
    }

    abstract fun <R> accept(visitor: Visitor<R>)

    class Expression(val expression: Expr) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>) {
            return visitor.visitExpressionStmt(this)
        }
    }

    class Print(val expression: Expr) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>) {
            return visitor.visitPrintStmt(this)
        }
    }
}