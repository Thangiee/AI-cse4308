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
  def findEdge(src: V, dst: V): Option[Edge[V, W]] = adjacencyMap.get(src).flatMap(_.find(_.to == dst))
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

object UniformCostSearch {

  def findShortestPath[V, W: Numeric](graph: Graph[V, W], start: V, dst: V): List[Edge[V, W]] = {
    val tree = makeSearchTree(graph, start, dst)

    @tailrec
    def go(node: V, path: List[Edge[V, W]]): List[Edge[V, W]] = {
      tree.get(node) match {
        case Some(edge) => go(edge.from, path :+ edge)
        case None       => path.reverse
      }
    }
    go(dst, Nil)
  }

  private def makeSearchTree[V, W](graph: Graph[V, W], start: V, dst: V)(implicit num: Numeric[W]): Map[V, Edge[V, W]] = {
    @tailrec
    def go(current: V, visited: List[V], open: mutable.PriorityQueue[Edge[V, W]], tree: Map[V, Edge[V, W]]): Map[V, Edge[V, W]] = {
      if (current == dst) return tree
      if (open.isEmpty) return Map.empty

      val next = open.dequeue()
      if (visited.contains(next.to)) {
        go(current, visited, open, tree)
      } else {
        open ++= graph.adjacent(next.to)
          .filter(e => !visited.contains(e.to))
          .map(e => Edge(e.from, e.to, num.plus(e.weight, next.weight)))

        go(next.to, visited :+ next.to, open, tree + (next.to -> graph.findEdge(next.from, next.to).get))
      }
    }
    go(start, List(start), mutable.PriorityQueue[Edge[V, W]]() ++ graph.adjacent(start), Map.empty)
  }
}

object Assigment1 extends App {
  def parseEdge(line: String): Edge[String, Int] = {
    val Array(src, dst, weight)  = line.split(" ")
    Edge(src, dst, weight.toInt)
  }

  val edges = Source.fromFile(args(0)).getLines()
    .filter(line => line != "END OF INPUT" && line.nonEmpty)
    .map(parseEdge)

  val graph = Graph.fromEdges[String, Int](edges.toSet)

  UniformCostSearch.findShortestPath(graph, args(1), args(2)) match {
    case Nil =>
      List("distance: infinity", "route:", "none").foreach(println)
    case path =>
      println(s"distance: ${path.map(_.weight).sum} km")
      println("route:")
      path.foreach(edge => println(s"${edge.from} to ${edge.to}, ${edge.weight} km"))
  }
}
