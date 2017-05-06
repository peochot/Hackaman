package actors

import akka.actor.{Actor, ActorRef}
import domain.CommandWithSender

/**
  * Created by pnguyenhuy on 29/04/17.
  */
class InputAnalyzer(val commandAnalyzer: ActorRef, answerAnalyzer: ActorRef) extends Actor {
    def receive = {
        case command@CommandWithSender(input, _, _) if input startsWith "-"  =>  commandAnalyzer ! command.copy(command = input.substring(1))
        case answer@CommandWithSender => answerAnalyzer ! answer
        case _ =>  sender() ! "error"
    }
}
