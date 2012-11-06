package com.twitter.finatra_example

import com.twitter.finatra._

object App {

  
  class ExampleApp extends Controller {

    /**
     * Basic Example
     *
     * curl http://localhost:7070/hello => "hello world"
     */
    get("/hello") { request =>
      render.plain("hello world").toFuture
    }

    /**
     * Route parameters
     *
     * curl http://localhost:7070/user/dave => "hello dave"
     */
    get("/user/:username") { request =>
      val username = request.routeParams.getOrElse("username", "default_user")
      render.plain("hello " + username).toFuture
    }

    /**
     * Setting Headers
     *
     * curl -I http://localhost:7070/headers => "Foo:Bar"
     */
    get("/headers") { request =>
      render.plain("look at headers").header("Foo", "Bar").toFuture
    }

    /**
     * Rendering json
     *
     * curl -I http://localhost:7070/headers => "Foo:Bar"
     */
    get("/data.json") { request =>
      render.json(Map("foo" -> "bar")).toFuture
    }

    /**
     * Query params
     *
     * curl http://localhost:7070/search?q=foo => "no results for foo"
     */
    get("/search") { request =>
      request.params.get("q") match {
        case Some(q) => render.plain("no results for "+ q).toFuture
        case None    => render.plain("query param q needed").status(500).toFuture
      }
    }

    /**
     * Uploading files
     *
     * curl -F avatar=@/path/to/img http://localhost:7070/profile
     */
    post("/profile") { request =>
      request.multiParams.get("avatar").map { avatar =>
        println("content type is " + avatar.contentType)
        avatar.writeToFile("/tmp/avatar") //writes uploaded avatar to /tmp/avatar
      }
      render.plain("ok").toFuture
    }

    /**
     * Rendering views
     *
     * curl http://localhost:7070/posts
     */
    class AnView extends View {
      val template = "an_view.mustache"
      val some_val = "random value here"
    }

    get("/template") { request =>
      val anView = new AnView
      render.view(anView).toFuture
    }
  }

  val app = new ExampleApp


  def main(args: Array[String]) = {
    FinatraServer.register(app)
    FinatraServer.start()
  }
}
