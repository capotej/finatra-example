package com.twitter.finatra_example

import com.twitter.finatra._

object App {

  
  class ExampleApp extends Controller {
    get("/") { request =>
      render.plain("ok").toFuture
    }
  }

  val app = new ExampleApp


  def main(args: Array[String]) = {
    FinatraServer.register(app)
    FinatraServer.start()
  }
}
