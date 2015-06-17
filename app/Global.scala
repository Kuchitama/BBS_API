
import play.api._
import play.api.mvc.RequestHeader
import play.api.mvc.Results._
import util.LoggerFeature

import scala.concurrent.{ExecutionException, Future}

object Global extends GlobalSettings with LoggerFeature {

  override def onError(request: RequestHeader, ex: Throwable) = {
    val cause = ex.getCause
    cause match {
      case e: IllegalArgumentException => Future.successful(BadRequest(s"""{error: "${e.getMessage}"}"""))
      case _ => {
        logger.error(s"unhandle exception occures: ${ex.getMessage}", ex)
        Future.successful(InternalServerError(s"""{error: "${ex.getMessage}"}"""))
      }
    }
  }
}
