package controllers

import daos.ThreadDao
import org.joda.time.DateTime
import play.api.mvc._
import util.LoggerFeature

import skinny.util.JSONStringOps._
import entities.{User, Thread}

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._
import scalaz._
import Scalaz._
import scalaz.std.scalaFuture._

object ThreadController extends Controller with FutureTimeoutFeature with LoggerFeature {

  def index(id: Long) = Action { implicit request =>
    val thread = Await.result(ThreadDao.findById(id), timeout).getOrElse(throw new IllegalArgumentException(s"Thread[${id}] is not found."))
    Ok(toJSONString(thread))
  }

  def create() = Action { implicit request =>
     request.body.asJson map { jValue =>
       logger.debug(s"======== ${jValue.toString}")

       val title = (jValue \ "title").as[String]
       val tags: Seq[Thread.Tag] = (jValue \ "tags").as[List[Thread.Tag]]

       // TODO get user
       val user  = new User(1, "temp_user@loclhost", "password", DateTime.now())
       val fut = ThreadDao.create(title, tags, user).flatMap{ idOpt =>
         val temp = idOpt.map(id => ThreadDao.findById(id))
           temp.sequence.map(_.flatten)
       }
       val thread = Await.result(fut, timeout).getOrElse(throw new RuntimeException(s"Created thread is not found."))

       Ok(toJSONString(thread))
     } getOrElse {
       BadRequest("Expecting Json data")
     }


  }
}
