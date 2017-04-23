package actors

import akka.actor.Actor
import domain.User
import repository.UserRepository
/**
  * Created by beochot on 4/23/2017.
  */
class AuthActor(val userRepository: UserRepository) extends Actor {
  def receive = {
    case u: User => {
      val user = userRepository.findUser(u.username, u.password)
      sender() ! user
    }
  }

}
