package com.github.mathieus.gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.util.Random
import io.gatling.core.session.Expression

class FullSimulation extends Simulation {

  val backofficeBaseUrl = Option(System.getenv("BASE_URL_BACKOFFICE")).getOrElse("http://localhost:8080")


  val backofficeApp = scenario("Scenario backoffice")
    .exec(session => session.set("agencyId", Random.nextInt(2000) + 1))
    .exec(
      http("Get first page of credit applications")
        .get("/credit-applications")
        .queryParam("agencyId", "#{agencyId}")
        .queryParam("page", "0")
        .queryParam("size", "10")
        .check(jsonPath("$.content").saveAs("firstPageContent"))
        .check(jsonPath("$.page.totalPages").ofType[Int].saveAs("totalPages"))
    )

//  val agencyApp = scenario("Scenario App B")
//    .exec(http("Request B1").get("/endpointB"))

  val httpBackoffice = http.baseUrl(backofficeBaseUrl)
//  val httpAppB = http.baseUrl("https://appB.example.com")

  setUp(
    backofficeApp.inject(atOnceUsers(1)).protocols(httpBackoffice),
//    scnAppB.inject(atOnceUsers(200)).protocols(httpAppB)
  )

  /*
  // Tirage aléatoire d'une agence entre 1 et 2000
  def randomAgencyId: Expression[Int] = _ => {
    val id = Random.nextInt(2000) + 1 // 1..2000
    scala.util.Success(id)
  }

  def randomPage(totalPages: Int): Int = Random.nextInt(totalPages max 1)

  val scn = scenario("CreditApplication Scenario")
    .exec(session => session.set("agencyId", randomAgencyId(session).get))
    // GET première page
    .exec(
      http("Get first page of credit applications")
        .get("/credit-applications")
        .queryParam("agencyId", "#{agencyId}")
        .queryParam("page", "0")
        .queryParam("size", "10")
        .check(jsonPath("$.content").saveAs("firstPageContent"))
        .check(jsonPath("$.totalPages").ofType[Int].saveAs("totalPages"))
    )
    // Tirage d'une page aléatoire
    .exec(session => {
      val totalPages = session("totalPages").as[Int]
      val targetPage = randomPage(totalPages)
      session.set("targetPage", targetPage)
    })
    // Itération sur les pages 1..targetPage
    .exec(session => session.set("currentPage", 0)) // initialiser la page
    .asLongAs(session => session("currentPage").as[Int] <= session("targetPage").as[Int]) {
      exec(
        http("Get page of credit applications")
          .get("/credit-applications")
          .queryParam("agencyId", "#{agencyId}")
          .queryParam("page", "#{currentPage}")
          .queryParam("size", "10")
          .check(jsonPath("$.content").saveAs("pageContent"))
          .check(jsonPath("$.totalPages").ofType[Int].saveAs("totalPages"))
      )
      .exec(session => {
        val currentPage = session("currentPage").as[Int]
        session.set("currentPage", currentPage + 1)
      })
    }
    // Sélection aléatoire d'une demande sur la page
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
    // GET unitaire
    .exec(
      http("Get single credit application")
        .get("/credit-applications/#{selectedId}")
        .check(status.is(200))
    )
    // PUT modification (ex : modifier termMonths +1)
    .exec(session => {
      val updatedJson =
        s"""
           |{
           |  "termMonths": ${Random.nextInt(12) + 12}
           |}
           |""".stripMargin
      session.set("updateBody", updatedJson)
    })
    .exec(
      http("Update credit application")
        .put("/credit-applications/#{selectedId}")
        .body(StringBody("#{updateBody}")).asJson
        .check(status.is(200))
    )
    // GET unitaire après modification
    .exec(
      http("Get updated credit application")
        .get("/credit-applications/#{selectedId}")
        .check(status.is(200))
    )

  setUp(
    scn.inject(atOnceUsers(1)) // POC : 1 utilisateur
  ).protocols(httpProtocol)
   */

}
