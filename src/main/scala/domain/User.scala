package domain

import spray.json._
import DefaultJsonProtocol._
import java.sql.ResultSet

/**
  * Created by beochot on 4/23/2017.
  */

case class User(username: String, score: Long, stage: Long)

case class UserPass(username: String, password: String)

case class UserState(username: String, score: Long, stage: Long, question: String) {
    def toUser: User = User(username, score, stage)
}

object User {
    def fromResult(rs: ResultSet): User = User(rs.getString("username"), rs.getLong("score"), rs.getLong("stageId"))
}

object UserState {
    def fromResult(rs: ResultSet): UserState = UserState(rs.getString("username"), rs.getLong("score"), rs.getLong("stageId"), rs.getString("content"))
}

object UserJsonProtocol extends DefaultJsonProtocol {
    implicit val userFormat = jsonFormat2(UserPass)
    implicit val userWithoutPassFormat = jsonFormat3(User.apply)
}