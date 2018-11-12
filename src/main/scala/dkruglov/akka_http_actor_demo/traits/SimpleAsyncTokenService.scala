package dkruglov.akka_http_actor_demo.traits

import dkruglov.akka_http_actor_demo.entities.{Credentials, UserToken}

import scala.concurrent.Future


trait SimpleAsyncTokenService {
  def requestToken(credentials: Credentials): Future[UserToken]
}