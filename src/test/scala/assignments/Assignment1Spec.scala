package assignments

class Assignment1Spec extends BaseSpec {

  val edges = Set(
    Edge("Luebeck", "Hamburg")(63),
    Edge("Hamburg", "Bremen")(116),
    Edge("Hamburg", "Hannover")(153),
    Edge("Hamburg", "Berlin")(291),
    Edge("Bremen", "Hannover")(132),
    Edge("Bremen", "Dortmund")(234),
    Edge("Hannover", "Magdeburg")(148),
    Edge("Hannover", "Kassel")(165),
    Edge("Magdeburg", "Berlin")(166),
    Edge("Berlin", "Dresden")(204),
    Edge("Dresden", "Leipzig")(119),
    Edge("Leipzig", "Magdeburg")(125),
    Edge("Dortmund", "Duesseldorf")(69),
    Edge("Kassel", "Frankfurt")(185),
    Edge("Frankfurt", "Dortmund")(221),
    Edge("Frankfurt", "Nuremberg")(222),
    Edge("Leipzig", "Nuremberg")(263),
    Edge("Dortmund", "Saarbruecken")(350),
    Edge("Saarbruecken", "Frankfurt")(177),
    Edge("Saarbruecken", "Karlsruhe")(143),
    Edge("Karlsruhe", "Stuttgart")(71),
    Edge("Stuttgart", "Frankfurt")(200),
    Edge("Stuttgart", "Munich")(215),
    Edge("Stuttgart", "Nuremberg")(207),
    Edge("Nuremberg", "Munich")(171),
    Edge("Manchester", "Birmingham")(84),
    Edge("Birmingham", "Bristol")(85),
    Edge("Birmingham", "London")(117)
  )

  "A shortest path" should "exists between Bremen and Frankfurt" in {
    val g = Graph.fromEdges(edges)
    val path = UniformCostSearch.findShortestPath(g, "Bremen", "Frankfurt")

    path.head shouldEqual Edge("Bremen", "Dortmund")(234)
    path.last shouldEqual Edge("Dortmund", "Frankfurt")(221)
    path.map(_.cost).sum shouldEqual 455
  }

  "No path" should "exists between London and Frankfurt" in {
    val g = Graph.fromEdges(edges)
    val path = UniformCostSearch.findShortestPath(g, "London", "Frankfurt")

    path shouldEqual Nil
  }

}
