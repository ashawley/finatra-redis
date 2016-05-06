/**
 * TextFileSpec.scala --- Property testing of Spark text file
 */

package com.github.ashawley
package spark

import com.twitter.util.Await
import com.twitter.conversions.time._

import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.AnyOperators

class TextFileSpec extends Properties("spark.TextFile")
    with generator.JavaFile
    with generator.RandomFile {

  property("line(x)") = {
    Prop.forAll { (f: java.io.File, randFile: random.File) =>
      randFile.write(f)
      val textFile = new TextFile(f)
      val x = randFile.randNumLine
      val future = textFile.line(x - 1)
      val line: String = Await.result(future, 1.second).get
      val lineLength: Int = line.length
      (randFile.colMin <= lineLength && lineLength <= randFile.colMax)
    }
  }
}
