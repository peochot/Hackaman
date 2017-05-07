package routes

import java.nio.charset.StandardCharsets
import java.util.Base64

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, onComplete, optionalCookie, optionalHeaderValueByName, pathPrefix, post, reject}
import akka.pattern.ask
import akka.util.Timeout
import domain._

import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
  * Created by pnguyenhuy on 29/04/17.
  */

class GameRouter(val validator: ActorRef) extends SprayJsonSupport {
    implicit val timeout: Timeout = 3 seconds
    import domain.CommandProtocol._
    import domain.ResponseProtocol._

    val route = pathPrefix("game") {
        post {
            optionalHeaderValueByName("secret") {
                case Some(secret) => entity(as[Command]) { req =>
                    onComplete(validator ? CommandRequest(secret, req.command)) {
                        case Success(response: Response) => complete(response)
                        case _ => complete(500, s"""{"error": "Internal server error"}""")
                    }
                }
                case None => complete(443, s"""{"error": "Unauthorized"}""")
            }
        }
    }
}
