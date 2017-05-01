package repository

import database.Database
import domain.User

/**
  * Created by beochot on 4/23/2017.
  */
class UserRepository {
  val CREATE_USER = """INSERT INTO user (username, password) VALUES(%s, %s)"""
  val FIND_USER = """SELECT * FROM user WHERE username = '%s' AND password = '%s'"""
  val ALL_USER = """SELECT * FROM user"""

  def createUser(username:String, password: String) =
    Database.insert(CREATE_USER.format(username, password), User.fromResultSet)

  def findUser(username:String, password: String): Option[User] =
    Database.findOne(FIND_USER.format(username, password), User.fromResultSet)

  def getUsers() = Database.findAll(ALL_USER, User.fromResultSet)
}