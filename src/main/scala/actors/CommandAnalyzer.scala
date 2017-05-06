package actors

import akka.actor.Actor
import domain.{CommandWithSender, ListUserResponse, User}
import repository.{AnswerRepository, UserRepository}

/**
  * Created by beochot on 5/1/2017.
  */

class CommandAnalyzer (userRepository: UserRepository, answerRepository: AnswerRepository) extends Actor {
    def receive = {
        case CommandWithSender("list", sender, username) =>  {
            val optionUser = userRepository.getUserState(username)
            val response = optionUser.map(userState =>  {
                val answers = answerRepository.getAnswers(userState.stage).map(answer => answer.content)
                ListUserResponse(userRepository.getUsers(), userState.copy(answers = answers))
            })

            sender ! response
        }
        case CommandWithSender(_, sender, _) =>  {
            sender ! "error"
        }
    }
}
