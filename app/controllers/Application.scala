package controllers

import play.api._
import play.api.libs.functional.syntax.toApplicativeOps
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


object Application extends Controller {
  
  def index = Action { implicit request =>
    Ok("application running")
  }
}
