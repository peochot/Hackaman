package domain

import java.sql.ResultSet

/**
  * Created by pnguyenhuy on 06/05/17.
  */
case class Answer (key: String, content: String, correct: Boolean)

object Answer {
    def apply(rs: ResultSet): Answer = new Answer(rs.getString("charkey"), rs.getString("content"), rs.getBoolean("correct"))
}
