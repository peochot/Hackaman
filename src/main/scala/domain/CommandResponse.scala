package domain

import spray.json.DefaultJsonProtocol

/**
  * Created by pnguyenhuy on 29/04/17.
  */

trait Response


case class CommandResponse(id: Long, content: String, state: UserState, clear: Boolean = false, end: Boolean = false) extends Response

case class ListUserResponse(users: List[User], state: UserState) extends Response

object ResponseProtocol extends DefaultJsonProtocol{
    implicit val userStateFormat = jsonFormat5(UserState.apply)
    implicit val questionFormat = jsonFormat2(Question)
    implicit val userDetailFormat = jsonFormat3(User.apply)
    implicit val commandResponse = jsonFormat5(CommandResponse)
    implicit val listUserResponse = jsonFormat2(ListUserResponse)
}