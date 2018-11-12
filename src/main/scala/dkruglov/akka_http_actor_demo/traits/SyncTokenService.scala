package dkruglov.akka_http_actor_demo.traits

import dkruglov.akka_http_actor_demo.entities.{Credentials, User, UserToken}

trait SyncTokenService {
  protected def authenticate(credentials: Credentials): User
  protected def issueToken(user: User): UserToken

  def requestToken(credentials: Credentials): UserToken = issueToken(authenticate(credentials))
}


