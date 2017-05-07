package repository

import database.Database
import domain.{User, UserState}

/**
  * Created by beochot on 4/23/2017.
  */
class UserRepository {
    val CREATE_USER = """INSERT INTO user (USERNAME, PASSWORD) VALUES('%s', '%s')"""
    val GET_AUTH_USER = """SELECT * FROM user WHERE username = '%s' AND password = '%s'"""
    val CHECK_USER = """SELECT * FROM user WHERE username = '%s'"""

    val ALL_USER = """SELECT * FROM user ORDER BY stageId DESC"""
    val FIND_USER_STATE =
        """SELECT * FROM user u JOIN question q ON u.stageId = q.id WHERE username = '%s'"""

    val UPDATE_USER_STAGE = """UPDATE user SET stageId = %d, score = %d WHERE username = '%s'"""

    def createUser(username: String, password: String) =
        Database.insert(CREATE_USER.format(username, password), it => it.getInt(1))

    def findAuthUser(username: String, password: String): Option[User] =
        Database.findOne(GET_AUTH_USER.format(username, password), User.fromResult)

    def getUserState(username: String): Option[UserState] = Database.findOne(FIND_USER_STATE.format(username), UserState.fromResult)
    def checkUser(username: String) = Database.findOne(CHECK_USER.format(username), User.fromResult)
    def getUsers() = Database.findAll(ALL_USER, User.fromResult)
    def updateStage(stageId: Long, score: Long, username: String): Int =
        Database.update(UPDATE_USER_STAGE.format(stageId, score, username))

}