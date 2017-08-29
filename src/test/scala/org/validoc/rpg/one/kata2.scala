package org.validoc.rpg.one
package com.business

/**
  * Created by prasenjit.b on 8/27/2017.
  */

trait Damage[M,P] extends ((M,P) => P)
trait Heal[M,P] extends ((M,P) => P)


case class DealInteraction(amountOfInteraction : Int )
case class Person( name: String, health : Int = 1000, level : Int = 1, alive : Boolean = true ){

  def callDamageOn( person: Person, measureInteraction: DealInteraction)(implicit damageAction : Damage[DealInteraction,Person] ): Person ={
    damageAction( measureInteraction, person )
  }

  def callHealOn( person: Person, measureInteraction: DealInteraction)(implicit healAction : Heal[DealInteraction,Person] ): Person ={
    healAction( measureInteraction, person )
  }
}


object Damage{
  implicit object DamageCharacter extends Damage[DealInteraction,Person] {
    override def apply(measureInteraction: DealInteraction, person: Person): Person = {
      isPersonAlive( substractHealth( person, measureInteraction) )
    }
  }

  def substractHealth( person: Person, measureInteraction: DealInteraction ): Person={
    if( measureInteraction.amountOfInteraction > person.health ){
      println(s"Charcter ${person.name} received damage more than or equals to its Health so it's Health becomes Zero")
      Person( person.name, 0, person.level, true )
    }else{
      Person( person.name, person.health - measureInteraction.amountOfInteraction, person.level, true )
    }
  }

  def isPersonAlive( person: Person ): Person={
    if( person.health == 0 ){
      println(s"As Charcter ${person.name} Health becomes Zero so it is Dead")
      Person( person.name, person.health, person.level, false )
    }else{
      Person( person.name, person.health, person.level, true )
    }
  }

}

object Heal{
  implicit object HealCharacter extends Heal[DealInteraction,Person] {
    override def apply(measureInteraction: DealInteraction, person: Person): Person = {
      addHealthIfPersonIsAlive( person, measureInteraction )
    }
  }

  def addHealthIfPersonIsAlive( person: Person, measureInteraction: DealInteraction ):Person={
    if( person.alive ){
      healingHealthCanNotBe1000( person, measureInteraction )
    }else{
      println(s" Dead Charcter ${person.name} can not be healed")
      person
    }
  }

  def healingHealthCanNotBe1000( person: Person, measureInteraction: DealInteraction ): Person ={
    if( person.health + measureInteraction.amountOfInteraction >= 1000 ){
      println(s"Charcter ${person.name} received Healing more than or equals to 1000 and it can not be 1000")
      person
    }else{
      Person( person.name, person.health + measureInteraction.amountOfInteraction )
    }
  }
}

object PersonInteraction{
  def callDamage( actorPerson: Person, affectedPerson : Person, measureInteraction: DealInteraction): Person ={
    actorPerson callDamageOn( affectedPerson , measureInteraction )
  }

  def callHeal( actorPerson: Person, affectedPerson : Person, measureInteraction: DealInteraction): Person ={
    actorPerson callHealOn( affectedPerson, measureInteraction )
  }

  def logging( actorPerson: Person, action: String, affectedPerson : Person, measureInteraction: DealInteraction, f : (Person, Person, DealInteraction) => Person  ): Person ={
    println(s"Character ${actorPerson.name} execute ${action} on ${affectedPerson.name} with amount ${measureInteraction.amountOfInteraction}")
    println(s" affected Character health value before opeartion ${affectedPerson}")
    val person = f( actorPerson, affectedPerson, measureInteraction)
    println(s" affected Character health value after opeartion ${person}")
    person
  }
}

object mainClass{
  def main(args: Array[String]): Unit = {
    import PersonInteraction._
    val p1 = Person("p1")
    var p2 = Person("p2")
    //p2 = logging( p1,"Damage", p2, DealInteraction(200), callDamage )
    logging( p1,"Heal", logging( p1,"Damage", p2, DealInteraction(1000), callDamage ), DealInteraction(200), callHeal )

  }
}