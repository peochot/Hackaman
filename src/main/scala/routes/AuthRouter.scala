package routes

import java.nio.charset.StandardCharsets
import java.util.Base64

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives.{as, complete, entity, onComplete, pathPrefix, post, setCookie}
import domain.{FailedAuth, SuccessAuth, UserPass}
import akka.pattern.ask
import akka.util.Timeout
import spray.json.DefaultJsonProtocol

import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
  * Created by pnguyenhuy on 28/04/17.
  */

class AuthRouter(val authActor: ActorRef) extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val timeout: Timeout = 3 seconds
    import domain.UserJsonProtocol._
    import domain.AuthProtocol._

    val route = pathPrefix("login") {
        post {
            entity(as[UserPass]) {
                process
            }
        }
    }

    // Move this to service class later
    private def process(user: UserPass) = {
        onComplete(authActor ? user) {
            case Success(result: SuccessAuth) => complete(result)
            case _ => complete(403, s"""{"error": "Invalid credentials"}""")
        }
    }
}

