import akka.http.scaladsl.model.StatusCodes.{BadRequest, OK, Unauthorized}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import dkruglov.akka_http_actor_demo.RestService
import dkruglov.akka_http_actor_demo.entities.Credentials
import dkruglov.akka_http_actor_demo.traits.JsonSupport
import dkruglov.akka_http_actor_demo.utils.TimeUtils.GetCurrentTime
import org.scalatest.{BeforeAndAfter, FunSpec, Matchers}

import scala.concurrent.duration._
import akka.testkit._
import dkruglov.akka_http_actor_demo.entities.{ErrorMessage, UserToken}


class ValidateRequirements extends FunSpec with Matchers with
  BeforeAndAfter with ScalatestRouteTest with JsonSupport {

  import TestHelpers._

  implicit val timeout = RouteTestTimeout(15.seconds dilated)

  val restService = new RestService()

  before {
  }

  describe("Actor business logic validation") {
    it ("When we submit wrong Request to server - server just ignore it") (
      Post(s"/v1/auth", Map("username"->"username")) ~> Route.seal(restService.route) ~> check {
        status shouldBe BadRequest
      }
    )
  }

  describe("REST API validation") {
    it ("Health check responds in expected format") {
      Get("/healthcheck") ~> restService.route ~> check {
        status shouldBe OK
        responseAs[String] shouldEqual "Healthcheck"
      }
    }

    it ("If we pass valid credentials - we will got token") {
      val credentials = getRandomValidCredentials
      val tokenPrefix = s"${credentials.username}_${GetCurrentTime()}"

      Post(s"/v1/auth", credentials) ~> restService.route ~> check {
        status shouldBe OK
        val firstDiffIndex = (responseAs[UserToken].token zip tokenPrefix).indexWhere{case (x,y) => x != y}
        firstDiffIndex shouldBe >= (credentials.username.length + 15)
      }
    }

    it ("If we pass INvalid credentials - we will NOT get token") {
      val credentials = getRandomInvalidCredentials
      Post(s"/v1/auth", credentials) ~> restService.route ~> check {
        status shouldBe Unauthorized
        responseAs[ErrorMessage].errorDescription should include ("uppercase")
      }

    }

    it ("If we pass credentials with userId starting with `A` we should not get token") {
      val sameString = s"A${generateValidUsername()}"
      val credentials = Credentials(sameString, sameString.toUpperCase)

      Post(s"/v1/auth", credentials) ~> restService.route ~> check {
        status shouldBe Unauthorized
        responseAs[ErrorMessage].errorDescription should include ("startsWith")
      }
    }

  }

}