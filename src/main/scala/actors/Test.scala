package actors

import akka.actor.{Actor, ActorRef}
import domain.CommandWithSender
import repository.UserRepository

/**
  * Created by pnguyenhuy on 29/04/17.
  */
class Test(userRepository: UserRepository) extends Actor {
    def receive = {
        case CommandWithSender(c, s, u) => {
            val users = userRepository.getUsers()

            s ! users
        }
    }
}
