/**
 * TextFile.scala --- Text file loaded in Redis
 */

package com.github.ashawley
package redis

import com.twitter.finagle.redis.util.RedisCluster
import com.twitter.finagle.redis.Client
import com.twitter.finagle.Redis

import com.twitter.finagle.redis.util.StringToChannelBuffer
import com.twitter.finagle.redis.util.CBToString
import org.jboss.netty.buffer.ChannelBuffer

import com.twitter.util.Future
import com.twitter.util.Await
import com.twitter.conversions.time._

class TextFile(file: java.io.File) extends TextFileLike {

  implicit def s2cb(s: String) = StringToChannelBuffer(s)
  def cb2s(cb: ChannelBuffer) = CBToString(cb)

  RedisCluster.start()

  // val client = Client("127.0.0.1:6379")
  val client = Client(RedisCluster.hostAddresses())

  val load: Future[Unit] = Future.join {
    scala.io.Source.fromFile(file).getLines.filterNot(_.isEmpty).zipWithIndex.map {
      case (v, k) => (k -> v)
    }.map {
      case (k, v) => client.set(k.toString, v)
    }.grouped(2048).map {
      // Sending everything to twitter.util.Future.join(:Seq[Future])
      // will cause an OOM, so chunking to Seq of 2048 helps.
      (group: Iterable[Future[Unit]]) => Future.join(group.toSeq)
    }.toSeq
  }

  // val load: Future[Unit] = Future.join(loads)

  print(s"Loading ${file}...")

  load.onSuccess { _ =>
    println("Done")
  }
  load.onFailure { _ =>
    println("FAIL!")
  }

  def line(x: Long): Future[Option[String]] = load.flatMap { _ =>
    client.get((x - 1).toString).map(_.map(cb2s(_)))
  }
}
