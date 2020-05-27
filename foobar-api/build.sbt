name := """FooBar API"""
organization := "com.microsoft"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.11"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "com.jason-goodwin" %% "authentikat-jwt" % "0.4.5"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.microsoft.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.microsoft.binders._"
