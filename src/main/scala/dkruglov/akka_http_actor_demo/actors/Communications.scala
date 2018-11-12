package dkruglov.akka_http_actor_demo.actors

import dkruglov.akka_http_actor_demo.entities.Credentials


object Communications {
  case class IssueToken(userId: String)
  case class ValidateCredentials(credentials: Credentials)
}
