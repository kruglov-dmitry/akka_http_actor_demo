package dkruglov.akka_http_actor_demo.traits

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import dkruglov.akka_http_actor_demo.entities.{Credentials, ErrorMessage, UserToken}
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userTokenFormat = jsonFormat1(UserToken)
  implicit val errorMessageFormat = jsonFormat1(ErrorMessage)
  implicit val credentialsFormat = jsonFormat2(Credentials)
}