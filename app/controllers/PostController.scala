package controllers

import daos.PostDao
import play.api.mvc._
import skinny.util.JSONStringOps._
import utils.LoggerFeature
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.{Future, Await}

object PostController extends Controller with FutureTimeoutFeature with AuthFeature with LoggerFeature {

  def index(id:Long, threadId: Long) = Action{ implicit request =>
    val post = Await.result(PostDao.findById(id).map(_.find(_.threadId == threadId)), timeout)
    Ok(toJSONString(post))
  }

  def create(threadId:Long) = Action.async { implicit request =>
    val user = auth
    request.body.asJson.map { json =>
      val content = (json \ "content").as[String]

      PostDao.create(threadId, content, user).flatMap {idOpt =>
        idOpt.map { id =>
          PostDao.findById(id).map(_.getOrElse(throw new IllegalArgumentException(s"failed to post")))
        }.getOrElse(Future.failed(new IllegalArgumentException(s"failed to post")))
      }.map { post =>
        Ok(toJSONString(post))
      }
    } getOrElse {
      throw new IllegalArgumentException("Expecting Json data")
    }
  }

  def update(id:Long, threadId: Long) = Action{ implicit request =>
    val user  = auth
    request.body.asJson.map { json =>
      val content = (json \ "content").as[String]
      val post = Await.result(PostDao.update(id, threadId, content, user), timeout)
        .getOrElse(throw new RuntimeException("fail to update post"))

      Ok(toJSONString(post))
    } getOrElse {
      throw new IllegalArgumentException("Expecting Json data")
    }
  }

  def delete(id:Long, threadId: Long) = Action {implicit request =>
    val user = auth
    Await.result(PostDao.deleteByIdAndThreadId(id, threadId), timeout)
    Ok("""{result: "deleted post"}""")
  }
}
