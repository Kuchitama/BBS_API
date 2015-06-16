name := """chatwork-test"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.skinny-framework" %% "skinny-json" % "1.3.18"
) ++ databaseDependencies ++ testDependencies

lazy val databaseDependencies = Seq(
  "com.zaxxer" % "HikariCP-java6" % "2.3.3",
  "com.h2database" % "h2" % "1.3.175",
  "com.typesafe.slick" %% "slick" % "3.0.0",
  "com.github.tototoshi" %% "play-flyway" % "1.2.1",
  "joda-time" % "joda-time" % "2.7",
  "org.joda" % "joda-convert" % "1.7",
  "com.github.tototoshi" %% "slick-joda-mapper" % "2.0.0"
)
lazy val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.scalatestplus" %% "play" % "1.2.0" % "test",
  "jp.t2v" %% "play2-auth-test" % "0.13.2" % "test"
)
