package dkruglov.akka_http_actor_demo.actors

import akka.actor.Status.Failure
import dkruglov.akka_http_actor_demo.actors.Communications.IssueToken
import dkruglov.akka_http_actor_demo.utils.TimeUtils.{GetCurrentTime, SleepForRandomTimeout}
import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import dkruglov.akka_http_actor_demo.entities.UserToken

import scala.concurrent.Future


class TokenIssuer extends Actor with ActorLogging {

  import context.dispatcher

  def receive = {
    case IssueToken(userId) => IssueTokenImpl(userId) pipeTo sender
  }

  def IssueTokenImpl(userId: String) = Future {

    SleepForRandomTimeout()

    if (userId startsWith "A")
      Failure(new Exception("userId startsWith \"A\""))
    else
      UserToken(generateToken(userId))
  }

  def generateToken(userId: String): String = s"${userId}_${GetCurrentTime()}"
}
