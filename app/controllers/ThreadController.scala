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

       val title = (jValue \ "title").as[String]
       val tags: Seq[Thread.Tag] = (jValue \ "tags").as[List[Thread.Tag]]

       // TODO get user
       val user  = new User(1, "temp_user@loclhost", "password", DateTime.now())

       val id = Await.result(ThreadDao.create(title, tags, user), timeout)

       // TODO return created thread info
       Ok(s"""{result: "success:${id}"""")
     } getOrElse {
       BadRequest("Expecting Json data")
     }


  }
}
