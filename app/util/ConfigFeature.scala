package util

import com.typesafe.config.{ConfigFactory, Config}
import play.api.Configuration

trait ConfigFeature {
  lazy val config = ConfigFactory.load()
}

object ConfigFeature {
  lazy val config = ConfigFactory.load()
}
