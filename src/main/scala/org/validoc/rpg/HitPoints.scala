package org.validoc.rpg


case class HitPoints(value: Int) extends AnyVal {
  def +(hitPoints: HitPoints) = HitPoints(value + hitPoints.value)

  def -(hitPoints: HitPoints) = HitPoints(value - hitPoints.value)

  def lessThanZero: Boolean = value < 0

  def moreThan(hitPoints: HitPoints) = value > hitPoints.value
}
