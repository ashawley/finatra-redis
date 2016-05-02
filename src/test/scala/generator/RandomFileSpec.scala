/**
 * JavaFileSpec.scala --- Test the JavaFile generator
 */

package com.github.ashawley
package generator

import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.AnyOperators

class RandomFileSpec extends Properties("generator.RandomFile") with RandomFile {

  property("randLines.length") = {
    Prop.forAll { (randFile: random.File) =>
      val nLines = randFile.randLines.length
      (randFile.lineMin <= nLines && nLines <= randFile.lineMax)
    }
  }

  property("randLines.head.length") = {
    Prop.forAll { (randFile: random.File) =>
      val lineLength = randFile.randLines.head.length
      (randFile.colMin <= lineLength && lineLength <= randFile.colMax)
    }
  }
}
