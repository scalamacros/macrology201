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
    val T = weakTypeOf[T]
    val deps =
      T.members.collect { case s: TermSymbol if !s.isMethod =>
        if (s.isVar) c.abort(c.enclosingPosition, s"$T is not immutable because it has mutable field ${s.name}")
        s.typeSignatureIn(T)
      }
    val implicitlies = deps.map { tpe => q"implicitly[Immutable[$tpe]]" }
    val name = TermName(c.freshName())
    q"""
      implicit object $name extends Immutable[$T]
      ..$implicitlies
      $name
    """
  }
}
