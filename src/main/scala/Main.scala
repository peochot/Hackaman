import java.sql.{Connection, DriverManager, ResultSet}

import database.Database
import org.apache.commons.dbcp2.BasicDataSourceFactory
import java.util.Properties

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import org.h2.tools.RunScript
import routes.{ApiRouter, StaticRouter}

import scala.concurrent.duration._

/**
  * Created by beochot on 4/22/2017.
  */
object Main {
  def main(args: Array[String]): Unit = {
    val JDBC_URL = "jdbc:h2:mem:test"
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

    val static = new StaticRouter
    val api = new ApiRouter
    val routes = static.route ~ api.route

    Http().bindAndHandle(routes,"localhost",8080)
    //Database.findOne("SELECT * from user", test)
  }

  def test(rs: ResultSet): Unit ={
    println(rs.getString("name"))
  }
}