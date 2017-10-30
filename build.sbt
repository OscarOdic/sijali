name := """sijali"""

version := "1.0.0"

scalaVersion := "2.12.2"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

assemblyMergeStrategy in assembly := {
  case PathList("reference.conf") => MergeStrategy.concat
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case _ => MergeStrategy.first
}

assemblyJarName in assembly := "sijali.jar"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "com.github.gilbertw1" %% "slack-scala-client" % "0.2.2",
  "com.typesafe.akka" %% "akka-actor" % "2.5.4",
  "com.typesafe.akka" %% "akka-stream" % "2.5.4",
  "com.github.melrief" %% "pureconfig" % "0.3.3",
  "org.scalaz" %% "scalaz-core" % "7.2.14"
)