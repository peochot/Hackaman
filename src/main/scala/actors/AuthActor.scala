package actors

import java.nio.charset.StandardCharsets
import java.util.Base64

import akka.actor.Actor
import domain.{FailedAuth, SuccessAuth, UserPass}
import repository.UserRepository

/**
  * Created by beochot on 4/23/2017.
  */
class AuthActor(val userRepository: UserRepository) extends Actor {
    def receive = {
        case u: UserPass =>
            userRepository.findAuthUser(u.username, u.password) match {
                case Some(user) => sender() ! SuccessAuth(s"Welcome ${user.username}", user.username,
                    Base64.getEncoder.encodeToString(user.username.getBytes(StandardCharsets.UTF_8)), newUser = false)
                case None =>
                    userRepository.createUser(u.username, u.password) match {
                        case Some(user) => sender() ! SuccessAuth(s"User created successfully.Welcome ${user.username}", user.username,
                            Base64.getEncoder.encodeToString(user.username.getBytes(StandardCharsets.UTF_8)), newUser = true)
                        case None => sender() ! FailedAuth("Database error")
                    }

            }
    }
}
