package daos

import org.joda.time.DateTime

trait CurrentTimeFeature {
  def currentTime = DateTime.now()
}
