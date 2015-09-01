package assigments

import scala.annotation.tailrec
import scala.collection.mutable
import scala.math.Ordered.orderingToOrdered

trait Graph[V, W] {
  protected def adjacencyMap: Map[V, Set[Edge[V, W]]]

  def vertexes: Set[V] = adjacencyMap.keys.toSet

  def edges: Set[Edge[V, W]]

  def adjacent(v: V): Set[Edge[V, W]] = adjacencyMap.getOrElse(v, Set.empty)

  def degree(v: V): Int = adjacencyMap.get(v).map(_.size).getOrElse(0)
}

object Graph {
  def fromEdges[V, W](_edges: Set[Edge[V, W]]): Graph[V, W] = new Graph[V, W] {
    protected def adjacencyMap: Map[V, Set[Edge[V, W]]] = (edges ++ edges.map(_.reverse)).groupBy(_.from)
    def edges: Set[Edge[V, W]] = _edges
  }
}

case class Edge[V, W: Numeric](from: V, to: V, weight: W) extends Ordered[Edge[V, W]] {
  def reverse: Edge[V, W] = Edge(to, from, weight)
  def compare(that: Edge[V, W]): Int = (this.weight compare that.weight) * -1
}

object ShortestPath {

  def uniformCostSearch[V, W](graph: Graph[V, W], start: V, dst: V)(implicit num: Numeric[W]): Option[List[Edge[V, W]]] = {
    @tailrec
    def go(current: V, visited: List[V], open: mutable.PriorityQueue[Edge[V, W]], path: Map[V, Edge[V, W]]): Option[Map[V, Edge[V, W]]] = {
      if (current == dst) return Some(path)
      if (open.isEmpty) return None

      val next = open.dequeue()
      if (visited.contains(next.to)) {
        go(current, visited, open, path)
      } else {
        open ++= graph.adjacent(next.to)
          .filter(e => !visited.contains(e.to))
          .map(e => Edge(e.from, e.to, num.plus(e.weight, next.weight)))

        go(next.to, visited :+ next.to, open, path + (next.from -> next))
      }
    }

    go(start, List(start), mutable.PriorityQueue[Edge[V, W]]() ++ graph.adjacent(start), Map.empty).map(_.values.toList)
  }
}

object Assigment1 extends App {
  val es = Set(Edge("a", "b", 3), Edge("b", "c", 4), Edge("c", "a", 5), Edge("d", "c", 10))
  val en = Set(Edge(1, 2, 3), Edge(2, 3, 4), Edge(3, 1, 5), Edge(4, 3, 10))
  val m  = Map(
    "a" -> Set(Edge("a", "b", 3), Edge("a", "c", 1)),
    "b" -> Set(Edge("b", "a", 3), Edge("b", "c", 2)),
    "c" -> Set(Edge("c", "a", 1), Edge("c", "b", 2))
  )

  val example2 = Set(
    Edge("a", "b", 4),
    Edge("a", "c", 2),
    Edge("b", "c", 1),
    Edge("b", "d", 5),
    Edge("c", "d", 8),
    Edge("c", "e", 10),
    Edge("d", "e", 2),
    Edge("d", "z", 6),
    Edge("e", "z", 3)
  )
  val g = Graph.fromEdges(es)
  //  println(g.edgeSize, g.vertexSize)
  //  println(Edge("a", "b", 5).equalTo(Edge("b", "a", 5)))
    println(ShortestPath.uniformCostSearch(g, "a", "d"))

//  val n = Graph.fromEdges(en)
//  println(n.path(1, 4))


}
