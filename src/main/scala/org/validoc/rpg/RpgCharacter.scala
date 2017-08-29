package org.validoc.rpg


case class RpgCharacter(str: String, hitPoints: HitPoints = HitPoints(1000), alive: Boolean = true)

object RpgCharacter {

  implicit object TakeDamageForRpgCharacter extends ReceiveDamage[RpgCharacter] {
    override def apply(hitPoints: HitPoints) = { character: RpgCharacter => character.copy(hitPoints = character.hitPoints - hitPoints) }
  }

  implicit object HealDamageForRpgCharacter extends ReceiveHealing[RpgCharacter] {

    override def apply(hitPoints: HitPoints): (RpgCharacter) => RpgCharacter = { t =>
      t.copy(hitPoints = t.hitPoints + hitPoints)
    }
  }

  implicit object KillIfNeededForRpgCharacter extends KillIfNeeded[RpgCharacter] {
    override def apply(v1: RpgCharacter): RpgCharacter = {
      if (v1.hitPoints.lessThanZero) v1.copy(alive = false) else v1
    }
  }

  implicit object IsDeadForRpgCharacter extends IsDead[RpgCharacter] {
    override def apply(v1: RpgCharacter): Boolean = !v1.alive
  }

  implicit object CapAtMaxHitpointsForRpgCharacter extends CapAtMaxHitpoints[RpgCharacter] {
    val maximumHitpoints = HitPoints(1000)

    override def apply(v1: RpgCharacter): RpgCharacter =
      if (v1.hitPoints.moreThan(maximumHitpoints)) v1.copy(hitPoints = maximumHitpoints) else v1

  }

}




