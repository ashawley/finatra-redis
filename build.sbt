scalaVersion  := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-feature", "-encoding", "utf8")

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.6.2",
  "org.specs2" %% "specs2-scalacheck" % "3.6.2"
)

scalariformSettings

lazy val writeFile = taskKey[Unit]("Write a random file")

writeFile in Runtime := {

  val f = file(".") / "FILE.txt"
  val log = streams.value.log
  val charset = java.nio.charset.Charset.forName("US-ASCII")
  val nLines = 1024
  val lineLength = 1024 - IO.Newline.length

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

lazy val indexFile = taskKey[Unit]("Index the lines of a file")

indexFile := {

  val f = file(".") / "FILE.txt"
  val log = streams.value.log
  val charset = java.nio.charset.Charset.forName("US-ASCII")
  val nLines = 1024
  val lineLength = 1024 - IO.Newline.length

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
