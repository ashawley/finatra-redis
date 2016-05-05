package com.github.ashawley

import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators

class SourceSpec extends Properties("Source")
    with generator.JavaFile
    with generator.RandomFile {

  property("line(x)") = {
    Prop.forAll { (f: java.io.File, randFile: random.File) =>
      val x = randFile.randNumLine
      randFile.write(f)
      val lines = scala.io.Source.fromFile(f).getLines
      val lineLength = lines.drop(x - 1).next.length
      (randFile.colMin <= lineLength && lineLength <= randFile.colMax)
    }
  }
}
