package controllers

import daos.ThreadDao
import entities.Thread
import play.api.mvc._
import skinny.util.JSONStringOps._
import utils.{ConfigFeature, LoggerFeature}

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

object ThreadController extends Controller
with FutureTimeoutFeature
with AuthFeature
with ConfigFeature
with LoggerFeature {

  val SearchLimit = config.getInt("thread.searchLimit")

  def index(id: Long) = Action { implicit request =>
    val thread = Await.result(ThreadDao.findById(id), timeout).getOrElse(throw new IllegalArgumentException(s"Thread[${id}] is not found."))
    Ok(toJSONString(thread))
  }

  def create() = Action { implicit request =>
    request.body.asJson map { jValue =>
      logger.debug(s"======== ${jValue.toString}")

      val title = (jValue \ "title").asOpt[String].find(s => 0 < s.size && s.size <= 40)
        .getOrElse(throw new IllegalArgumentException("Invalid title length."))
      val tags: Seq[Thread.Tag] = (jValue \ "tags").as[List[Thread.Tag]]

      val future = ThreadDao.create(title, tags, ???).flatMap { resultOpt =>
        resultOpt.map { result =>
          ThreadDao.findById(result).map(_.getOrElse(new IllegalArgumentException(s"Created thread is not found.")))
        }.getOrElse(Future.failed(new IllegalArgumentException(s"Created thread is not found.")))
      }.map { thread =>
        Ok(toJSONString(thread))
      }
      Await.result(future, timeout)
    } getOrElse {
      throw new IllegalArgumentException("Expecting Json data")
    }
  }

  def search(q: String, offset: Option[Int] = None, limit: Option[Int] = None, strict: Boolean = true) = Action { implicit request =>
    val user = auth
    val (_offset, _limit) = getOffsetAndLimit(offset, limit)
    val threads = Await.result(ThreadDao.findByTitle(q, _offset, _limit, strict), timeout).toList

    Ok(toJSONString(threads))
  }

  def searchByTags(tags: String, offset: Option[Int] = None, limit: Option[Int] = None) = Action { implicit request =>
    val user = auth
    val _tags: List[Thread.Tag] = tags.split(",").map(_.trim).toList
    val (_offset, _limit) = getOffsetAndLimit(offset, limit)
    val threads = Await.result(ThreadDao.findByTags(_tags, _offset, _limit), timeout).toList

    Ok(toJSONString(threads))
  }

  private def getOffsetAndLimit(offset: Option[Int], limit: Option[Int]): (Int, Int) = {
    val _offset = offset.getOrElse(0)
    val _limit = limit.map(_.min(SearchLimit)).getOrElse(SearchLimit)
    (_offset, _limit)
  }
}
