/**
 * LineTest.scala --- Integration test of line server
 */

package com.github.ashawley
package http

import com.twitter.inject.server.FeatureTest
import com.twitter.inject.Mockito
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.finagle.http.Status
import com.google.inject.testing.fieldbinder.Bind

import com.twitter.util.Future

import com.twitter.util.Await
import com.twitter.conversions.time._

class LineTest extends FeatureTest with Mockito {

  override val server = new EmbeddedHttpServer(
    new LineServer,
    verbose = false,
    disableTestLogging = true
  )

  @Bind val textFile = mock[TextFileLike]

  "/" should {
    "Found" in {
      server.httpGet("/", andExpect = Status(200))
    }
  }

  "/line/0" should {
    "413" in {
      textFile.line(anyLong).returns(Future(None))

      server.httpGet("/line/1", andExpect = Status(413))
    }
  }

  "/line/1" should {
    "200" in {
      textFile.line(anyLong).returns(Future(Some("x")))

      server.httpGet("/line/1", andExpect = Status(200))
    }
  }
}
