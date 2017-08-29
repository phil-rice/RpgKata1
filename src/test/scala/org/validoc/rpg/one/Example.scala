package org.validoc.rpg.one

import org.validoc.rpg.{DoHealing, HitPoints, RpgCharacter, RpgLanguage}

object Example {

  import RpgLanguage._

  implicit def toHitPoints(i: Int) = HitPoints(i)

  val thrud = RpgCharacter("Thrud the Barbarian")

  val doHealing = new DoHealing[RpgCharacter]()
  thrud.takeDamage(10).heal(100)

  //  val change1 = damageToCharacters(char, badHealth)
  //  val change2 = damageToCharacters(change1, badHealth)
  //  val change3 = damageToCharacters(change2, badHealth)
  //  val change4 = healToCharacter(change3, goodHealth)
  //  val change5 = healToCharacter(change4, goodHealth)
  //
  //  printStatus(change5)
  //
  //
  //  val veryBadHealth = ChangeHealth("PERSON", 800)
  //  val change6 = damageToCharacters(change5, veryBadHealth)
  //
  //  val recoverHealth = ChangeHealth("PERSON", 1000)
  //  healToCharacter(change6, recoverHealth)

}
