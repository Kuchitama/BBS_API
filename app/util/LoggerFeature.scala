package util

import play.api.Logger

trait LoggerFeature {
  lazy val logger = Logger(this.getClass)
}
