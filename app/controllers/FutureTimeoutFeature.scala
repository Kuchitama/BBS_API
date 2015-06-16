package controllers

import java.util.concurrent.TimeUnit

import com.typesafe.config.{ConfigFactory, Config}
import play.api.Configuration

import scala.concurrent.duration.Duration

trait FutureTimeoutFeature {
  lazy val timeout = Duration(FutureTimeoutFeature.config.getDuration("data.access.timeout", TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS)
}
object FutureTimeoutFeature {
  lazy val config = ConfigFactory.load()
}
