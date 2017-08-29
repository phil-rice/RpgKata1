package org.validoc.rpg

class HealingSpec extends RpgSpec with RpgLanguage {
  behavior of "DoDamage"

  val startCharacter = "start"
  val initialHealed = "healed"
  val killedIfNeeded = "killedIfNeeded"
  val cappedAtMaxHitpoints = "healedAndCapped"
  val hitpoints = HitPoints(100)

  implicit val receivedHealing = new ReceiveHealing[String] {
    override def apply(v1: HitPoints) = { character =>
      if (v1 != hitpoints) fail
      if (character != startCharacter) fail
      initialHealed
    }
  }

  implicit val capAtMaxHitPoints = new CapAtMaxHitpoints[String]() {
    override def apply(v1: String) = {
      v1 shouldBe initialHealed
      cappedAtMaxHitpoints
    }
  }

  class IsDeadForString(isDead: Boolean) extends IsDead[String] {
    override def apply(v1: String): Boolean = isDead
  }

  it should "do damage by calling 'receive healing' and then 'kill if needed' and then 'capMaxHitpoints' if the character is alive" in {
    implicit val isDead = new IsDeadForString(false)

    startCharacter.heal(hitpoints) shouldBe cappedAtMaxHitpoints
  }

  it should "return the character if the character is dead" in {
    implicit val isDead = new IsDeadForString(true)

    val doDamage = implicitly[DoHealing[String]]
    startCharacter.heal(hitpoints) shouldBe startCharacter
  }

}
