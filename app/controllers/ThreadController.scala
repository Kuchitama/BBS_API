package controllers

import daos.ThreadDao
import org.joda.time.DateTime
import play.api.mvc._
import util.LoggerFeature

import scala.concurrent.Await
import skinny.util.JSONStringOps._
import entities.{User, Thread}


object ThreadController extends Controller with FutureTimeoutFeature with LoggerFeature {

  def index(id: Long) = Action { implicit request =>
    val thread = Await.result(ThreadDao.findById(id), timeout).getOrElse(throw new IllegalArgumentException(s"Thread[${id}] is not found."))
    Ok(toJSONString(thread))
  }

  def create() = Action { implicit request =>
     request.body.asJson map { jValue =>
       logger.debug(s"======== ${jValue.toString}")

       val title = Option((jValue \ "title").as[String]).find(s=> 0 < s.size && s.size <= 40)
         .getOrElse(throw new IllegalArgumentException("Invalid title length."))
       val tags: Seq[Thread.Tag] = (jValue \ "tags").as[List[Thread.Tag]]

       // TODO get user
       val user  = new User(1, "temp_user@loclhost", "password", DateTime.now())

       val idOpt = Await.result(ThreadDao.create(title, tags, user), timeout)
       val thread = idOpt.flatMap(id => Await.result(ThreadDao.findById(id), timeout))
                        .getOrElse(throw new IllegalArgumentException(s"Created thread is not found."))

       Ok(toJSONString(thread))
     } getOrElse {
       throw new IllegalArgumentException("Expecting Json data")
     }


  }
}
