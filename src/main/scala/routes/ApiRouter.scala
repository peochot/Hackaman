package routes

import actors.AuthActor
import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import domain.User
import spray.json.DefaultJsonProtocol

import scala.concurrent.duration._

/**
  * Created by beochot on 4/22/2017.
  */
class ApiRouter(val authActor: ActorRef) extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userFormat = jsonFormat2(User)
  implicit val timeout: Timeout = 3 seconds

  val route =
      pathPrefix("api") {
        pathPrefix("login") {
          post {
            entity(as[User]) {
              user => onComplete(authActor ? user) {
                case util.Success(f) => complete("""{"auth": "ok"}""")
                case util.Failure(ex) => complete("Nooooooooo")
              }
            }
          }
        }
      }
}
