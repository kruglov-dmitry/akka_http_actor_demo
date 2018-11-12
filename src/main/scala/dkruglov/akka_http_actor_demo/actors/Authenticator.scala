package dkruglov.akka_http_actor_demo.actors

import akka.actor.Status.Failure
import dkruglov.akka_http_actor_demo.actors.Communications.ValidateCredentials
import dkruglov.akka_http_actor_demo.entities.Credentials
import dkruglov.akka_http_actor_demo.utils.TimeUtils.SleepForRandomTimeout
import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import dkruglov.akka_http_actor_demo.entities.User

import scala.concurrent.Future

class Authenticator extends Actor with ActorLogging {

  import context.dispatcher

  def receive = {
    case ValidateCredentials(credentials) => ValidateCredentialsImpl(credentials) pipeTo sender
  }

  def ValidateCredentialsImpl(credentials: Credentials) = Future {

    SleepForRandomTimeout()

    if (credentials.username.toUpperCase equals credentials.password)
      User(credentials.username)
    else
      Failure(new Exception("password does not matches the username in uppercase"))

  }
}
