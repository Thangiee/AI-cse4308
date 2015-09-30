package assignments

import scala.collection.JavaConverters._

package object hw4 {

  implicit class LogicalExpressionConversion(expr: LogicalExpression) {
    def toADT: LogicalExpressionADT = {
      if (expr.getUniqueSymbol == null)
        expr.getConnective.toLowerCase match {
          case "not" => Not(expr.getSubexpressions.get(0).toADT)
          case "and" => And(expr.getSubexpressions.asScala.map(_.toADT))
          case "or"  => Or(expr.getSubexpressions.asScala.map(_.toADT))
          case "xor" => Xor(expr.getSubexpressions.asScala.map(_.toADT))
          case "if"  => If(expr.getSubexpressions.get(0).toADT, expr.getSubexpressions.get(1).toADT)
          case "iff" => Iff(expr.getSubexpressions.get(0).toADT, expr.getSubexpressions.get(1).toADT)
        }
      else Symbol(expr.getUniqueSymbol)
    }
  }

}
