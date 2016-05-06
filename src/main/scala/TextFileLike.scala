/**
 * TextFileLike.scala --- Interface to lined text file
 */

package com.github.ashawley

import com.twitter.util.Future

trait TextFileLike {
  def line(x: Long): Future[Option[String]]
}
