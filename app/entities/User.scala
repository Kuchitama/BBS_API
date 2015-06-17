package entities

import org.joda.time.DateTime
import slick.driver.H2Driver.api._
import slick.lifted.ProvenShape
import com.github.tototoshi.slick.H2JodaSupport._

class Users(tag: Tag) extends Table[(Option[Long], String, String, String, DateTime, DateTime)](tag, "USER_DATA") {
  def id = column[Option[Long]]("ID", O.PrimaryKey, O.AutoInc)
  def mailAddress = column[String]("MAILADDRESS")
  def password = column[String]("PASSWORD")
  def name = column[String]("NAME")
  def createdAt = column[DateTime]("CREATED_AT")
  def updatedAt = column[DateTime]("UPDATED_AT")

  def * : ProvenShape[(Option[Long], String, String, String, DateTime, DateTime)] = (id, mailAddress, password, name, createdAt, updatedAt)
}
object Users {
  val tableQuery = TableQuery[Users]
}

case class User(id: Long,
                mailAddress: String,
                password: String,
                name: String,
                createdTime: DateTime,
                updatedTime: DateTime)
