package domain

import java.sql.ResultSet

/**
  * Created by beochot on 4/23/2017.
  */
case class User(username: String, password: String)
case class UserWithoutPassword(username: String)

object UserWithoutPassword {
  def fromResultSet(rs: ResultSet): UserWithoutPassword = UserWithoutPassword(rs.getString("username"))
}