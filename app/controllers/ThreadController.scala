package controllers

import daos.ThreadDao
import play.api.mvc._

import scala.concurrent.Await
import skinny.util.JSONStringOps._


object ThreadController extends Controller with FutureTimeoutFeature {

  def index(id: Long) = Action { implicit request =>
    val thread = Await.result(ThreadDao.findById(id), timeout).getOrElse(throw new IllegalArgumentException(s"Thread[${id}] is not found."))
    Ok(toJSONString(thread))
  }
}
