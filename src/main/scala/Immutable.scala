package immutable

import language.experimental.macros
import scala.reflect.macros.whitebox.Context

trait Immutable[T]
object Immutable {
  implicit def materialize[T]: Immutable[T] = macro impl[T]
  def impl[T: c.WeakTypeTag](c: Context) = {
    import c.universe._
    q"new Immutable[${weakTypeOf[T]}] { }"
  }
}
