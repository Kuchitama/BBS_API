package entities

import org.joda.time.DateTime
import slick.driver.H2Driver.api._
import slick.lifted.ProvenShape
import com.github.tototoshi.slick.H2JodaSupport._

/**
 * Created by kunihira on 15/05/31.
 */
class Threads(tag: Tag) extends  Table[(Option[Long], String, String, Long, DateTime, DateTime)](tag, "THREAD"){
  def id: Rep[Option[Long]] = column[Option[Long]]("ID")
  def title: Rep[String] = column[String]("TITLE")
  def tags: Rep[String] = column[String]("TAGS")
  def createdAccountId: Rep[Long] = column[Long]("CREATED_ACCOUNT_ID")
  def createdTime: Rep[DateTime] = column[DateTime]("CREATED_TIME")
  def updatedTime: Rep[DateTime] = column[DateTime]("UPDATED_TIME")

  def * : ProvenShape[(Option[Long], String, String, Long, DateTime, DateTime)] = (id, title, tags, createdAccountId, createdTime, updatedTime)
}
object Threads {
  val tableQuery = TableQuery[Threads]
}

case class Thread(id: Long,
  title: String,
  tags: List[Thread.Tag],
  createdAccountId: Long,
  createdTime: DateTime,
  updatedTime: DateTime)

object Thread {
  type Tag = String

  def apply(id: Long,
    title: String,
    tags: String,
    createdAccountId: Long,
    createdTime: DateTime,
    updatedTime: DateTime) = {
      val _tags = tags.split(",").map(_.trim).toList
      new Thread(id, title, _tags, createdAccountId, createdTime, updatedTime)
  }
}

