package com.twitter.finatra_example

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import com.twitter.finatra.test._
import com.twitter.finatra_example._

class AppSpec extends SpecHelper {

  val app = new App.ExampleApp

  
  "GET /notfound" should "respond 404" in {
    get("/notfound")
    response.body   should equal ("not found yo")
    response.code   should equal (404)
  }

  "GET /error" should "respond 500" in {
    get("/error")
    response.body   should equal ("whoops!")
    response.code   should equal (500)
  }

  "GET /hello" should "respond with hello world" in {
    get("/")
    response.body should equal ("hello world")
  }

  "GET /user/foo" should "responsd with hello foo" in {
    get("/user/foo")
    response.body should equal ("hello foo")
  }

  "GET /headers" should "respond with Foo:Bar" in {
    get("/headers")
    response.getHeader("Foo") should equal("Bar")
  }

  "GET /data.json" should """respond with {"foo":"bar"}""" in {
    get("/data.json")
    response.body should equal("""{"foo":"bar"}""")
  }

  "GET /search?q=foo" should "respond with no results for foo" in {
    get("/search?q=foo")
    response.body should equal("no results for foo")
  }

  "GET /template" should "respond with a rendered template" in {
    get("/template")
    response.body should equal("Your value is random value here")
  }


}
