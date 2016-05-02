/**
 * JavaFileSpec.scala --- Test the JavaFile generator
 */

package com.github.ashawley
package generator

import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.AnyOperators

class JavaFileSpec extends Properties("generator.JavaFile") with JavaFile {

  property("write") = {
    Prop.forAll { (ss: List[String], f: java.io.File) =>
      val out = new java.io.PrintWriter(f)
      ss.foreach { str =>
        out.write(str.filterNot(ch => "\n\r".contains(ch)))
        out.write('\n')
      }
      out.close
      scala.io.Source.fromFile(f).getLines.length ?= ss.length
    }
  }
}
