package database
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import javax.sql.DataSource
/**
  * Created by beochot on 4/22/2017.
  */
object Database {
  def init(dataSource: DataSource) = {
    this.dataSource = dataSource
  }

  private var dataSource: DataSource = null
  private val currentConnection = new ThreadLocal[Connection]()

  /**
    * Opens new thread-local connection
    */
  def open(): Connection = {
    val connection = dataSource.getConnection
    currentConnection.set(connection)
    connection
  }

  /**
    * Returns current thread-local connection
    */
  def current(): Connection = currentConnection.get()

  /**
    * Closes current thread-local connection
    */
  def close() {
    currentConnection.get().close()
    currentConnection.set(null)
  }
  def transaction[T](block: () => T): T = {

    val connection = Database.open()
    connection.setAutoCommit(false)

    try {
      val result = block()
      connection.commit()

      return result
    } catch {
      case e: Exception => {
        if (!connection.isClosed)
          connection.rollback()
        throw e
      }

    } finally {
      connection.setAutoCommit(true)
      Database.close()
    }
  }

  def prepare(sql: String, autogenerateKey: Boolean = false) = {
    val connection = Database.open()
    val autoGeneratedKeysFlag = if (autogenerateKey) Statement.RETURN_GENERATED_KEYS else Statement.NO_GENERATED_KEYS
    connection.prepareStatement(sql, autoGeneratedKeysFlag)
  }

  def findOne[T](sql: String, extractor: (ResultSet) => T): Option[T] = {
    val resultSet = prepare(sql).executeQuery()

    if (resultSet.next()) {
      return Option(extractor(resultSet))
    }

    return None
  }

  def findAll[T](sql: String, extractor: (ResultSet) => T): List[T] = {
    val resultSet =  prepare(sql).executeQuery()
    val iterator = new Iterator[T] {
      def hasNext = resultSet.next()
      def next() = extractor(resultSet)
    }.toStream

      iterator.toList
  }

  def insert[T](sql: String, block: (ResultSet) => T): Option[T] = {
    val statement = prepare(sql, autogenerateKey = true)
    statement.executeUpdate()

    val resultSet = statement.getGeneratedKeys

    if (resultSet.next()) {
      return Option(block(resultSet))
    }

    return None
  }

  def update(sql: String): Int = {
    return prepare(sql).executeUpdate()
  }

}



