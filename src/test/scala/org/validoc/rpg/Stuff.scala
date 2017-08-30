package org.validoc.rpg

import scala.concurrent.{ExecutionContext, Future}

trait Monoid[T] {
  // This is the design pattern of aggregation
  // We want a monoid instead of a foldLeft because we distribute the work if we need to go faster
  // rules (t1 add t2) add t3 is the same as t1 add (t2 add t3)
  // t1 add identity is the same as t2
  // identity add  t1 is not the same as t1
  //NOTE IT IS NOT IMPORTANT THAT t1 add t2 is the same as t2 add t1, in fact that is often not true

  def identity: T

  def add(t1: T, t2: T): T
}

trait Functor[M[_]] {
  // We use these all the time.
  // this is a structure preserving transformation.
  // when we use list comprehensions, we almost always have a 'map' somewhere in there
  //
  // Why do we care about these? We care about them because almost every time we iterate over something we are either folding, filtering or mapping. This is mapping

  def map[T, T1](m: M[T], fn: T => T1): M[T1]
}

trait Monad[M[_]] extends Functor[M] {
  //NOTHING TO DO WITH MONOID. It's a sad coincidence they sound similar
  // Three laws. All basically say 'implement flatMap properly'
  // (m1 flatmp m2) flatmap m3 is the same as m1 flatmap(m2 flatmap m3)
  //  lift(x) flatMap(fn1) is the same as fn1(x)
  //  m flatMap(lift) is the same as m

  // Why do we care about these
  // because we want to chain operations together. Things like microservice calls, and without this, we can't do it simply
  // we can also do over very useful things like parsing, controlling error handling ...

  def lift[T](t: T): M[T]

  def flatMap[T, T1](m: M[T], fn: T => M[T1]): M[T1]

  //from the basic operations we can do helpful things like this:
  def join[Seq[_] : Functor, T](m: M[Seq[M[T]]]): M[Seq[T]] = ???

}

object Monad {

  implicit object MonadForList extends Monad[List] {
    override def lift[T](t: T): List[T] = List(t)

    override def flatMap[T, T1](m: List[T], fn: (T) => List[T1]): List[T1] = m.flatMap(fn)

    override def map[T, T1](m: List[T], fn: (T) => T1): List[T1] = m.map(fn)
  }

  implicit def MonadForFuture(implicit ec: ExecutionContext) = new Monad[Future] {
    override def lift[T](t: T): Future[T] = Future(t)

    override def flatMap[T, T1](m: Future[T], fn: (T) => Future[T1]): Future[T1] = m.flatMap(fn)

    override def map[T, T1](m: Future[T], fn: (T) => T1): Future[T1] = m.map(fn)
  }
}

trait KleisliLanguage {
  type Kleisli[M[_], Req, Res] = Req => M[Res]

  implicit class KleisliPimper[M[_], Req, Res](k: Kleisli[M, Req, Res])(implicit monad: Monad[M]) {
    def andThen[T](kleisli: Kleisli[M, Res, T]): Kleisli[M, Req, T] = {
      req: Req => monad.flatMap(k(req), kleisli)
    }
  }


}