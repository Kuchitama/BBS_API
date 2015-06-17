package daos

import entities.{User, Thread, Threads}
import org.joda.time.DateTime

import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import com.github.tototoshi.slick.H2JodaSupport._

object ThreadDao extends UseDBFeature with CurrentTimeFeature {

  def findById(id: Long) = useDB{ db =>
    val filterQuery = for(thread <- Threads.tableQuery if thread.id === id) yield thread
    db.run(filterQuery.result).map(_.headOption).map(_.map(asThread))
  }

  /**
   * create Thread
   * @param title
   * @param tags
   * @param user
   * @param now
   */
  def create(title:String, tags: Seq[Thread.Tag], user: User, now: DateTime = currentTime) = useDB{ db =>
    val value = (None, title, tags.mkString(","), user.id, now, now)
    val threads = Threads.tableQuery
    val insert = (threads returning threads.map(_.id) into ((thread, id) => thread.copy(_1 = Some(id)))) += value
    db.run(insert).map(_._1.flatten)
  }

  def findByTitle(title:String, offset:Int, limit:Int, strict: Boolean = true) = useDB { db =>
    val likeState = if(strict) title else s"%${title}%"
    val filter = Threads.tableQuery.filter(_.title like likeState).drop(offset).take(limit)
    db.run(filter.result).map(_.map(asThread))
  }


  def asThread: PartialFunction[(Option[Long], String, String, Long, DateTime, DateTime), Thread] = {
    case (id: Option[Long], title: String, tags: String, accountId: Long, createdTime: DateTime, updatedTime: DateTime) => {
      Thread(id.get, title, tags, accountId, createdTime, updatedTime)
    }
  }
}
