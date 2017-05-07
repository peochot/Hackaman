name := "Hackaman"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= {
  val akkaVersion = "2.4.17"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % "10.0.5",
    "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.4",
    "com.h2database"  %  "h2" % "1.4.193",
    "org.apache.commons" % "commons-dbcp2" % "2.0.1"
  )
}