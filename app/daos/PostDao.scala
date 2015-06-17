package daos

import entities._
import org.joda.time.DateTime
import slick.driver.H2Driver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object PostDao extends UseDBFeature with CurrentTimeFeature {

  def findById(id: Long) = useDB{ db =>
    val filterQuery = for(post <- Posts.tableQuery if post.id === id) yield post
    db.run(filterQuery.result).map(_.headOption).map(_.map(asPost))
  }

  def countByThreadId(threadId: Long) = useDB {db =>
    val query = Posts.tableQuery.filter(_.threadId === threadId).length
    db.run(query.result)
  }

  def validateLimitOfThreadPostCount(threadId: Long): Future[Either[Throwable, Unit]] = countByThreadId(threadId).map { count =>
    if (count <= 1000) Right() else Left(new IllegalStateException(s"thread has limited count posts."))
  }

  def create(threadId: Long, content: String, user: User, now:DateTime = currentTime) = {
    validateLimitOfThreadPostCount(threadId).flatMap{either =>
      either.fold(t=> throw t, right => useDB{ db =>
        val value = (None, threadId, content, now, user.id, now, user.id)
        val posts = Posts.tableQuery
        val insert= (posts returning posts.map(_.id) into ((post, id) => post.copy(_1 = Some(id)))) += value
        db.run(insert).map(_._1.flatten)
      })
    }
  }

  def asPost: PartialFunction[(Option[Long], Long, String, DateTime, Long, DateTime, Long), Post] = {
    case (id: Option[Long], threadId: Long, content: String,  createdTime: DateTime, createdBy: Long, updatedTime: DateTime, updatedBy: Long) => {
      Post(id.get, threadId, content, createdTime,createdBy, updatedTime, updatedBy)
    }
  }
}
