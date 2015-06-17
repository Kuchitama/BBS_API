package entities

import org.joda.time.DateTime
import slick.driver.H2Driver.api._
import slick.lifted.ProvenShape
import com.github.tototoshi.slick.H2JodaSupport._

class Posts(tag: Tag) extends Table[(Option[Long], Long, String, DateTime, Long, DateTime, Long)](tag, "POST") {
  def id = column[Option[Long]]("ID", O.PrimaryKey, O.AutoInc)
  def threadId = column[Long]("THREAD_ID")
  def content = column[String]("CONTENT")
  def createdAt = column[DateTime]("CREATED_AT")
  def createdBy = column[Long]("CREATED_BY")
  def updatedAt = column[DateTime]("UPDATED_AT")
  def updatedBy= column[Long]("UPDATED_BY")

  def * : ProvenShape[(Option[Long], Long, String, DateTime, Long, DateTime, Long)] = (id, threadId, content, createdAt, createdBy, updatedAt, updatedBy)
}
object Posts {
  val tableQuery = TableQuery[Posts]
}

case class Post(id: Long,
                 threadId: Long,
                 content: String,
                 createdAt: DateTime,
                 createdBy: Long,
                 updatedAt: DateTime,
                 updatedBy: Long)

