package assigments

import scala.annotation.tailrec
import scala.collection.mutable
import scala.io.Source
import scala.math.Ordered.orderingToOrdered

case class Edge[V, C: Numeric](src: V, dst: V, cost: C) extends Ordered[Edge[V, C]] {
  def reverse: Edge[V, C] = Edge(dst, src, cost)
  def compare(that: Edge[V, C]): Int = (this.cost compare that.cost) * -1
}

trait Graph[V, C] {
  protected def adjacencyMap: Map[V, Set[Edge[V, C]]]
  def vertexes: Set[V] = adjacencyMap.keys.toSet
  def edges: Set[Edge[V, C]]
  def adjacent(v: V): Set[Edge[V, C]] = adjacencyMap.getOrElse(v, Set.empty)
  def degree(v: V): Int = adjacencyMap.get(v).map(_.size).getOrElse(0)
  def findEdge(src: V, dst: V): Option[Edge[V, C]] = adjacencyMap.get(src).flatMap(_.find(_.dst == dst))
}

object Graph {
  def fromEdges[V, C](_edges: Set[Edge[V, C]]): Graph[V, C] = new Graph[V, C] {
    protected def adjacencyMap: Map[V, Set[Edge[V, C]]] = (edges ++ edges.map(_.reverse)).groupBy(_.src)
    def edges: Set[Edge[V, C]] = _edges
  }
}

object UniformCostSearch {

  def findShortestPath[V, C: Numeric](graph: Graph[V, C], start: V, dst: V): List[Edge[V, C]] = {
    val tree = makeSearchTree(graph, start, dst)

    @tailrec
    def go(node: V, path: List[Edge[V, C]]): List[Edge[V, C]] = {
      tree.get(node) match {
        case Some(edge) => go(edge.src, path :+ edge)
        case None       => path.reverse
      }
    }
    go(dst, Nil)
  }

  private def makeSearchTree[V, C](graph: Graph[V, C], start: V, dst: V)(implicit num: Numeric[C]): Map[V, Edge[V, C]] = {
    @tailrec
    def go(current: V, visited: Set[V], open: mutable.PriorityQueue[Edge[V, C]], tree: Map[V, Edge[V, C]]): Map[V, Edge[V, C]] = {
      if (current == dst) return tree
      if (open.isEmpty) return Map.empty

      val next = open.dequeue()
      if (visited.contains(next.dst)) {
        go(current, visited, open, tree)
      } else {
        open ++= graph.adjacent(next.dst)
          .filter(adjEdge => !visited.contains(adjEdge.dst))
          .map(adjEdge => adjEdge.copy[V, C](cost = num.plus(adjEdge.cost, next.cost)))

        go(next.dst, visited + next.dst, open, tree + (next.dst -> graph.findEdge(next.src, next.dst).get))
      }
    }
    go(start, Set(start), mutable.PriorityQueue[Edge[V, C]]() ++ graph.adjacent(start), Map.empty)
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
      println(s"distance: ${path.map(_.cost).sum} km")
      println("route:")
      path.foreach(edge => println(s"${edge.src} to ${edge.dst}, ${edge.cost} km"))
  }
}
