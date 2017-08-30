package org.validoc.rpg


class DoDamageSpec extends RpgSpec with RpgLanguage {

  behavior of "DoDamage"

  val startCharacter = "start"
  val damagedCharacter = "damaged"
  val killedIfNeededCharacter = "killedIfNeeded"
  val hitpoints = HitPoints(100)

  implicit val receivedDamage = new ReceiveDamage[String] {
    override def apply(v1: HitPoints) = { character =>
      if (v1 != hitpoints) fail
      if (character != startCharacter) fail
      damagedCharacter
    }
  }

  implicit val killIfNeeded = new KillIfNeeded[String]() {
    override def apply(v1: String) = {
      if (v1 != damagedCharacter) fail
      killedIfNeededCharacter
    }
  }
  it should "do damage by calling 'receive damage' and then 'kill if needed'" in {
    startCharacter.takeDamage(hitpoints).waitIfNeeded shouldBe killedIfNeededCharacter
  }

}
