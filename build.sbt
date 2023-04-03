ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.2.2"
ThisBuild / libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
ThisBuild / libraryDependencies +="org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4"
ThisBuild / libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test"
ThisBuild / libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.0-RC7"

lazy val root = (project in file("."))
  .settings(
    name := "Follow the leader -simulation"


  )