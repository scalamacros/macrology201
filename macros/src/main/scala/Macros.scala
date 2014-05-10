import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

// Allocation-free option type for Scala
// Inspired by https://github.com/arosenberger/nalloc

final class Optional[+A >: Null](val value: A) extends AnyVal {
  def get: A = value
  def isEmpty = value == null
  // can't seem to coerce the inliner into inlining calls to getOrElse
  // would be great if you know a way of achieving that!
  // upd. Thank you, Jason! https://issues.scala-lang.org/browse/SI-8580
  @inline final def getOrElse[B >: A](alt: => B): B = if (isEmpty) alt else value
  override def toString = if (isEmpty) "<empty>" else s"$value"
}