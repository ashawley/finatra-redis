/**
 * LineServer.scala --- HTTP server and App.main
 */

package com.github.ashawley
package http

import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{ CommonFilters, LoggingMDCFilter, TraceIdMDCFilter }
import com.twitter.finagle.http.{ Request, Response }
import com.twitter.finatra.http.routing.HttpRouter

object LineServerMain extends LineServer

class LineServer extends HttpServer {

  override val modules = Seq(TextFileModule)

  override val disableAdminHttpServer = true

  override def configureHttp(router: HttpRouter): Unit = {
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[LineController]
  }
}
