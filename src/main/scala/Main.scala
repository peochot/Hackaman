import java.sql.{Connection, DriverManager, ResultSet}

import database.Database
import org.apache.commons.dbcp2.BasicDataSourceFactory
import java.util.Properties

import scala.util.{Properties => SProperties}
import actors._
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink}
import domain.{Question, Response}
import org.h2.tools.RunScript
import repository.{AnswerRepository, UserRepository}
import routes.{ApiRouter, StaticRouter}

import scala.concurrent.duration._

/**
  * Created by beochot on 4/22/2017.
  */
object Main {
    def main(args: Array[String]): Unit = {
        val JDBC_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
        //val JDBC_URL = "jdbc:h2:./db/hakaman;AUTO_SERVER=TRUE"
        val JDBC_INIT = s"$JDBC_URL;MODE=MySQL;INIT=RUNSCRIPT FROM 'classpath:create.sql';"

        Class.forName("org.h2.Driver")
        val conn: Connection = DriverManager.getConnection(JDBC_INIT, "test", "test")
        RunScript.execute(JDBC_URL, "test", "test", "classpath:populate.sql", null, false)

        val properties = new Properties()

        properties.put("username", "test")
        properties.put("password", "test")
        properties.put("url", JDBC_URL)
        Database.init(properties)

        implicit val system = ActorSystem("Hakaman")
        implicit val actorMaterializer = ActorMaterializer()

        val userRepo = new UserRepository
        val answerRepo = new AnswerRepository
        val authActor = system.actorOf(Props(new AuthActor(userRepo)), "auth")

        val answerAnalyzer = system.actorOf(Props(new AnswerAnalyzer(userRepo, answerRepo)), "answerAnalyzer")
        val commandAnalyzer = system.actorOf(Props(new CommandAnalyzer(userRepo, answerRepo)), "commandAnalyzer")
        val inputAnalyzer = system.actorOf(Props(new InputAnalyzer(commandAnalyzer, answerAnalyzer)), "analyzer")
        val validator = system.actorOf(Props(new Validator(inputAnalyzer)), "validator")

        val static = new StaticRouter
        val api = new ApiRouter(authActor, validator)
        val routes = static.route ~ api.route

        val port = SProperties.envOrElse("PORT", "9999").toInt

        Http().bindAndHandle(routes, "localhost", port)
        println(s"Server is running at localhost:$port")
    }

}