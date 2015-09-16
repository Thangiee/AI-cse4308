package assignments.hw1

import assignments.BaseSpec
import org.scalacheck.Gen

class ShortestPathSpec extends BaseSpec {

  val genEdge = for {
    src  <- Gen.choose(0, 7)
    dst  <- Gen.choose(0, 7).retryUntil(_ != src)
    cost <- Gen.choose(1, 200)
  } yield Edge(src, dst)(cost)

  val genGraph = for {
    edges <- Gen.nonEmptyContainerOf[Set, Edge[Int, Int]](genEdge)
    graph = Graph.fromEdges(edges)
    start <- Gen.oneOf(graph.vertexes.toSeq)
    end   <- Gen.oneOf(graph.vertexes.toSeq).retryUntil(_ != start)
  } yield (graph, start, end)

  "Shortest path between two vertexes" must "have the start vertex at the head of the path and end vertex at the end of the path" in {
    forAll(genGraph) { case (graph, start, end) =>
      val path = UniformCostSearch.findShortestPath(graph, start, end)

      whenever(path.nonEmpty) {
        path.head.src shouldEqual start
        path.last.dst shouldEqual end
      }
    }
  }

  it must "cost no greater than the total cost of the whole graph" in {
    forAll(genGraph) { case (graph, start, end) =>
      val path = UniformCostSearch.findShortestPath(graph, start, end)

      whenever(path.nonEmpty) {
        path.map(_.cost).sum should be <= (graph.edges.toList.map(_.cost).sum / 2)
      }
    }
  }

}
