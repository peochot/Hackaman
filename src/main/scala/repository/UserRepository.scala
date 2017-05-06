package repository

import database.Database
import domain.{User, UserState}

/**
  * Created by beochot on 4/23/2017.
  */
class UserRepository {
    val CREATE_USER = """INSERT INTO user (username, password) VALUES(%s, %s)"""
    val GET_AUTH_USER = """SELECT * FROM user WHERE username = '%s' AND password = '%s'"""
    val ALL_USER = """SELECT * FROM user"""
    val FIND_USER_STATE =
        """SELECT * FROM user WHERE username = '%s' JOIN question ON user.stageId = question.id"""

    def createUser(username: String, password: String) =
        Database.insert(CREATE_USER.format(username, password), User.fromResult)

    def findAuthUser(username: String, password: String): Option[User] =
        Database.findOne(GET_AUTH_USER.format(username, password), User.fromResult)

    def getUserState(username: String): Option[UserState] = Database.findOne(FIND_USER_STATE.format(username), UserState.fromResult)

    def getUsers() = Database.findAll(ALL_USER, User.fromResult)
}