package daos

import java.sql.{Date => SqlDate}
import java.util.Date

/**
 * Created by kunihira on 15/05/31.
 */
trait CurrentTimeFeature {
  def currentTime = new SqlDate(new Date().getTime)
}
