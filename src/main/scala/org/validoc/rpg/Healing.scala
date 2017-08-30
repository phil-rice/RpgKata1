package org.validoc.rpg

import scala.concurrent.Future


trait IsDead[T] extends (T => Boolean)

trait ReceiveHealing[T] extends (HitPoints => T => T)

trait CapAtMaxHitpoints[T] extends (T => T)

import Futures._

class DoHealing[T](implicit healDamage: ReceiveHealing[T], isDead: IsDead[T], capAtMaxHitPoints: CapAtMaxHitpoints[T]) extends (HitPoints => T => Future[T]) {

  def apply(hitPoints: HitPoints) = { done: T =>
    if (isDead(done))
      done
    else
      (healDamage(hitPoints) andThen capAtMaxHitPoints) (done)
  }.toFutureFn

}

object DoHealing {
  implicit def doHealing[T](implicit healDamage: ReceiveHealing[T], isDead: IsDead[T], capAtMaxHitPoints: CapAtMaxHitpoints[T]) = new DoHealing[T]
}