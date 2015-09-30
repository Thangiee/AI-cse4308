package assignments.hw4

object InferenceEngine {

  def checkTrueFalse(kb: LogicalExpression, alpha: LogicalExpression): Unit = {
    val (_kb, _alpha) = (kb.toADT, alpha.toADT)

    (ttEntails(_kb, _alpha), ttEntails(_kb, Not(_alpha))) match {
      case (true, false)  => println("definitely true")
      case (false, true)  => println("definitely false")
      case (false, false) => println("possibly true, possibly false")
      case (true, true)   => println("both true and false")
    }
  }

  def ttEntails(kb: LogicalExpressionADT, alpha: LogicalExpressionADT): Boolean = {
    // efficiency requirement; Identify some symbols and their values right at the beginning.
    val model = kb match {
      case And(exprs) =>
        exprs.foldLeft(Map.empty[String, Boolean])((model, expr) => expr match {
          case Not(Symbol(symbol)) => model + (symbol -> false) // case connective is NOT, and whose only child is a leaf
          case Symbol(symbol)      => model + (symbol -> true)  // case leaf
          case _                   => model // don't assign symbol for other cases
        })
      case _ => Map.empty[String, Boolean] // ignore cases where top connective is not an AND
    }

    // combine symbols from kb and alpha. then remove the symbols
    // that have been assigned values in the model
    val symbols = (kb.extractSymbols ++ alpha.extractSymbols).filterNot(model.contains)

    ttCheckAll(kb, alpha, symbols, model)
  }

  def ttCheckAll(kb: LogicalExpressionADT, alpha: LogicalExpressionADT, symbols: Set[String], model: Map[String, Boolean]): Boolean = {
    if (symbols.isEmpty) {
      if (plTrue(kb, model)) plTrue(alpha, model) // does the kb entails alpha?
      else true
    } else {
      ttCheckAll(kb, alpha, symbols.tail, model + (symbols.head -> true)) &&
      ttCheckAll(kb, alpha, symbols.tail, model + (symbols.head -> false))
    }
  }

  def plTrue(expression: LogicalExpressionADT, model: Map[String, Boolean]): Boolean = expression match {
    case Not(expr)  => !plTrue(expr, model)
    case And(exprs) => if (exprs.isEmpty) true else exprs.forall(expr => plTrue(expr, model))
    case Or(exprs)  => if (exprs.isEmpty) false else !exprs.forall(expr => !plTrue(expr, model))
    case Xor(exprs) => if (exprs.isEmpty) false else exprs.map(expr => plTrue(expr, model)).count(_ == true) == 1
    case If(l, r)   => !plTrue(l, model) || plTrue(r, model)
    case Iff(l, r)  => plTrue(l, model) == plTrue(r, model)
    case Symbol(symbol) => model.get(symbol).get
  }
}
