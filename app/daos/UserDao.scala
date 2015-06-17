package daos

import entities._
import org.joda.time.DateTime
import slick.driver.H2Driver.api._
import utils.ConfigFeature

import scala.concurrent.ExecutionContext.Implicits.global

import utils.StringToHash._

object UserDao extends UseDBFeature with CurrentTimeFeature with ConfigFeature {

  val LimitOfPostCount: Int = config.getInt("thread.limitPostCount")

  def findById(id: Long) = useDB{ db =>
    val filterQuery = for(user <- Users.tableQuery if user.id === id) yield user
    db.run(filterQuery.result).map(_.headOption).map(_.map(asUser))
  }

  def create(mailAddress: String, password: String, name: String, now:DateTime = currentTime) = useDB{ db =>
      val value = (None, mailAddress, password.toHash.value, name, now, now)
      val users = Users.tableQuery
      val insert= (users returning users.map(_.id) into ((post, id) => post.copy(_1 = Some(id)))) += value
      db.run(insert).map(_._1.flatten)
  }

  def asUser: PartialFunction[(Option[Long], String, String, String, DateTime, DateTime), User] = {
    case (id: Option[Long], mailAddress: String, password: String, name: String, createdTime: DateTime, updatedTime: DateTime) => {
      User(id.get, mailAddress, password, name, createdTime,updatedTime)
    }
  }
}
