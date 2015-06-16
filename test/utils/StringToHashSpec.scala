package utils

import org.scalatest.FlatSpec
import util.{StringToHash, HashedString}

import util.StringToHash._

/**
 * Created by kunihira on 15/05/23.
 */
class StringToHashSpec extends FlatSpec {
  val strToHash:StringToHash = "foobar"
  val hashedString = strToHash.toHash

  "strtoHash" should "generate HashedString" in {
      assert(strToHash.isInstanceOf[StringToHash])
  }
  it should "comversion to HashedString implicitly" in {
    assert(hashedString.count.isInstanceOf[Int])
    assert("foobar" === hashedString)
    assert(("barbazz" === hashedString) == false)
  }

  val  Reg = "^(\\d+):([0-9a-f]+)$".r

  "toHash" should "generate HashedString" in {
    val hashedString = "foobar".toHash
    assert(hashedString.isInstanceOf[HashedString])
    hashedString.value match {
      case Reg(count, hash) => {
        count.toInt
      }
      case _ => fail("Invalid format value")
    }
  }

  "HashedString" should "" in {
    println("rootpassword".toHash(10).value)

    assert(hashedString === "foobar")
    assert((hashedString === "barbazz") == false)
  }
}
