package com.github.ashawley
package random

import org.scalacheck.Properties
import org.scalacheck.Prop

class FileSpec extends Properties("random.File") with generator.JavaFile {

  property("write") = {
    Prop.forAll { (f: java.io.File) =>
      val randFile = new File(0, 78, 1, 1024)
      randFile.write(f)
      val nLines = scala.io.Source.fromFile(f).getLines.length
      (randFile.lineMin <= nLines && nLines <= randFile.lineMax)
    }
  }
}
