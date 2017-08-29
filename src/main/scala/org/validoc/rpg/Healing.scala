package org.validoc.rpg


trait IsDead[T] extends (T => Boolean)

trait ReceiveHealing[T] extends (HitPoints => T => T)

trait CapAtMaxHitpoints[T] extends (T => T)

class DoHealing[T](implicit healDamage: ReceiveHealing[T], isDead: IsDead[T], capAtMaxHitPoints: CapAtMaxHitpoints[T]) extends (HitPoints => T => T) {

  def apply(hitPoints: HitPoints) = { done =>
    if (isDead(done))
      done
    else
      (healDamage(hitPoints) andThen capAtMaxHitPoints) (done)
  }

}

object DoHealing {
  implicit def doHealing[T](implicit healDamage: ReceiveHealing[T], isDead: IsDead[T], capAtMaxHitPoints: CapAtMaxHitpoints[T]) = new DoHealing[T]
}