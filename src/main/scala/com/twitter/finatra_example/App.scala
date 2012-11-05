package com.twitter.finatra_example

import com.twitter.finatra._

object App {

  
  class ExampleApp extends Controller {

    get("/hello") { request =>
      render.plain("hello world").toFuture
    }

    get("/user/:username") { request =>
      val username = request.routeParams.getOrElse("username", "unknown")
      render.plain("hello " + username).toFuture
    }

  }

  val app = new ExampleApp


  def main(args: Array[String]) = {
    FinatraServer.register(app)
    FinatraServer.start()
  }
}
