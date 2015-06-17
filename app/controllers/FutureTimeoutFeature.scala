package controllers

import java.util.concurrent.TimeUnit

import utils.ConfigFeature

import scala.concurrent.duration.Duration

trait FutureTimeoutFeature extends ConfigFeature {
  lazy val timeout = Duration(config.getDuration("data.access.timeout", TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS)
}


