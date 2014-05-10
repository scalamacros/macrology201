import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

// Allocation-free option type for Scala
// Inspired by https://github.com/arosenberger/nalloc

final class Optional[+A >: Null](val value: A) extends AnyVal {
  def get: A = value
  def isEmpty = value == null
  def getOrElse[B >: A](alt: => B): B = macro OptionalMacros.getOrElse
  override def toString = if (isEmpty) "<empty>" else s"$value"
}

class OptionalMacros(val c: Context) {
  def getOrElse(alt: c.Tree): c.Tree = {
    import c.universe._
    val q"$prefix.$_[..$_](..$args)" = c.macroApplication
    val temp = c.freshName(TermName("temp"))
    q"""
      val $temp = $prefix
      if ($temp.isEmpty) $alt else $temp.value
    """
  }
}