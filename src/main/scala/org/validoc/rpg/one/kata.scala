package org.validoc.rpg.one
package com.rpg.kata

trait DamageToCharacter[T, P] extends ((T, P) => P)

trait HealToCharacter[T, P] extends ((T, P) => P)

trait Character

case class ChangeHealth(name: String, changeHealth: Int) extends Character

case class PhysicalCharacter(name: String, health: Int = 1000, level: Int = 1, alive: Boolean = true) extends Character {

  def processDamageToCharacters(changeHealth: ChangeHealth, character: PhysicalCharacter)(implicit damageCharacters: DamageToCharacter[ChangeHealth, PhysicalCharacter]): PhysicalCharacter = {
    damageCharacters(changeHealth, character)
  }

  def processHealToCharacter(changeHealth: ChangeHealth, character: PhysicalCharacter)(implicit healCharacter: HealToCharacter[ChangeHealth, PhysicalCharacter]): PhysicalCharacter = {
    healCharacter(changeHealth, character)
  }
}

object Character {

  implicit object damageToCharacters extends DamageToCharacter[ChangeHealth, PhysicalCharacter] {
    override def apply(cChar: ChangeHealth, pChar: PhysicalCharacter): PhysicalCharacter = {

      pChar.name match {
        case cChar.name =>
          pChar.health match {
            case x if (x == 0 || (x > 0 && cChar.changeHealth > pChar.health)) =>
              PhysicalCharacter(cChar.name, 0, pChar.level, false)
            case _ =>
              PhysicalCharacter(cChar.name, pChar.health - cChar.changeHealth, pChar.level, true)
          }
        case _ => pChar
      }

    }
  }

  implicit object healToCharacter extends HealToCharacter[ChangeHealth, PhysicalCharacter] {
    override def apply(cChar: ChangeHealth, pChar: PhysicalCharacter): PhysicalCharacter = {

      pChar.name match {
        case cChar.name =>
          pChar.alive match {
            case true => {
              if (pChar.health + cChar.changeHealth >= 1000)
                PhysicalCharacter(cChar.name, 1000, pChar.level, true)
              else
                PhysicalCharacter(cChar.name, pChar.health + cChar.changeHealth, pChar.level, true)
            }
            case false => pChar
          }
        case _ => pChar
      }
    }
  }

}


object CharacterBehaviour {

  def damageToCharacters(physicalCharacter: PhysicalCharacter, health: ChangeHealth): PhysicalCharacter = {
    val char = physicalCharacter processDamageToCharacters(health, physicalCharacter)
    printStatus(char)
    char
  }

  def healToCharacter(physicalCharacter: PhysicalCharacter, health: ChangeHealth): PhysicalCharacter = {
    val char = physicalCharacter processHealToCharacter(health, physicalCharacter)
    printStatus(char)
    char
  }

  def printStatus(character: PhysicalCharacter): Unit = {
    println(s"Character Name : ${character.name} -> Health : ${character.health}, Level : ${character.level}, Alive : ${character.alive}")
  }
}


object CharacterInAction {
//  type Service[Req, Res] = Req => Res

  def main(args: Array[String]): Unit = {

    import CharacterBehaviour._

    val char = PhysicalCharacter("PERSON")
    printStatus(char)


    val goodHealth = ChangeHealth("PERSON", 100)
    val badHealth = ChangeHealth("PERSON", 200)

    val change1 = damageToCharacters(char, badHealth)
    val change2 = damageToCharacters(change1, badHealth)
    val change3 = damageToCharacters(change2, badHealth)
    val change4 = healToCharacter(change3, goodHealth)
    val change5 = healToCharacter(change4, goodHealth)

    printStatus(change5)


    val veryBadHealth = ChangeHealth("PERSON", 800)
    val change6 = damageToCharacters(change5, veryBadHealth)

    val recoverHealth = ChangeHealth("PERSON", 1000)
    healToCharacter(change6, recoverHealth)
  }

}