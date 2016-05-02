package com.github.com.ashawley
package http

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.request.RouteParam

class Lineervice {
  def go(r: Request) = println("Doing it!")
}

case class LineRequest(
  @RouteParam num: Int
)

class LineController extends Controller {

  get("/lineRequest/:num") { request: LineRequest =>
    request
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
