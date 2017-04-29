package domain

/**
  * Created by pnguyenhuy on 29/04/17.
  */

trait Response

case class QuestionResponse(clear: Boolean, content: String, command: String, score: Int) extends Response

case class ListResponse()