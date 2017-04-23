package routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._

/**
  * Created by beochot on 4/22/2017.
  */
class StaticRouter {
  val route =
    get {
      pathSingleSlash {
        redirect("/index.html", StatusCodes.TemporaryRedirect)
      } ~
        getFromResourceDirectory("public")
    }
}
