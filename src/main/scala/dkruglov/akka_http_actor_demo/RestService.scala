package dkruglov.akka_http_actor_demo

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

import com.typesafe.scalalogging.LazyLogging

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout

import dkruglov.akka_http_actor_demo.actors.Communications.{IssueToken, ValidateCredentials}
import dkruglov.akka_http_actor_demo.actors.{Authenticator, TokenIssuer}
import dkruglov.akka_http_actor_demo.entities.{Credentials, User, UserToken}
import dkruglov.akka_http_actor_demo.traits.{CorsSupport, JsonSupport, SimpleAsyncTokenService}
import dkruglov.akka_http_actor_demo.utils.TimeUtils.SleepForRandomTimeout
import dkruglov.akka_http_actor_demo.entities.{ErrorMessage, User, UserToken}


class RestService(implicit executionContext: ExecutionContext, actorSystem: ActorSystem) extends SimpleAsyncTokenService
  with LazyLogging with JsonSupport with CorsSupport {

  implicit val timeout = Timeout(15 seconds)

  val authenticator = actorSystem.actorOf(Props(new Authenticator()), "Authenticator")
  val tokenIssuer = actorSystem.actorOf(Props(new TokenIssuer()), "TokenIssuer")

  def ValidateCredentialsImpl(credentials: Credentials) = {

    SleepForRandomTimeout()

    if (credentials.username.toUpperCase equals credentials.password)
      User(credentials.username)
    else
      throw new Exception("password does not matches the username in uppercase")
  }

  def requestToken(credentials: Credentials): Future[UserToken] = {
    val authentification = (authenticator ? ValidateCredentials(credentials)).mapTo[User]
    def issueToken(user: User) = (tokenIssuer ? IssueToken(user.userId)).mapTo[UserToken]

    authentification flatMap issueToken
  }

  val route: Route =  corsHandler {

    path("healthcheck") {
      get {
        complete(StatusCodes.OK, "Healthcheck")
      }
    } ~
      pathPrefix("v1") {
        pathPrefix("auth") {
          post {
            entity(as[Credentials]) { credentials => onComplete(requestToken(credentials)) {
                  case Success(userToken) => complete(StatusCodes.OK, userToken)
                  case Failure(e) => complete(StatusCodes.Unauthorized, ErrorMessage(e.getMessage))
              }
            }
          }
        }
      }
  }
}
