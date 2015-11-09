package assignments.hw9

object Assignment9 extends App {

  val a =
    Node(1,
      Node(2,
        Node(4,
          Node(6, Node(7, Empty, Empty), Empty),
          Node(5, Empty, Empty)),
        Empty),
      Node(3, Empty, Empty)
    )


  //  a.map(println)
  println(a.depth)

  val b = Empty
  println(b.depth)

  val c = Node(0, Empty, a)
  println(c)
}
