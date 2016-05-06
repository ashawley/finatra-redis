/**
 * ServerSpec.scala -- Test Redis commands
 */

package com.github.ashawley

import com.twitter.finagle.redis.util.RedisCluster
import com.twitter.finagle.redis.Client

import com.twitter.finagle.redis.util.StringToChannelBuffer
import com.twitter.finagle.redis.util.CBToString
import org.jboss.netty.buffer.ChannelBuffer

import com.twitter.util.Await
import com.twitter.conversions.time._

import org.specs2.mutable.Specification
import org.specs2.specification.AfterAll

class RedisSpec extends Specification with AfterAll {

  implicit def s2cb(s: String) = StringToChannelBuffer(s)
  implicit def cb2s(cb: ChannelBuffer) = CBToString(cb)

  RedisCluster.start()

  // TODO: Will be available in 6.36.0
  // val client = cluster.newClient
  // cluster.withClient { ... }

  val client = Client(RedisCluster.hostAddresses())

  def afterAll: Unit = RedisCluster.stop

  "RedisClient" should {
    "get(key)" in {
      val f = client.get("key1")
      Await.result(f, 1.second) should beNone
    }

    "set(key)" in {
      val f = client.set("key2", "a")
      Await.result(f, 1.second) must beEqualTo((): Unit)
    }

    "set(key)/get(key)" in {
      val fset = client.set("key3", "b")
      Await.result(fset, 1.second) must beEqualTo((): Unit)
      val fget = client.get("key3")
      Await.result(fget, 1.second) should beSome(s2cb("b"))
    }
  }
}
