scalaVersion  := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-feature", "-encoding", "utf8")

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.6.2",
  "org.specs2" %% "specs2-scalacheck" % "3.6.2"
)

scalariformSettings
