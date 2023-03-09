ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.2.2"
ThisBuild / libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
ThisBuild / libraryDependencies +="org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4"
ThisBuild / libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test"

lazy val root = (project in file("."))
  .settings(
    name := "Follow the leader -simulation"


  )