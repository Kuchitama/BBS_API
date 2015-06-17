package controllers

import entities.User
import org.joda.time.DateTime
import play.api.mvc._

trait AuthFeature {
  def auth(implicit request: Request[_]):User = {
    // TODO auth
    User(1, "test@localhost", "password", "name" , DateTime.now(), DateTime.now())
  }
}
