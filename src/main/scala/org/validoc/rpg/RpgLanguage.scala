package org.validoc.rpg

trait RpgLanguage {

  implicit class PimpDoDamage[T](t: T)(implicit doDamage: DoDamage[T]) {
    def takeDamage(hitPoints: HitPoints) = doDamage(hitPoints)(t)
  }

  implicit class PimpHeal[T](t: T)(implicit healing: DoHealing[T]) {
    def heal(hitPoints: HitPoints) = healing(hitPoints)(t)
  }

}

object RpgLanguage extends RpgLanguage