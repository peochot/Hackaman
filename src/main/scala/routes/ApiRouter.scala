package routes

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._

/**
  * Created by beochot on 4/22/2017.
  */
class ApiRouter(val authActor: ActorRef, val validator: ActorRef) {
  val authRouter = new AuthRouter(authActor)
  val commandRouter = new GameRouter(validator)

  val route =
      pathPrefix("api") {
          authRouter.route ~
          commandRouter.route
      }
}
