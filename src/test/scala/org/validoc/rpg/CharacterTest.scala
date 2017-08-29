package org.validoc.rpg

trait CharacterTestFixture {

  val oneHundredHitPoints = HitPoints(100)
  val nineHundredHitPoints = HitPoints(900)
  val oneThousandHitPoints = HitPoints(1000)
  val tenThousandHitPoints = HitPoints(10000)

  val thrud = RpgCharacter("Thrud the Barbarian")
  val thrud900 = RpgCharacter("Thrud the Barbarian", hitPoints = HitPoints(900))
  val thrud800 = RpgCharacter("Thrud the Barbarian", hitPoints = HitPoints(800))
  val thrudMinus100ButAlive = RpgCharacter("Thrud the Barbarian", hitPoints = HitPoints(-100), alive = true)
  val deadThrud = RpgCharacter("Thrud the Barbarian", hitPoints = HitPoints(0), alive = false)
}

class CharacterTest extends RpgSpec with CharacterTestFixture {


  behavior of "Character"

  it should "be created with 1000 hp" in {
    thrud.hitPoints shouldBe oneThousandHitPoints

  }

  behavior of "ReceiveDamage[RpgCharacter]"

  val receiveDamage = implicitly[ReceiveDamage[RpgCharacter]]

  it should "do the damage to the character returning a new one" in {
    receiveDamage(oneHundredHitPoints)(thrud) shouldBe thrud900
  }


  behavior of "ReceiveHealing[RpgCharacter]"

  val receiveHealing = implicitly[ReceiveHealing[RpgCharacter]]

  it should "heal the character returning a new one" in {
    receiveHealing(oneHundredHitPoints)(thrud900) shouldBe thrud
    receiveHealing(oneHundredHitPoints)(thrud800) shouldBe thrud900
  }

  it should "allow healing over maximum hitpoints (that's a different responsibility)" in {
    receiveHealing(tenThousandHitPoints)(thrud900) shouldBe thrud.copy(hitPoints = HitPoints(10900))
    receiveHealing(tenThousandHitPoints)(thrud800) shouldBe thrud.copy(hitPoints = HitPoints(10800))

  }

  behavior of "CapAtMaximumHitPoints"

  val capAtMaxHitPoints = implicitly[CapAtMaxHitpoints[RpgCharacter]]

  it should "return the character as is, if hitpoints <=1000" in {
    capAtMaxHitPoints(thrud) shouldBe thrud
    capAtMaxHitPoints(thrud800) shouldBe thrud800
    capAtMaxHitPoints(thrudMinus100ButAlive) shouldBe thrudMinus100ButAlive
    capAtMaxHitPoints(thrud.copy(hitPoints = HitPoints(1001))) shouldBe thrud
    capAtMaxHitPoints(thrud.copy(hitPoints = HitPoints(10000))) shouldBe thrud
  }
  behavior of "Kill if needed[RpgCharacter"

  val killIfNeeded = implicitly[KillIfNeeded[RpgCharacter]]

  it should "do nothing to the character with positive hitpoints" in {
    killIfNeeded(thrud) shouldBe thrud
    killIfNeeded(thrud900) shouldBe thrud900
    killIfNeeded(thrud800) shouldBe thrud800
  }

  it should "kill characters with negative hitpoints" in {
    killIfNeeded(thrudMinus100ButAlive) shouldBe thrudMinus100ButAlive.copy(alive = false)
    killIfNeeded(deadThrud) shouldBe deadThrud
  }

  behavior of "IsDead[RpgCharacer]"

  val isDead = implicitly[IsDead[RpgCharacter]]

  it should "report based on the alive flag" in {
    isDead(thrudMinus100ButAlive) shouldBe false
    isDead(thrud900) shouldBe false
    isDead(deadThrud) shouldBe true
  }

}




