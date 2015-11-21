name := "AI-csc4308"
version := "1.0"
scalaVersion := "2.11.7"
scalacOptions += "-Xexperimental"
logLevel := Level.Error

mainClass in assembly := Some("assignments.hw10.Assignment10")
assemblyOutputPath in assembly := new File("./naive_bayes.jar")

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.3",
 "com.github.cb372" %% "scalacache-guava" % "0.7.0",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.4" % "test"
)
