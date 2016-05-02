/**
 * RandomFile.scala --- Generate random.File for Scalacheck tests
 *
 * Create temporary file that is deleted when the JVM exits, see
 * java.io.File.deleteOnExit()
 */

package com.github.ashawley
package generator

import org.scalacheck.Arbitrary
import org.scalacheck.Gen

trait RandomFile {
  implicit def arbRandFile: Arbitrary[random.File] = Arbitrary {
    for {
      nCols <- Gen.choose(0, 78)
      nLines <- Gen.choose(1, 1024)
    } yield {
      new random.File(nCols, nCols, nLines, nLines)
    }
  }
}
