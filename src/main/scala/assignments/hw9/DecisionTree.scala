package assignments.hw9

import assignments.hw9.DecisionTree._

class DecisionTree(treeId: Int, tree: BinTree[Info]) {

  def classify(dataSet: DataSet): Distribution = {
    def go(subTree: BinTree[Info]): Distribution = {
      subTree match {
        case Node(NodeInfo(_, index, threshold, _), left, right) =>
          if (dataSet.attrs(index) < threshold) go(left) else go(right)
        case Leaf(LeafInfo(_, labels)) =>
          labels
        case _ =>
          throw new IllegalStateException("This shouldn't happen if the decision tree is built correctly")
      }
    }
    go(tree)
  }

  override def toString: String = tree.levelOrder.map {
    case NodeInfo(id, index, threshold, gain) =>
      f"tree=$treeId%2d, node=$id%3d, feature=$index%2d, thr=$threshold%6.2f, gain=$gain"
    case LeafInfo(id, label) =>
      f"tree=$treeId%2d, node=$id%3d, feature=${-1}%2d, thr=${-1.0}%6.2f, gain=${0.0}"
  }.mkString("\n")

}

object DecisionTree {

  sealed trait Info
  case class NodeInfo(id: Int, attrIndex: Int, threshold: Double, gain: Double) extends Info
  case class LeafInfo(id: Int, labelsDistro: Distribution) extends Info

  def learn(id: Int, examples: Seq[DataSet], testAttrs: Seq[Int], strategy: Strategy, pruning: Boolean = false): DecisionTree = {
    def go(n: Int, examples: Seq[DataSet], testAttrs: Seq[Int], default: Distribution):BinTree[Info] = {
      examples match {
        case ex if (pruning && ex.size < 50) || ex.isEmpty => Leaf(LeafInfo(id = n, labelsDistro = default))

        case ex if ex.map(_.label).distinct.size == 1 => // check if all examples have the same class
          Leaf(LeafInfo(id = n, labelsDistro = Map(ex.head.label -> 1.0)))

        case ex =>
          val (bestAttr, bestThreshold, gain) = chooseAttr(ex, testAttrs, strategy)
          val (leftExamples, rightExamples) = examples.partition(_.attrs(bestAttr) < bestThreshold)

          Node(
            value = NodeInfo(id = n, bestAttr, bestThreshold, gain),
            left = go(n*2, leftExamples, testAttrs, distribution(examples)),
            right = go(n*2 + 1, rightExamples, testAttrs, distribution(examples))
          )
      }
    }
    new DecisionTree(id, go(1, examples, testAttrs, distribution(examples)))
  }
}
