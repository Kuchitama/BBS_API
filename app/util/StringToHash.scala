package util

import play.api.Logger

import scala.annotation.tailrec
import scala.util.Random
import java.security.MessageDigest

/**
 * Created by kunihira on 15/05/22.
 */
object StringToHash {
  implicit def strtoHash(str: String) = StringToHash(str)
}

case class StringToHash(underlying: String) {
  def toHash:HashedString = HashedString(underlying)
  def toHash(count: Int):HashedString = HashedString(underlying, count)
  def ===(hashed: HashedString): Boolean = HashedString(this.underlying, hashed.count) == hashed
}

object HashedString {
  val logger = Logger(this.getClass)
  val digest = MessageDigest.getInstance("SHA-1")

  def apply(underlying: String, hashingTime: Int = Random.nextInt(50)+1): HashedString = {
    val hashed: String = s"${hashingTime}:${hashing(underlying, hashingTime)}"
    new HashedString(hashed)
  }

  @tailrec
  private def hashing(msg: String, count: Int): String = {
    val hashed = digest.digest(msg.map(_.toByte).toArray).map("%1$x".format(_)).mkString
    if (count <= 1) hashed else hashing(hashed, count-1)
  }


}
object Extractor{
  object HashedString {
    def unapply(target: HashedString):Option[(Int, String)] = unapply(target.value)
    def unapply(target: String):Option[(Int, String)] = {
      val list:List[String] = target.split(":").toList
      list match {
        case count :: hashed :: Nil => Some(count.toInt, hashed)
        case _ => None
      }
    }
  }
}
class HashedString(_value: String) {
  val value: String = _value
  lazy val count = value.split(":")(0).toInt
  lazy val hash = value.split(":")(1)

  def ===(underlying: String): Boolean = HashedString(underlying, this.count) == this

  override def equals(right: Any) = {
    if (right.isInstanceOf[HashedString]) right.asInstanceOf[HashedString].value == this.value else false
  }

}
