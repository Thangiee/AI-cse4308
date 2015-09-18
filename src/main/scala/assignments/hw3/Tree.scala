package assignments.hw3

/**
 * A lazy(non-strict) n-airy tree algebraic data type.
 *
 * The children of a node/branch are computed on-demand which means
 * this tree can support infinite depth and width.
 */
sealed trait Tree[+A] {
  type Fringe[B] = Stream[Tree[B]]
  type Items[B] = Stream[Tree[B]]

  def traversal[B >: A](cat: (Fringe[B], Items[B]) => Fringe[B]): Stream[B] = {
    def go(fringe: Fringe[B]): Stream[B] = {
      if (fringe.isEmpty) return Stream.Empty

      fringe.head match {
        case Node(x, items) => Stream.cons(x, go(cat(fringe.tail, items)))
        case Leaf(x) => Stream.cons(x, go(fringe.tail))
        case Empty => go(fringe.tail)
      }
    }
    go(Stream(this))
  }

  def levelOrder: Stream[A] = traversal((queue, items) => queue ++ items)

  def preOrder: Stream[A] = traversal((stack, items) => items ++ stack)
}

case class Node[A](value: A, subTrees: Stream[Tree[A]]) extends Tree[A]
case class Leaf[A](value: A) extends Tree[A]
case object Empty extends Tree[Nothing]
