package assignments.hw9

object Assignment9 extends App {

  val a =
    Node("F",
      Node("B",
        Node("A", Empty, Empty),
        Node("D",
          Node("C", Empty, Empty),
          Node("F", Empty, Empty))),
      Node("G",
        Empty,
        Node("I",
          Node("H", Empty, Empty),
          Empty)))

  println(a.levelOrder)

  //  a.map(println)
  println(a.depth)

  val b = Empty
  println(b.depth)

  val c = Node(0, Empty, a)
  println(c)

}
