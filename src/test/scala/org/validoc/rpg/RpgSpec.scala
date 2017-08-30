package org.validoc.rpg

import org.scalamock.scalatest.MockFactory
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers, OneInstancePerTest}

import scala.concurrent.{Await, Future}
import Futures._
trait RpgSpec extends FlatSpec with Matchers with MockFactory  with OneInstancePerTest{

  implicit class RpgSpecAnyPimper(t: Any){
    def waitIfNeeded = t match {
      case f: Future[_] => f.await
      case _ => t
    }
  }

}
