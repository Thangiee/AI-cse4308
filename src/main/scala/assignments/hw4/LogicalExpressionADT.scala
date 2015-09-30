package assignments.hw4

sealed trait LogicalExpressionADT {
  def extractSymbols: Set[String] = {
    def go(expression: LogicalExpressionADT, symbols: Set[String]): Set[String] = expression match {
      case Not(expr)  => go(expr, symbols)
      case And(exprs) => exprs.map(expr => go(expr, symbols)).foldLeft(Set.empty[String])(_ ++ _)
      case Or(exprs)  => exprs.map(expr => go(expr, symbols)).foldLeft(Set.empty[String])(_ ++ _)
      case Xor(exprs) => exprs.map(expr => go(expr, symbols)).foldLeft(Set.empty[String])(_ ++ _)
      case If(l, r)   => go(l, symbols) ++ go(r, symbols)
      case Iff(l, r)  => go(l, symbols) ++ go(r, symbols)
      case Symbol(symbol) => symbols + symbol
    }

    go(this, Set.empty)
  }
}
case class Not(expr: LogicalExpressionADT)                               extends LogicalExpressionADT
case class And(exprs: Seq[LogicalExpressionADT])                         extends LogicalExpressionADT
case class Or(exprs: Seq[LogicalExpressionADT])                          extends LogicalExpressionADT
case class Xor(exprs: Seq[LogicalExpressionADT])                         extends LogicalExpressionADT
case class If(lExpr: LogicalExpressionADT, rExpr: LogicalExpressionADT)  extends LogicalExpressionADT
case class Iff(lExpr: LogicalExpressionADT, rExpr: LogicalExpressionADT) extends LogicalExpressionADT
case class Symbol(value: String)                                         extends LogicalExpressionADT

