package controllers

import daos.UserDao
import entities.User
import play.api.mvc._
import skinny.util.JSONStringOps._
import utils.LoggerFeature

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

object UserController extends Controller with FutureTimeoutFeature with AuthFeature with LoggerFeature {

  def index(id:Long) = Action{ implicit request =>
    val user = Await.result(UserDao.findById(id), timeout).getOrElse(throw new IllegalArgumentException("user ins not found"))
    Ok(toJSONString(toInfo(user)))
  }

  def create() = Action { implicit request =>
    request.body.asJson.map { json =>
      val mailAddress = (json \ "mail_address").as[String]
      val password = (json \ "password").as[String]
      val name = (json \ "name").as[String]

      val idOpt = Await.result(UserDao.create(mailAddress, password, name), timeout)
      val user = idOpt.flatMap(id => Await.result(UserDao.findById(id), timeout))
      Ok(toJSONString(user))
    } getOrElse {
      throw new IllegalArgumentException("Expecting Json data")
    }
  }

  case class UserInfo(id: Long, name: String)
  def toInfo(user: User) = UserInfo(user.id, user.name)
}
