package assignments.hw9

sealed trait BinTree[+A]
case class Node[A](value: A, left: BinTree[A], right: BinTree[A]) extends BinTree[A]
case object Empty extends BinTree[Nothing]

object BinTree {

  implicit class BinTreeOps[A](tree: BinTree[A]) {

    def size: Int = tree.fold(0)((_, l, r) => 1 + l + r)

    def depth: Int = tree.fold(0)((_, l, r) => 1 + (l max r))

    def fold[B](z: B)(f: (A, B, B) => B): B = tree match {
      case Node(x, l, r) => f(x, l.fold(z)(f), r.fold(z)(f))
      case Empty => z
    }

    def map[B](f: A => B): BinTree[B] = tree.fold(Empty: BinTree[B])((x, l, r) => Node(f(x), l, r))
  }
}

