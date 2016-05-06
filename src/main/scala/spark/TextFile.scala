/**
 * TextFile.scala --- Text file loaded in to Spark
 */

package com.github.ashawley
package spark

import org.apache.spark.rdd.RDD

import com.twitter.util.Future

class TextFile(file: java.io.File) extends TextFileLike {
  val lineIdx: RDD[(Long, String)] =
    SparkLocal.sc.textFile(file.getCanonicalPath).zipWithIndex.map {
      case (t, k) => (k, t)
    }

  def line(x: Long): Future[Option[String]] = Future.value(lineIdx.lookup(x - 1).headOption)
}
