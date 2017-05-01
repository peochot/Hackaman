package routes

import java.nio.charset.StandardCharsets
import java.util.Base64

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.headers.HttpCookie
import akka.http.scaladsl.server.Directives.{as, complete, entity, onComplete, pathPrefix, post, setCookie}
import akka.http.scaladsl.server.Route
import domain.{FailedAuth, UserPass, SuccessAuth}
import akka.pattern.ask
import akka.util.Timeout
import spray.json.DefaultJsonProtocol

import scala.concurrent.duration._

/**
  * Created by pnguyenhuy on 28/04/17.
  */

class AuthRouter(val authActor: ActorRef) extends SprayJsonSupport {
    implicit val timeout: Timeout = 5 seconds
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
            case util.Success(result) =>
                result match {
                    case Some(success: SuccessAuth) =>
                        setCookie(HttpCookie("secret", value = Base64.getEncoder.encodeToString(success.username.getBytes(StandardCharsets.UTF_8)))) {
                            complete(success)
                        }
                    case f: FailedAuth => complete(500, f.cause)
                }

            case util.Failure(ex) => complete("Nooooooooo")
        }
    }
}

