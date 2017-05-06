package domain

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

/**
  * Created by beochot on 5/1/2017.
  */

trait AuthResponse

case class SuccessAuth(message: String, username: String, newUser: Boolean) extends AuthResponse
case class FailedAuth(cause: String) extends AuthResponse


object AuthProtocol extends DefaultJsonProtocol {
    implicit val successFormat: RootJsonFormat[SuccessAuth] = jsonFormat3(SuccessAuth)
    implicit val failedFormat: RootJsonFormat[FailedAuth] = jsonFormat1(FailedAuth)
}