package actors

import akka.actor.{Actor, ActorRef}
import domain.{CommandRequest, CommandWithSender}
import java.util.Base64
import java.nio.charset.StandardCharsets

/**
  * Created by pnguyenhuy on 29/04/17.
  */

class Validator(val pipe: ActorRef) extends Actor {
    def receive = {
        case c: CommandRequest => {
            println(c.token)
            val username = Base64.getDecoder.decode(c.token).toString
            pipe ! CommandWithSender(c.command, sender(), username)
        }
    }
}
