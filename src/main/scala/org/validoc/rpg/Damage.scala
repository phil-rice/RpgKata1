package org.validoc.rpg

trait ReceiveDamage[T] extends (HitPoints => T => T)

trait KillIfNeeded[T] extends (T => T)

class DoDamage[Done](implicit takeDamage: ReceiveDamage[Done], killIfNeeded: KillIfNeeded[Done]) extends (HitPoints => Done => Done) {
  def apply(hitPoints: HitPoints) = takeDamage(hitPoints) andThen killIfNeeded
}

object DoDamage {
  implicit def doDamage[Done](implicit takeDamage: ReceiveDamage[Done], killIfNeeded: KillIfNeeded[Done]) = new DoDamage[Done]
}

