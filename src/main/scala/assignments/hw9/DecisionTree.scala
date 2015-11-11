package assignments.hw9

case class DataSet(label: Double, attrs: Double*)

class DecisionTree {

}

object DecisionTree {

  def train(examples: Seq[DataSet], testAttrs: Seq[Double], default: Double): DecisionTree = ???
}
