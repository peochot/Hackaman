package actors

import akka.actor.Actor
import database.Database
import domain.{CommandWithSender, Question, Response}
import repository.{AnswerRepository, UserRepository}

/**
  * Created by beochot on 5/1/2017.
  */

class AnswerAnalyzer(userRepository: UserRepository, answerRepository: AnswerRepository) extends Actor {
    val validAnswers = List("A", "B", "C", "D", "E", "a", "b", "c", "d", "e")

    def receive = {
        case CommandWithSender(answer, sender, username) if validAnswers.indexOf(answer) > 0 =>
            val userState = userRepository.getUserState(username)
            val response = userState.map(state => {
                val availableAnswers = answerRepository.getAnswers(state.stage)
                val correctAnswer = answerRepository.getAnswers(state.stage).filter(option => option.correct).head.key

                if (correctAnswer == answer.toUpperCase) {

                    val cac = userRepository.updateStage(state.stage + 1, username)
                    println(cac)
                    val newState = userRepository.getUserState(username).getOrElse(state)
                    if (newState.stage != state.stage) {

                        val newAnswers = answerRepository.getAnswers(newState.stage)
                        val nextQuestion = Question(newState.stage, newState.question, newAnswers)
                        println("kec")
                        Response("Correct ! \n".concat(nextQuestion.toString), newState.toUser, clear = true)
                    }
                } else {
                    val question = Question(state.stage, state.question, availableAnswers)

                    val answersContent = availableAnswers.map(option => option.content)
                    Response("Wrong answer. Try again !! \n".concat(question.toString), state.toUser)
                }
            }).getOrElse(
                Response("Congratulations. You have pass all the tests \n", null, clear = true, end = true)
            )
            sender ! response
        case CommandWithSender(_, sender, _) => sender ! "Error"
    }
}
