package repository

import database.Database
import domain.{Answer, User}

/**
  * Created by pnguyenhuy on 06/05/17.
  */
class AnswerRepository {
    val FIND_ANSWER =
        """SELECT * FROM answer WHERE questionId = %d"""

    def getAnswers(questionId: Long) =
        Database.findAll(FIND_ANSWER.format(questionId), Answer.fromResult)
}
