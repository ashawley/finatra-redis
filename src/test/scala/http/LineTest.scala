package com.github.ashawley
package http

import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import com.twitter.finagle.http.Status

class LineTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(new LineServer, verbose = false, disableTestLogging = true)

  "/" should {
    "Found" in {
      server.httpGet("/", andExpect = Status(200))
    }
  }

  "/line/1" should {
    "Ok" in {
      server.httpGet("/line/1")
    }
  }
}
