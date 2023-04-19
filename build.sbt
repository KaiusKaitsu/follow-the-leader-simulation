ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.2.2"
/** adding scala swing to create the GUI */
ThisBuild / libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
/** adding parallel collections like parSeq to be able to run 100+ simulants at the same time */
ThisBuild / libraryDependencies +="org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4"
/** adding scala test for writing tests */
ThisBuild / libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.14" % "test"
/** adding play json to use its json tools */
ThisBuild / libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.0-RC7"

lazy val root = (project in file("."))
  .settings(
    name := "Follow the leader -simulation"


  )