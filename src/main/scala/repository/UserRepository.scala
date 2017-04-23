package repository

import database.Database
import domain.{User, UserWithoutPassword}

/**
  * Created by beochot on 4/23/2017.
  */
class UserRepository {
  val CREATE_USER = """INSERT INTO user (username, password) VALUES(%s, %s)"""
  val FIND_USER = """SELECT * FROM user WHERE username = '%s' AND password = '%s'"""

  def createUser(username:String, password: String) =
    Database.insert(CREATE_USER.format(username, password), UserWithoutPassword.fromResultSet)

  def findUser(username:String, password: String): Option[UserWithoutPassword] =
    Database.findOne("SELECT * FROM user", UserWithoutPassword.fromResultSet)
}
