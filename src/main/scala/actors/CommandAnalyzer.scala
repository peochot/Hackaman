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
            val optionUser = userRepository.getUserState(username)
            val response = optionUser.map(userState =>  {
                val answers = answerRepository.getAnswers(userState.stage)
                val question = Question(userState.stage, userState.question, answers)
                Response(question.toString, userState.toUser)
            }).getOrElse(
                Response("Congratulations. You have pass all the tests \n", User(username, 0, 0), end = true)
            )

            sender ! response
        case CommandWithSender(_, sender, _) =>
            sender ! "error"
    }

    def composeRankMessage(users: List[User]): String =
        s"Current ranking : \n".concat(
            users.foldLeft("")((acc, user) => acc.concat(s"${user.username} ---- Score: ${user.score} ---- Stage: ${user.stage} \n"))
        )

}
