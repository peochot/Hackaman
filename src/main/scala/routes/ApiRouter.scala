package routes

import akka.http.scaladsl.server.Directives._

/**
  * Created by beochot on 4/22/2017.
  */

class ApiRouter {
  val route =
      pathPrefix("api") {
        complete("Nooooooooo")
        pathPrefix("login") {
          post {
            complete("Nooooooooo")
          }
        }
      }
}
