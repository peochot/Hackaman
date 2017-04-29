import java.sql.{Connection, DriverManager, ResultSet}

import database.Database
import org.apache.commons.dbcp2.BasicDataSourceFactory
import java.util.Properties

import actors.{AuthActor, InputAnalyzer, Test, Validator}
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import org.h2.tools.RunScript
import repository.UserRepository
import routes.{ApiRouter, StaticRouter}

import scala.concurrent.duration._

/**
  * Created by beochot on 4/22/2017.
  */
object Main {
  def main(args: Array[String]): Unit = {
    val JDBC_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
    //val JDBC_URL = "jdbc:h2:./db/hakaman"
    val JDBC_INIT = s"$JDBC_URL;MODE=MySQL;INIT=RUNSCRIPT FROM 'classpath:create.sql';"

    Class.forName("org.h2.Driver")
    val conn: Connection = DriverManager.getConnection(JDBC_INIT, "test", "test")
    RunScript.execute(JDBC_URL, "test", "test", "classpath:populate.sql", null, false)

    val properties = new Properties()

    properties.put("username", "test")
    properties.put("password", "test")
    properties.put("url", JDBC_URL)
    properties.put("validationQuery", "SELECT 1")


    val dataSource = BasicDataSourceFactory.createDataSource(properties)
    Database.init(dataSource)

    implicit val system = ActorSystem("Hakaman")
    implicit val actorMaterializer = ActorMaterializer()

    val userRepo = new UserRepository
    val authActor = system.actorOf(Props(new AuthActor(userRepo)), "auth")

    val tester = system.actorOf(Props(new Test(userRepo)), "test")
    val inputAnalyzer = system.actorOf(Props(new InputAnalyzer(tester)), "analyzer")
    val validator = system.actorOf(Props(new Validator(inputAnalyzer)), "validator")

    val static = new StaticRouter
    val api = new ApiRouter(authActor, validator)
    val routes = static.route ~ api.route

    Http().bindAndHandle(routes, "localhost", 9999)
  }

  def test(rs: ResultSet): Unit ={
    println(rs.getString("username"))
  }
}