package routes

import java.nio.charset.StandardCharsets
import java.util.Base64

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.headers.HttpCookie
import akka.http.scaladsl.server.Directives.{as, complete, entity, onComplete, pathPrefix, post, setCookie}
import domain.{User, UserWithoutPassword}
import akka.pattern.ask
import akka.util.Timeout
import spray.json.DefaultJsonProtocol

import scala.concurrent.duration._

/**
  * Created by pnguyenhuy on 28/04/17.
  */

class AuthRouter(val authActor: ActorRef) extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val timeout: Timeout = 5 seconds
    import domain.UserJsonProtocol._

    val route = pathPrefix("login") {
        post {
            entity(as[User]) {
                user =>
                    onComplete(authActor ? user) {
                        case util.Success(result) => {
                            result match {
                                case Some(u: UserWithoutPassword) =>
                                    setCookie(HttpCookie("secret", value = Base64.getEncoder.encodeToString("user:pass".getBytes(StandardCharsets.UTF_8)))) {
                                        complete("""{"auth": "ok"}""")
                                    }
                                case _ => complete(500, "ERROR")
                            }
                        }

                        case util.Failure(ex) => complete("Nooooooooo")
                    }
            }
        }
    }
}
