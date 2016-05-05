package com.github.ashawley
package http

import com.twitter.inject.server.FeatureTest
import com.twitter.inject.Mockito
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.finagle.http.Status
import com.google.inject.testing.fieldbinder.Bind

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

  "/line/1" should {
    "Ok" in {
      server.httpGet("/line/1", andExpect = Status(413))
    }
  }
}
