package com.twitter.finatra_example

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import com.twitter.finatra.test._
import com.twitter.finatra_example._

class AppSpec extends SpecHelper {

  val app = new App.ExampleApp

  
  "GET /" should "respond 200" in {
    get("/")
    response.body should equal ("ok")
    response.code should equal (200)
  }


}
