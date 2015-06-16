package daos

import entities._
import org.joda.time.DateTime
import slick.driver.H2Driver.api._

import scala.concurrent.ExecutionContext.Implicits.global

object PostDao extends UseDBFeature with CurrentTimeFeature {

  def findById(id: Long) = useDB{ db =>
    val filterQuery = for(post <- Posts.tableQuery if post.id === id) yield post
    db.run(filterQuery.result).map(_.headOption).map(_.map(asPost))
  }

  def create() = useDB{
    // TODO implement
    ???
  }


  def asPost: PartialFunction[(Option[Long], Long, String, DateTime, Long, DateTime, Long), Post] = {
    case (id: Option[Long], threadId: Long, content: String,  createdTime: DateTime, createdBy: Long, updatedTime: DateTime, updatedBy: Long) => {
      Post(id.get, threadId, content, createdTime,createdBy, updatedTime, updatedBy)
    }
  }
}
