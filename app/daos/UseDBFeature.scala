package daos

import play.api.db.DB
import slick.driver.H2Driver.backend.DatabaseDef
import slick.driver.H2Driver.api._
import play.api.Play.current

/**
 * Created by kunihira on 15/05/20.
 */
trait UseDBFeature {
  protected def useDB[T](f: DatabaseDef=> T):T = {
    val db = Database.forDataSource(DB.getDataSource())
    try f(db) finally db.close()
  }
}
