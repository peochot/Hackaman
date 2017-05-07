package actors

import akka.actor.Actor
import database.Database
import domain._
import repository.{AnswerRepository, UserRepository}

/**
  * Created by beochot on 5/1/2017.
  */

class CommandAnalyzer (userRepository: UserRepository, answerRepository: AnswerRepository) extends Actor {
    def receive = {
        case CommandWithSender("leaderboard", sender, username) =>
            val users = userRepository.getUsers()
            sender ! Response(composeRankMessage(users), User(username, 0, 0), clear = true)
        case CommandWithSender("start", sender, username) =>
            val user = userRepository.checkUser(username).map((user) => {
                userRepository.getUserState(username).map(userState =>  {
                    val answers = answerRepository.getAnswers(user.stage)
                    val question = Question(userState.stage, userState.question, answers)
                    Response(question.toString, userState.toUser)
                }).getOrElse(
                    Response("Congratulations. You have passed all the tests \n", User(username, 0, 0), end = true)
                )
            }).getOrElse(
                Response("Invalid credentials \n", User(username, 0, 0))
            )

            val response = user
            sender ! response

        case CommandWithSender("reset", sender, username) =>
            userRepository.updateStage(0, 0,username)
            sender ! Response("Reset !!!\n", User(username, 0, 0))
        case CommandWithSender(input, sender, username) if input.startsWith("find") =>
            val response = if(input.length >= 5) {
                val toFind = input.substring(5)
                val found = userRepository.findUsers(toFind)
                Response(composeFoundUsers(found), User(username, 0, 0))
            } else {
                Response("Invalid command \n", User(username, 0, 0))
            }

            sender ! response
        case CommandWithSender(_, sender, _) =>
            sender ! "error"
    }
    def composeFoundUsers(users: List[User]): String = if (users != null && users.length > 0){
        s"Found : \n".concat(
            users.foldLeft("")((acc, user) => acc.concat(s"${user.username} \n")))
    } else {
        "No user found\n"
    }

    def composeRankMessage(users: List[User]): String =
        s"Current ranking : \n".concat(
            users.foldLeft("")((acc, user) => acc.concat(s"${user.username} ---- Score: ${user.score} ---- Stage: ${user.stage} \n"))
        )

}
