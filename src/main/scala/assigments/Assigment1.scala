package assigments

import scala.annotation.tailrec
import scala.collection.mutable
import scala.io.Source
import scala.math.Ordered.orderingToOrdered

/**
 * Representation of an edge connecting two vertexes
 *
 * @param src a source vertex
 * @param dst a destination vertex
 * @param cost the cost of this edge from src to dst
 * @tparam V the Vertex type
 * @tparam C the Cost type that is some Numeric type (Int, Long, Double, etc...)
 */
case class Edge[V, C: Numeric](src: V, dst: V, cost: C) extends Ordered[Edge[V, C]] {
  /** @return a new Edge with the src and dst switched */
  def reverse: Edge[V, C] = Edge(dst, src, cost)

  override def compare(that: Edge[V, C]): Int = (this.cost compare that.cost) * -1 // order edges by cost from smallest first
}

/**
 * Representation of a Weighted Graph
 *
 * @tparam V the Vertex type
 * @tparam C the Cost type that is some Numeric type (Int, Long, Double, etc...)
 */
trait Graph[V, C] {
  protected def adjacencyMap: Map[V, Set[Edge[V, C]]]

  /** @return all vertexes in the graph */
  def vertexes: Set[V] = adjacencyMap.keys.toSet

  /** @return all edges in the graph */
  def edges: Set[Edge[V, C]]

  /**
   * @param v vertex
   * @return a set of edges connected to the given vertex
   */
  def adjacent(v: V): Set[Edge[V, C]] = adjacencyMap.getOrElse(v, Set.empty)

  /**
   * Search for an edge within the graph that matches the src and dst vertex
   *
   * @param src vertex
   * @param dst vertex
   * @return [[Some]] found edge or else [[None]]
   */
  def findEdge(src: V, dst: V): Option[Edge[V, C]] = adjacencyMap.get(src).flatMap(_.find(_.dst == dst))
}

object Graph {

  /**
   * Create an undirected graph with a given set of edges.
   * That is, if given Edge("a", "b", 5), an Edge("b", "a", 5) will also be created.
   *
   * @param _edges set of edges
   * @return an undirected graph
   */
  def fromEdges[V, C](_edges: Set[Edge[V, C]]): Graph[V, C] = new Graph[V, C] {
    protected def adjacencyMap: Map[V, Set[Edge[V, C]]] = (edges ++ edges.map(_.reverse)).groupBy(_.src)
    def edges: Set[Edge[V, C]] = _edges
  }
}

object UniformCostSearch {

  /**
   * Find the shortest path between src and dst in a given graph using uniform cost search algo
   *
   * @param graph the graph to perform the search algo
   * @param src the starting vertex
   * @param dst the ending vertex
   * @tparam V the Vertex type
   * @tparam C the Cost type that is some Numeric type (Int, Long, Double, etc...)
   * @return a list of edges with the closest edge to the src at the head of the list.
   *         If no path is found, the list will be empty.
   */
  def findShortestPath[V, C: Numeric](graph: Graph[V, C], src: V, dst: V): List[Edge[V, C]] = {
    val tree = makeSearchTree(graph, src, dst)

    @tailrec
    // starting at the dst(leaf), traverse up the tree to the src(root) while keep track of the path taken
    def go(node: V, path: List[Edge[V, C]]): List[Edge[V, C]] = {
      tree.get(node) match {
        case Some(edge) => go(edge.src, path :+ edge)
        case None       => path.reverse  // reverse the path at the end so the closest edge to the src at the head of the list
      }
    }
    go(dst, Nil)
  }

  // create a search tree with the vertex as the key and the value is the edge that led to that vertex
  private def makeSearchTree[V, C](graph: Graph[V, C], start: V, dst: V)(implicit num: Numeric[C]): Map[V, Edge[V, C]] = {
    @tailrec
    def go(current: V, visited: Set[V], open: mutable.PriorityQueue[Edge[V, C]], tree: Map[V, Edge[V, C]]): Map[V, Edge[V, C]] = {
      if (current == dst) return tree
      if (open.isEmpty) return Map.empty

      val next = open.dequeue()
      if (visited.contains(next.dst) && visited.contains(next.src)) {
        // skip edges with src and dst already been visited
        go(current, visited, open, tree)
      } else {
        // add adjacent edges to the minimum priority queue
        open ++= graph.adjacent(next.dst)
          .filter(adjEdge => !visited.contains(adjEdge.dst)) // remove edges whose dst vertex has already been visited
          .map(adjEdge => adjEdge.copy[V, C](cost = num.plus(adjEdge.cost, next.cost))) // accumulate the cost

        go(next.dst, visited + next.dst, open, tree + (next.dst -> graph.findEdge(next.src, next.dst).get))
      }
    }
    go(start, Set(start), mutable.PriorityQueue[Edge[V, C]]() ++ graph.adjacent(start), Map.empty)
  }
}

object Assigment1 extends App { // program entry point

  def parseEdge(line: String): Edge[String, Int] = {
    val Array(src, dst, cost) = line.split(" ")
    Edge(src, dst, cost.toInt)
  }

  val edges = Source.fromFile(args(0)).getLines()
    .filter(line => line != "END OF INPUT" && line.nonEmpty)
    .map(parseEdge)

  val graph = Graph.fromEdges[String, Int](edges.toSet)

  UniformCostSearch.findShortestPath(graph, args(1), args(2)) match {
    case Nil  => // no path found
      List("distance: infinity", "route:", "none").foreach(println)
    case path => // found the shortest path
      println(s"distance: ${path.map(_.cost).sum} km")
      println("route:")
      path.foreach(edge => println(s"${edge.src} to ${edge.dst}, ${edge.cost} km"))
  }
}
