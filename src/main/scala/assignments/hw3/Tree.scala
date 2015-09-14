package assignments.hw3

sealed trait Tree[+A] {

  def levelOrder: Stream[A] = {
    def go(queue: Stream[Tree[A]]): Stream[A] = {
      if (queue.isEmpty) return Stream.Empty

      queue.head match {
        case Node(x, xs) => Stream.cons(x, go(queue.tail ++ xs))
        case Leaf(x) => Stream.cons(x, go(queue.tail))
        case Empty => go(queue.tail)
      }
    }
    go(Stream(this))
  }

  def preOrder: Stream[A] = {
    def go(stack: Stream[Tree[A]]): Stream[A] = {
      if (stack.isEmpty) return Stream.Empty

      stack.head match {
        case Node(x, xs) => Stream.cons(x, go(xs ++ stack.tail))
        case Leaf(x) => Stream.cons(x, go(stack.tail))
        case Empty => go(stack.tail)
      }
    }
    go(Stream(this))
  }
}

case class Node[A](value: A, subTrees: Stream[Tree[A]]) extends Tree[A]
case class Leaf[A](value: A) extends Tree[A]
case object Empty extends Tree[Nothing]

