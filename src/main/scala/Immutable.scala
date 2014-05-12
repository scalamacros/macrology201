package immutable

import language.experimental.macros
import scala.reflect.macros.whitebox.Context

trait Immutable[T]
object Immutable {
  def is[T]: Boolean = macro isImpl[T]
  def isImpl[T: c.WeakTypeTag](c: Context) = {
    import c.universe._
    val immutableOfT = appliedType(typeOf[Immutable[_]], weakTypeOf[T])
    val inferred = c.inferImplicitValue(immutableOfT, silent = true)
    q"${inferred.nonEmpty}"
  }

  implicit def materialize[T]: Immutable[T] = macro materializeImpl[T]
  def materializeImpl[T: c.WeakTypeTag](c: Context) = {
    import c.universe._
    q"new Immutable[${weakTypeOf[T]}] { }"
  }
}
