/**
 * JavaFile.scala --- Generate java.io.File for Scalacheck tests
 *
 * Create temporary file that is deleted when the JVM exits, see
 * java.io.File.deleteOnExit()
 */

package com.github.ashawley
package generator

import org.scalacheck.Arbitrary
import org.scalacheck.Gen

trait JavaFile {

  val genFileNameStem =
    Gen.alphaStr.suchThat(3 to 127 contains _.length)

  implicit def arbFile: Arbitrary[java.io.File] = Arbitrary {
    for {
      prefix <- genFileNameStem
      suffix <- genFileNameStem
    } yield {
      val f = java.io.File.createTempFile(prefix, suffix)
      f.deleteOnExit
      f
    }
  }
}
