abstract class Stmt {

    interface Visitor<R> {
        fun visitBlockStmt(stmt: Block)
        fun visitExpressionStmt(stmt: Expression)
        fun visitPrintStmt(stmt: Print)
        fun visitVarStmt(stmt: Var)
        fun visitIfStmt(stmt: If)
    }

    abstract fun <R> accept(visitor: Visitor<R>)

    class Block(val statements: List<Stmt?>) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>) {
            return visitor.visitBlockStmt(this)
        }
    }

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

    class Var(val name: Token, val initializer: Expr?) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>) {
            return visitor.visitVarStmt(this)
        }
    }

    class If(val condition: Expr, val thenBranch: Stmt, val elseBranch: Stmt?) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>) {
            return visitor.visitIfStmt(this)
        }
    }
}