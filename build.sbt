name := "AI-csc4308"
version := "1.0"
scalaVersion := "2.11.8"
scalacOptions += "-Xexperimental"
logLevel := Level.Error

mainClass in assembly := Some("assignments.hw9.Assignment9")
assemblyOutputPath in assembly := new File("./dtree.jar")

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.6.1",
  "org.apache.spark" %% "spark-mllib" % "1.6.1",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
 "com.github.cb372" %% "scalacache-guava" % "0.7.0",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.4" % "test"
)
