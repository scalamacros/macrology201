import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

// Allocation-free option type for Scala
// Inspired by https://github.com/arosenberger/nalloc

final class Optional[+A >: Null](val value: A) extends AnyVal {
  def get: A = value
  def isEmpty = value == null
  // @inline final def getOrElse[B >: A](alt: => B): B = if (isEmpty) alt else value
  def getOrElse[B >: A](alt: => B): B = macro OptionalMacros.getOrElse
  override def toString = if (isEmpty) "<empty>" else s"$value"
}

object Optional {
  def ensuringNotNull[A](x: A): A = {
    if (x == null) sys.error("argument to Optional.getOrElse can't be null")
    x
  }
}

class OptionalMacros(val c: Context) {
  def getOrElse(alt: c.Tree): c.Tree = {
    import c.universe._
    val q"$prefix.$_[..$_](..$args)" = c.macroApplication
    val temp = c.freshName(TermName("temp"))
    val Optional = q"_root_.Optional" // doesn't work!
    q"""
      val $temp = $prefix
      if ($temp.isEmpty) $Optional.ensuringNotNull($alt) else $temp.value
    """
  }
}