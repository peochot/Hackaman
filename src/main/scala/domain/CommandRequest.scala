package domain

import akka.actor.ActorRef
import spray.json.DefaultJsonProtocol

/**
  * Created by pnguyenhuy on 29/04/17.
  */

trait AbstractCommand

case class CommandRequest(token: String, command: String) extends AbstractCommand

case class CommandWithSender(command: String, sender: ActorRef, username: String) extends AbstractCommand

case class Command(command: String) extends AbstractCommand

object CommandProtocol extends DefaultJsonProtocol{
    implicit val command = jsonFormat1(Command)
}