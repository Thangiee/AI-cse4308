package assignments

import org.scalacheck.Gen

class GraphSpec extends BaseSpec {

  val genEdge = for {
    src <- Gen.choose(0, 10)
    dst <- Gen.choose(0, 10).retryUntil(_ != src)
    cost <- Gen.choose(1, 20)
  } yield Edge(src, dst)(cost)

  val genEdges = Gen.nonEmptyContainerOf[Set, Edge[Int, Int]](genEdge)

  "Graph.fromEdges" must "create a undirected graph with a given set of edges" in {
    forAll((genEdges, "edge(s)")) { edges =>
      val graph = Graph.fromEdges[Int, Int](edges)

      edges.foreach { edge =>
        graph.findEdge(edge.src, edge.dst) shouldBe Some(edge)
        graph.findEdge(edge.dst, edge.src) shouldBe Some(edge.reverse)
      }
    }
  }

  "adjacent" must "return edges connected to a given vertex" in {
    forAll((genEdges, "edge(s)")) { edges =>
      val graph = Graph.fromEdges[Int, Int](edges)

      edges.foreach { edge =>
        graph.adjacent(edge.src).size shouldEqual graph.edges.count(_.dst == edge.src)
      }
    }
  }

  "vertexes" must "return all vertexes in the graph" in {
    forAll((genEdges, "edge(s)")) { edges =>
      val graph = Graph.fromEdges[Int, Int](edges)

      graph.vertexes.size shouldEqual  edges.flatMap(e => Set(e.src, e.dst)).size
    }
  }
}
