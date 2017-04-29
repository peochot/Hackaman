package actors

import akka.actor.{Actor, ActorRef}
import domain.CommandWithSender

/**
  * Created by pnguyenhuy on 29/04/17.
  */
class InputAnalyzer(val pipe: ActorRef) extends Actor {
    def receive = {
        case test@CommandWithSender("list", sender, username) => {
            println(test)
            pipe ! test
        }
    }
}
