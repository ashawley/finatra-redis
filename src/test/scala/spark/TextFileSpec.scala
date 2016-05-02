package com.github.ashawley
package spark

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.AnyOperators

class TextFileSpec extends Properties("spark.TextFile")
    with generator.JavaFile
    with generator.RandomFile {

  val sparkConf = new SparkConf().
    setAppName("spark.TextFileSpec")
    .setMaster("local")
    .set("spark.driver.allowMultipleContexts", "true")
    .set("spark.akka.timeout", "60")

  val sc = new SparkContext(sparkConf)

  property("read") = {
    Prop.forAll { (f: java.io.File, randFile: random.File) =>
      randFile.write(f)
      val nLines = sc.textFile(f.getCanonicalPath).count
      (randFile.lineMin <= nLines && nLines <= randFile.lineMax)
    }
  }
}
