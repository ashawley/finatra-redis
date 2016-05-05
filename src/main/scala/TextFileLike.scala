/**
 * TextFileLike.scala --- Interface to lined text file
 */

package com.github.ashawley

trait TextFileLike {
  def line(x: Long): Option[String]
}
