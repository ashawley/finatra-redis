scalaVersion  := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-feature", "-encoding", "utf8", "-language:implicitConversions")

resolvers ++= Seq(
  "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  Resolver.sonatypeRepo("releases"),
  "Twitter Maven" at "https://maven.twttr.com"
)

libraryDependencies ++= Seq(
  "com.twitter.finatra" %% "finatra-http" % "2.1.6",
  "com.twitter.finatra" %% "finatra-http" % "2.1.6" % "test" classifier "tests",
  "com.twitter" %% "finagle-redis" % "6.34.0",
  "com.google.inject.extensions" % "guice-testlib" % "4.0" % "test",
  "com.twitter.inject" %% "inject-server" % "2.1.6" % "test",
  "com.twitter.inject" %% "inject-server" % "2.1.6" % "test" classifier "tests",
  "com.twitter.inject" %% "inject-app" % "2.1.6" % "test",
  "com.twitter.inject" %% "inject-app" % "2.1.6" % "test" classifier "tests",
  "com.twitter.inject" %% "inject-core" % "2.1.6" % "test",
  "com.twitter.inject" %% "inject-core" % "2.1.6" % "test" classifier "tests",
  "com.twitter.inject" %% "inject-modules" % "2.1.6" % "test",
  "com.twitter.inject" %% "inject-modules" % "2.1.6" % "test" classifier "tests",
  "org.mockito" % "mockito-core" % "1.9.5" % "test",
  "org.scalatest" %% "scalatest" % "2.2.3" % "test",
  "org.specs2" %% "specs2-core" % "3.6.2" % "test",
  "org.specs2" %% "specs2-junit" % "3.6.2" % "test",
  "org.specs2" %% "specs2-mock" % "3.6.2" % "test",
  "org.specs2" %% "specs2-scalacheck" % "3.6.2" % "test"
)

scalariformSettings

lazy val writeFile = taskKey[Unit]("Write a random file")

writeFile in Runtime := {

  val f = file(".") / "FILE.txt"
  val log = streams.value.log
  val charset = java.nio.charset.Charset.forName("US-ASCII")
  val lineLength = 1024 - IO.Newline.length
  val nLines = 1024 // 1 MB

  def genChar = scala.util.Random.nextPrintableChar

  def isNewline(c: Char) = "\n\r".contains(c)

  if (f.isFile)
    IO.delete(f)
  
  val stream = Stream.fill(nLines) {
    val s = Stream.fill(lineLength)(genChar).filterNot(isNewline).mkString
    val line = s + IO.Newline
    IO.append(f, line, charset)
  }
  stream.force
  log.info(s"Wrote file to $f")
}
