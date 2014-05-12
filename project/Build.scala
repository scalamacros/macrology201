import sbt._
import Keys._

object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "org.scalamacros",
    version := "1.0.0",
    scalaVersion := "2.11.0",
    crossScalaVersions := List("2.10.4", "2.11.0"),
    resolvers += Resolver.sonatypeRepo("snapshots"),
    resolvers += Resolver.sonatypeRepo("releases"),
    scalacOptions ++= Seq(),
    libraryDependencies := {
      CrossVersion.partialVersion(scalaVersion.value) match {
        // if scala 2.11+ is used, quasiquotes are merged into scala-reflect
        case Some((2, scalaMajor)) if scalaMajor >= 11 =>
          libraryDependencies.value
        // in Scala 2.10, quasiquotes are provided by macro paradise
        case Some((2, 10)) =>
          libraryDependencies.value ++ Seq(
            compilerPlugin("org.scalamacros" % "paradise" % "2.0.0" cross CrossVersion.full),
            "org.scalamacros" %% "quasiquotes" % "2.0.0" cross CrossVersion.binary)
      }
    },
    libraryDependencies += "org.scalatest" %% "scalatest" % "2.1.5" % "test"
  )
}

object MyBuild extends Build {
  import BuildSettings._

  lazy val root: Project = Project(
    "root",
    file("."),
    settings = buildSettings ++ Seq(
      run <<= run in Compile in core
    )
  ) aggregate(macros, core)

  lazy val macros: Project = Project(
    "macros",
    file("macros"),
    settings = buildSettings ++ Seq(
      libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided"
    )
  )

  lazy val core: Project = Project(
    "core",
    file("core"),
    settings = buildSettings ++ Seq(
      scalacOptions += "-optimize"
    )
  ) dependsOn(macros)
}
