package com.github.com.ashawley
package http

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.request.RouteParam

case class LineRequest(
  @RouteParam num: Int
)

class LineController extends Controller {

  get("/") { _: Request =>
    response.ok.html("<h1>Line Server</h1>")
  }

  get("/lineParam/:num") { request: Request =>
    request.params("num") // Text
  }

  get("/lineRequest/:num") { request: LineRequest =>
    request // JSON
  }

  get("/line/:num") { request: LineRequest =>
    Some("line") match {
      case Some(l: String) => {
        response.
          ok
          .contentType("text/plain")
          .body(l)
      }
      case _ => {
        response.
          status(413)
          .contentType("text/plain")
          .body("Not found")
      }
    }
  }
}
