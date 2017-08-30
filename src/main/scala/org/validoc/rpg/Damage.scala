package org.validoc.rpg

import scala.concurrent.Future

trait ReceiveDamage[T] extends (HitPoints => T => T)

trait KillIfNeeded[T] extends (T => T)

import Futures._

class DoDamage[Done](implicit takeDamage: ReceiveDamage[Done], killIfNeeded: KillIfNeeded[Done]) extends (HitPoints => Done => Future[Done]) {
  def apply(hitPoints: HitPoints) = (takeDamage(hitPoints) andThen killIfNeeded).toFutureFn
}

object DoDamage {
  implicit def doDamage[Done](implicit takeDamage: ReceiveDamage[Done], killIfNeeded: KillIfNeeded[Done]) = new DoDamage[Done]
}

