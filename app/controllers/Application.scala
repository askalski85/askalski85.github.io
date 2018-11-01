package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.Play.current

import play.api.db._


@Singleton class Application @Inject()(val database: Database, val cc: ControllerComponents) extends AbstractController(cc) {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def db() = Action {
    var out = ""
    database.withConnection { conn =>
      val stmt = conn.createStatement
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)")
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())")
      val rs = stmt.executeQuery("SELECT tick FROM ticks")

      while (rs.next) {
        out += "Read from DB: " + rs.getTimestamp("tick") + "\n"
      }
      true
    }
    Ok(out)
  }
}
