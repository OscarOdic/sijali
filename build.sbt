name := """sijali"""

version := "1.0.0"

scalaVersion := "2.11.8"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

coverageEnabled := true

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.github.gilbertw1" %% "slack-scala-client" % "0.2.1",
  "com.typesafe.akka" %% "akka-actor" % "2.4.14",
  "com.github.melrief" %% "pureconfig" % "0.3.3",
  "org.ow2.chameleon.urlshortener" % "url-shortener-tinyurl" % "0.2.0",
  "org.scalaz" %% "scalaz-core" % "7.2.14"
)

enablePlugins(JavaAppPackaging)