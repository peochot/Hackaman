package routes

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives.{as, complete, entity, extractRequest, get, onComplete, optionalCookie, pathPrefix, post}
import akka.pattern.ask
import akka.util.Timeout
import domain.{Command, CommandRequest, UserWithoutPassword}
import spray.json.DefaultJsonProtocol

import scala.concurrent.duration._
/**
  * Created by pnguyenhuy on 29/04/17.
  */
class CommandRouter(val validator: ActorRef) extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val timeout: Timeout = 10 seconds
    import domain.CommandProtocol._
    import domain.UserJsonProtocol._

    val route = pathPrefix("command") {
        post {
            optionalCookie("secret") {
                case Some(secret) => entity(as[Command]) { req =>
                    onComplete(validator ? CommandRequest(secret.value, req.command)) {
                        case util.Success(result) => result match {
                            case list: List[UserWithoutPassword] => complete(list)
                            case _ => complete("Yck")
                        }
                        case util.Failure(ex) => complete(s"""{"test": "Nooooooo"}""")
                    }
                }
                case None => complete("Nooooooooo")
            }
        }
    }

}
