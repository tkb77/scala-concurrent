name := "scala-concurrent"

version := "0.1"

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.31",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.31" % Test
)