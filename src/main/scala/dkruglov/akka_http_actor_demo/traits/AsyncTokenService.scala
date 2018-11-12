package dkruglov.akka_http_actor_demo.traits

import dkruglov.akka_http_actor_demo.entities.{Credentials, UserToken}
import dkruglov.akka_http_actor_demo.entities.{User, UserToken}

import scala.concurrent.{ExecutionContext, Future}

trait AsyncTokenService {
  protected def authenticate(credentials: Credentials): Future[User]
  protected def issueToken(user: User): Future[UserToken]

  def requestToken(credentials: Credentials)(implicit executionContext: ExecutionContext): Future[UserToken] =
    authenticate(credentials) flatMap {user: User => issueToken(user)}
}