package com.github.ashawley
package spark

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.AnyOperators

class TextFileSpec extends Properties("spark.TextFile")
    with generator.JavaFile
    with generator.RandomFile {

  property("count") = {
    Prop.forAll { (f: java.io.File, randFile: random.File) =>
      randFile.write(f)
      val nLines = SparkLocal.sc.textFile(f.getCanonicalPath).count
      (randFile.lineMin <= nLines && nLines <= randFile.lineMax)
    }
  }

  property("lookup(x)") = {
    Prop.forAll { (f: java.io.File, randFile: random.File) =>
      randFile.write(f)
      val lines: RDD[String] = SparkLocal.sc.textFile(f.getCanonicalPath)
      val lineIdx: RDD[(Long, String)] = lines.zipWithIndex.map {
        case (t, k) => (k, t)
      }
      val x = randFile.randNumLine
      val line: String = lineIdx.lookup(x - 1).head
      val lineLength: Int = line.length
      (randFile.colMin <= lineLength && lineLength <= randFile.colMax)
    }
  }
}
