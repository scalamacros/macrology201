import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

object Macros {
  def impl(c: Context): c.Tree = {
    import c.universe._
    q"""println("Hello World")"""
  }

  def hello: Unit = macro impl
}