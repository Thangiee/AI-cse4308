name := "AI-csc4308"

version := "1.0"

scalaVersion := "2.11.7"

assemblyOutputPath in assembly := new File("./find_route.jar")

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.+" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.4" % "test"
)
