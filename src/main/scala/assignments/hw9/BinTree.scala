package assignments.hw9

sealed trait BinTree[+A]
case class Node[A](value: A, left: BinTree[A], right: BinTree[A]) extends BinTree[A]
case class Leaf[A](value: A) extends BinTree[A]
case object Empty extends BinTree[Nothing]

object BinTree {

  implicit class BinTreeOps[A](tree: BinTree[A]) {

    def size: Int = tree.fold(0)((_, l, r) => 1 + l + r)

    def depth: Int = tree.fold(0)((_, l, r) => 1 + (l max r))

    def fold[B](z: B)(f: (A, B, B) => B): B = tree match {
      case Node(x, l, r) => f(x, l.fold(z)(f), r.fold(z)(f))
      case Leaf(x) => f(x, z, z)
      case Empty => z
    }

    def map[B](f: A => B): BinTree[B] = tree match {
      case Node(x, l, r) => Node(f(x), l.map(f), r.map(f))
      case Leaf(x) => Leaf(f(x))
      case Empty => Empty
    }

    def levelOrder: Seq[A] = {
      def go(queue: Seq[BinTree[A]]): Seq[A] = {
        if (queue.isEmpty) Seq.empty
        else queue.head match {
          case Empty         => go(queue.tail)
          case Leaf(x)       => x +: go(queue.tail)
          case Node(x, l, r) => x +: go(queue.tail :+ l :+ r)
        }
      }
      go(Seq(tree))
    }
  }
}

