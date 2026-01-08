package com.github.mathieus.gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.util.Random

class FullSimulation extends Simulation {

  val backofficeBaseUrl = Option(System.getenv("BASE_URL_BACKOFFICE")).getOrElse("http://localhost:8280")
  val agencyAppBaseUrl = Option(System.getenv("BASE_URL_AGENCY_APP")).getOrElse("http://localhost:8180")

  def randomPage(totalPages: Int): Int = Random.nextInt(totalPages max 1)

  def fetchRandomApplication(prefix: String) = {
    exec(session => session.set("agencyId", Random.nextInt(2000) + 1))
      .exec(
        http(s"$prefix - Get first page")
          .get("/credit-applications")
          .queryParam("agencyId", "#{agencyId}")
          .queryParam("page", "0")
          .queryParam("size", "10")
          .check(jsonPath("$.page.totalPages").ofType[Int].saveAs("totalPages"))
      )
      .exec(session => session.set("targetPage", randomPage(session("totalPages").as[Int])))
      .exec(session => session.set("currentPage", 0))
      .asLongAs(session => session("currentPage").as[Int] <= session("targetPage").as[Int]) {
        exec(
          http(s"$prefix - Get page of credit applications")
            .get("/credit-applications")
            .queryParam("agencyId", "#{agencyId}")
            .queryParam("page", "#{currentPage}")
            .queryParam("size", "10")
            .check(jsonPath("$.content").saveAs("pageContent"))
        )
          .exec(session => {
            val currentPage = session("currentPage").as[Int]
            session.set("currentPage", currentPage + 1)
          })
      }
      .exec(session => {
        import io.circe.parser._
        val content = session("pageContent").as[String]
        val applications = decode[List[io.circe.Json]](content).getOrElse(List.empty)
        if (applications.nonEmpty) {
          val selected = applications(Random.nextInt(applications.size))
          val id = selected.hcursor.get[Long]("id").getOrElse(0L)
          session.set("selectedId", id)
        } else session
      })
  }

  def updateApplication(prefix: String) = {
    exec(
      http(s"$prefix - Get single app")
        .get("/credit-applications/#{selectedId}")
        .check(status.is(200))
    )
      .exec(session => {
        val body =
          s"""
             {
               "termMonths": ${Random.nextInt(12) + 12}
             }
           """
        session.set("updateBody", body)
      })
      .exec(
        http(s"$prefix - Update app")
          .put("/credit-applications/#{selectedId}")
          .body(StringBody("#{updateBody}")).asJson
          .check(status.is(200))
      )
  }

  def commonWorkflow(prefix: String) =
    repeat(10) {
      exec(fetchRandomApplication(prefix))
        .doIf(session => session.contains("selectedId")) {
          updateApplication(prefix)
            .exec(
              http(s"$prefix - Get updated app")
                .get("/credit-applications/#{selectedId}")
                .check(status.is(200))
            )
        }
    }

  val backofficeApp = scenario("Scenario backoffice").exec(commonWorkflow("BCK"))
  val agencyApp     = scenario("Scenario agency app").exec(commonWorkflow("AG_APP"))

  setUp(
    backofficeApp.inject(atOnceUsers(50)).protocols(http.baseUrl(backofficeBaseUrl)),
    agencyApp.inject(atOnceUsers(200)).protocols(http.baseUrl(agencyAppBaseUrl))
  ).assertions(
    global.successfulRequests.percent.gte(100)
  )
}