/**
 * LineController.scala --- Finatra routes
 */

package com.github.ashawley
package http

import com.google.inject.Inject
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.request.RouteParam
import com.twitter.util.Future

case class LineRequest(
  @RouteParam num: Int
)

class LineController @Inject() (
    textFile: TextFileLike
) extends Controller {

  get("/") { _: Request =>
    response.ok.html("<h1>Line Server</h1>").toFuture
  }

  get("/lineParam/:num") { request: Request =>
    response.ok.body(request.params("num")).toFuture // Text
  }

  get("/lineRequest/:num") { request: LineRequest =>
    response.ok.body(request).toFuture // JSON
  }

  get("/line/:num") { request: LineRequest =>
    textFile.line(request.num).map { lineOpt =>
      lineOpt match {
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
}
