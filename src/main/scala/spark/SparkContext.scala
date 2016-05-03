package com.github.ashawley
package spark

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object SparkLocal {
  val sparkConf = new SparkConf().
    setAppName("spark.TextFileSpec")
    .setMaster("local")
    .set("spark.driver.allowMultipleContexts", "true")
    .set("spark.akka.timeout", "60")

  val sc = new SparkContext(sparkConf)
}
