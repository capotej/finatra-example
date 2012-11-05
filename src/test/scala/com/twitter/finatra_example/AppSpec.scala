package com.twitter.finatra_example

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import com.twitter.finatra.test._
import com.twitter.finatra_example._

class AppSpec extends SpecHelper {

  val app = new App.ExampleApp

  
  "GET /hello" should "response with hello world" in {
    get("/hello")
    response.body should equal ("hello world")
    response.code should equal (200)
  }

  "GET /user/foo" should "response with foo" in {
    get("/user/foo")
    response.body should equal ("hello foo")
    response.code should equal (200)
  }


}
