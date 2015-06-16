package controllers

import daos.PostDao
import play.api.mvc._
import skinny.util.JSONStringOps._
import util.LoggerFeature
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Await

object PostController extends Controller with FutureTimeoutFeature with LoggerFeature {

  def index(id:Long, threadId: Long) = Action{ implicit request =>
    val post = Await.result(PostDao.findById(id).map(_.find(_.threadId == threadId)), timeout)
    Ok(toJSONString(post))
  }
}
