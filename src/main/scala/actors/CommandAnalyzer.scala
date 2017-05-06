package actors

import akka.remote.ContainerFormats.ActorRef
import domain.{CommandWithSender, ListUserResponse}
import repository.UserRepository

/**
  * Created by beochot on 5/1/2017.
  */
class CommandAnalyzer (userRepository: UserRepository) {
    def receive = {
        case command@CommandWithSender("list", sender, username) =>  {
            val user = userRepository.getUserState(username)
            sender ! ListUserResponse(userRepository.getUsers(), null)
        }
    }
}
