package domain

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

/**
  * Created by beochot on 5/1/2017.
  */

trait AuthResponse

case class SuccessAuth(message: String, username: String, secret: String, newUser: Boolean) extends AuthResponse
case class FailedAuth(cause: String) extends AuthResponse


object AuthProtocol extends DefaultJsonProtocol {
    implicit val successFormat = jsonFormat4(SuccessAuth)
    implicit val failedFormat = jsonFormat1(FailedAuth)
}