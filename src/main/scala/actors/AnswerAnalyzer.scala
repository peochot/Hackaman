package actors

import akka.actor.Actor
import domain.CommandWithSender
import repository.UserRepository

/**
  * Created by beochot on 5/1/2017.
  */

class AnswerAnalyzer(userRepository: UserRepository) extends Actor {
    def receive = {
        case command@CommandWithSender(answer, sender, username) =>  {
            val userState = userRepository.getUserState(username)
        }
    }
}
