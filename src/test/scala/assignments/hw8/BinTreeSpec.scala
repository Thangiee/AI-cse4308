package assignments.hw8

import assignments.BaseWordSpec
import assignments.hw9._
import org.scalacheck.Gen

class BinTreeSpec extends BaseWordSpec {

  "A binary tree" when {

    "empty" should {
      val tree = Empty
      "have a size of 0" in {
        tree.size shouldEqual 0
      }
      "have a depth of 0" in {
        tree.depth shouldEqual 0
      }
    }

    "non-empty" should {
      "have a size that is larger than its left and right children" in {
        forAll((treeGen, "tree")) {
          case tree: Node[Int] =>
            tree.size should be > tree.left.size
            tree.size should be > tree.right.size
          case Empty =>
        }
      }
      "have a depth that is larger than its left and right children" in {
        forAll((treeGen, "tree")) {
          case tree: Node[Int] =>
            tree.depth should be > tree.left.depth
            tree.depth should be > tree.right.depth
          case Empty =>
        }
      }
    }

  }

  it must {
    "return the correct level-order" in {
      // https://en.wikipedia.org/wiki/Tree_traversal#/media/File:Sorted_binary_tree_breadth-first_traversal.svg
      val tree =
        Node("F",
          Node("B",
            Node("A", Empty, Empty),
            Node("D",
              Node("C", Empty, Empty),
              Node("E", Empty, Empty))),
          Node("G",
            Empty,
            Node("I",
              Node("H", Empty, Empty),
              Empty)))

      tree.levelOrder shouldEqual Seq("F", "B", "G", "A", "D", "I", "C", "E", "H")
    }
  }

  def nodeGen: Gen[Node[Int]] = for {
    value <- Gen.choose(-100, 100)
    left <- treeGen
    right <- treeGen
  } yield Node(value, left, right)

  def treeGen: Gen[BinTree[Int]] = Gen.oneOf(Gen.const(Empty), nodeGen)

}
