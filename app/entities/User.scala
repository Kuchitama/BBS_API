package entities

import org.joda.time.DateTime

case class User(id: Long, mailAddress: String, password: String, latstPostTime: DateTime) {

}
