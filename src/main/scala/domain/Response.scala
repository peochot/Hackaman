package domain

import spray.json.DefaultJsonProtocol

/**
  * Created by pnguyenhuy on 29/04/17.
  */

case class Response(message: String, state: User, clear: Boolean = false, correct: Boolean = false, end: Boolean = false)

//case class ListUserResponse(users: List[User], state: UserState, clear: Boolean= true) extends Response

object ResponseProtocol extends DefaultJsonProtocol{
//    implicit val userStateFormat = jsonFormat5(UserState.apply)
//    implicit val questionFormat = jsonFormat3(Question)
//    implicit val answerFormat = jsonFormat3(Answer.apply)
    implicit val userDetailFormat = jsonFormat3(User.apply)
    implicit val responseFormat = jsonFormat5(Response)
//    implicit val listUserResponse = jsonFormat3(ListUserResponse)
}