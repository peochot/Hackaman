package domain

/**
  * Created by beochot on 5/1/2017.
  */
case class Question(id: Long, content: String, options: List[Answer]) {
    override def toString: String =
        s"$id) $content \n".concat(
            options.foldLeft("")((acc, answer) => acc.concat(s"${answer.key}> ${answer.content} \n"))
        )
}
