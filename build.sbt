name := """auth"""
organization := "com.scienaptic"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "com.jason-goodwin" %% "authentikat-jwt" % "0.4.5"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.scienaptic.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.scienaptic.binders._"
