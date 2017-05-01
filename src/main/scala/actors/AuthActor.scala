package actors

import akka.actor.Actor
import domain.{FailedAuth, SuccessAuth, UserPass}
import repository.UserRepository

/**
  * Created by beochot on 4/23/2017.
  */
class AuthActor(val userRepository: UserRepository) extends Actor {
    def receive = {
        case u: UserPass =>
            userRepository.findUser(u.username, u.password) match {
                case Some(user) => sender() ! SuccessAuth(s"Welcome ${user.username}", user.username, newUser = false)
                case None =>
                    userRepository.createUser(u.username, u.password) match {
                        case Some(user) => sender() ! SuccessAuth(s"User created successfully.Welcome ${user.username}", user.username, newUser = true)
                        case None => sender() ! FailedAuth("Database error")
                    }

            }
    }
}
