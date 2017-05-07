package actors

import akka.actor.Actor
import database.Database
import domain.{CommandWithSender, Question, Response, User}
import repository.{AnswerRepository, UserRepository}

/**
  * Created by beochot on 5/1/2017.
  */

class AnswerAnalyzer(userRepository: UserRepository, answerRepository: AnswerRepository) extends Actor {
    val validAnswers = List("A", "B", "C", "D", "E", "a", "b", "c", "d", "e")

    def receive = {
        case CommandWithSender(answer, sender, username) if validAnswers.indexOf(answer) > -1 =>
            val userState = userRepository.getUserState(username)
            val response = userState.map(state => {
                val availableAnswers = answerRepository.getAnswers(state.stage)
                val correctAnswer = answerRepository.getAnswers(state.stage).filter(option => option.correct).head.key

                if (correctAnswer == answer.toUpperCase) {
                    userRepository.updateStage(state.stage + 1, state.score + 100,username)
                    userRepository.getUserState(username).map((newState) => {
                        val newAnswers = answerRepository.getAnswers(newState.stage)
                        val nextQuestion = Question(newState.stage, newState.question, newAnswers)

                        Response("Correct ! \n".concat(nextQuestion.toString), newState.toUser, clear = true)
                    }).getOrElse(
                        Response("Congratulations. You have pass all the tests \n", User(username, 0, 0), end = true)
                    )
                } else {
                    val question = Question(state.stage, state.question, availableAnswers)
                    Response("Wrong answer. Try again !! \n".concat(question.toString), state.toUser)
                }
            }).get

            sender ! response
        case CommandWithSender(_, sender, _) => sender ! "Error"
    }
}
