package domain
import spray.json._
import DefaultJsonProtocol._
import java.sql.ResultSet

/**
  * Created by beochot on 4/23/2017.
  */
case class User(username: String, password: String)
case class UserWithoutPassword(username: String)

object UserWithoutPassword {
  def fromResultSet(rs: ResultSet): UserWithoutPassword = UserWithoutPassword(rs.getString("username"))
}

object UserJsonProtocol extends DefaultJsonProtocol{
  implicit val userFormat = jsonFormat2(User)
  implicit val userWithoutPassFormat = jsonFormat1(UserWithoutPassword.apply)
}