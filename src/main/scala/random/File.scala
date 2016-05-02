/**
 * File.scala --- Randomly generate  file of min-max lines and line length
 *
 * {{{
 * scala> val randFile = new com.github.ashawley.random.File(0, 78, 1, 2)
 * randFile: com.github.ashawley.random.File = File(0, 78, 1, 2)
 *
 * scala> randFile.write(new java.io.File("FILE.txt"))
 *
 * scala> scala.io.Source.fromFile("FILE.txt")getLines.foreach(println)
 * fpCrEzOLBUr5HeINI5mGvLtlZuwlxgvHcYSkH9tbiQPCL
 * H4cn49XDU25DjtqiNYKMxU8UqQli3HKJbk13vz4Z3HRtHnuF
 * }}}
 */

package com.github.ashawley
package random

import scala.collection.immutable.Stream

class File(
    val colMin: Int,
    val colMax: Int,
    val lineMin: Int,
    val lineMax: Int
) {

  override def toString = s"File($colMin, $colMax, $lineMin, $lineMax)"

  def rand(min: Int, max: Int): Int = (min, max) match {
    case (min, max) if (min == max) => min
    case (min, max) if (min > max) =>
      throw new IllegalArgumentException(s"$min must be greater than $max")
    case (min, max) if (min < max) => {
      val range = scala.math.abs(max - min)
      scala.math.abs(scala.util.Random.nextInt(range) + min + 1)
    }
  }

  def randString: String =
    scala.util.Random.nextString(rand(colMin, colMax))

  def randNumLine: Int = rand(lineMin, lineMax)

  def randLines: Stream[String] =
    for {
      l <- Stream.range(0, randNumLine)
    } yield {
      randString
    }

  def isNewline(c: Char) = "\n\r".contains(c)

  def write(file: java.io.File): Unit = {
    val out = new java.io.PrintWriter(file)
    val stream = for {
      line <- randLines
    } yield {
      out.write(line.filterNot(isNewline))
      out.write('\n')
    }
    stream.force // Process everything to the end
    out.close
  }
}
