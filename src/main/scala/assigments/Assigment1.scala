package assigments

import scala.annotation.tailrec
import scala.collection.mutable
import scala.io.Source
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
    def go(current: V, visited: List[V], open: mutable.PriorityQueue[Edge[V, W]], path: List[Edge[V, W]]): Option[List[Edge[V, W]]] = {
      if (current == dst) return Some(path)
      if (open.isEmpty) return None

      val next = open.dequeue()
      if (visited.contains(next.to)) {
        go(current, visited, open, path)
      } else {
        open ++= graph.adjacent(next.to)
          .filter(e => !visited.contains(e.to))
          .map(e => Edge(e.from, e.to, num.plus(e.weight, next.weight)))

        go(next.to, visited :+ next.to, open, path :+ next)
      }
    }

    go(start, List(start), mutable.PriorityQueue[Edge[V, W]]() ++ graph.adjacent(start), Nil)
  }
}

object Assigment1 extends App {
  def parseEdge(line: String): Edge[String, Int] = {
    val Array(src, dst, weight)  = line.split(" ")
    Edge(src, dst, weight.toInt)
  }

  val edges = Source.fromFile("./input1.txt").getLines()
    .filter(line => line != "END OF INPUT" && line.nonEmpty)
    .map(parseEdge)

  val g = Graph.fromEdges[String, Int](edges.toSet)

  println(ShortestPath.uniformCostSearch(g, "Bremen", "Luebeck"))
}
