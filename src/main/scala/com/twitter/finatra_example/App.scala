package com.twitter.finatra_example

import com.twitter.finatra._
import com.twitter.finatra.ContentType._

object App {

  
  class ExampleApp extends Controller {

    /**
     * Basic Example
     *
     * curl http://localhost:7070/hello => "hello world"
     */
    get("/") { request =>
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


    /**
     * Custom Error Handling
     *
     * curl http://localhost:7070/error
     */
    get("/error")   { request =>
      1234/0
      render.plain("we never make it here").toFuture
    }

    /**
     * Custom Error Handling with custom Exception
     *
     * curl http://localhost:7070/unautorized
     */
    class Unauthorized extends Exception

    get("/unauthorized") { request =>
      throw new Unauthorized
    }

    error { request =>
      request.error match {
        case Some(e:ArithmeticException) =>
          render.status(500).plain("whoops, divide by zero!").toFuture
        case Some(e:Unauthorized) =>
          render.status(401).plain("Not Authorized!").toFuture
        case Some(e:UnsupportedMediaType) =>
          render.status(415).plain("Unsupported Media Type!").toFuture
        case _ =>
          render.status(500).plain("Something went wrong!").toFuture
      }
    }


    /**
     * Custom 404s
     *
     * curl http://localhost:7070/notfound
     */
    notFound { request =>
      render.status(404).plain("not found yo").toFuture
    }


    /**
     * Dispatch based on Content-Type
     *
     * curl http://localhost:7070/index.json
     * curl http://localhost:7070/index.html
     */
    get("/blog/index.:format") { request =>
      respondTo(request) {
        case _:Html => render.html("<h1>Hello</h1>").toFuture
        case _:Json => render.json(Map("value" -> "hello")).toFuture
      }
    }

    /**
     * Also works without :format route using browser Accept header
     *
     * curl -H "Accept: text/html" http://localhost:7070/another/page
     * curl -H "Accept: application/json" http://localhost:7070/another/page
     * curl -H "Accept: foo/bar" http://localhost:7070/another/page
     */

    get("/another/page") { request =>
      respondTo(request) {
        case _:Html => render.plain("an html response").toFuture
        case _:Json => render.plain("an json response").toFuture
        case _:All => render.plain("default fallback response").toFuture
      }
    }
  }

  val app = new ExampleApp

  def main(args: Array[String]) = {
    FinatraServer.register(app)
    FinatraServer.start()
  }
}
