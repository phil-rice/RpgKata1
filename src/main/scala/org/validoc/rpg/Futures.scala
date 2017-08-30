package org.validoc.rpg

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
trait Futures {

  implicit class FunctionPimper[Req, Res](fn: Req => Res) {
    def toFutureFn: Req => Future[Res] = { req: Req => Future.successful(fn(req)) }
  }
  implicit class FuturePimper[T](t: Future[T]){
    def await = Await.result(t, 5 seconds)
  }

}

object Futures extends Futures
