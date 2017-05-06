package routes

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives.{as, complete, entity, extractRequest, get, onComplete, optionalCookie, pathPrefix, post}
import akka.pattern.ask
import akka.util.Timeout
import domain.{Command, CommandRequest, User}
import spray.json.DefaultJsonProtocol

import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
  * Created by pnguyenhuy on 29/04/17.
  */

class GameRouter(val validator: ActorRef) extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val timeout: Timeout = 10 seconds
    import domain.CommandProtocol._
    import domain.UserJsonProtocol._

    val route = pathPrefix("game") {
        post {
            optionalCookie("secret") {
                case Some(secret) => entity(as[Command]) { req =>
                    onComplete(validator ? CommandRequest(secret.value, req.command)) {
                        case Success(result) => result match {
                            case list: List[User] => complete(list)
                            case _ => complete("Yck")
                        }
                        case Failure(ex) => complete(s"""{"test": "Nooooooo"}""")
                    }
                }
                case None => complete("Nooooooooo")
            }
        }
    }

}
