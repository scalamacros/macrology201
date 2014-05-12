import scala.language.experimental.macros
import Compat210._

// Allocation-free option type for Scala
// Inspired by https://github.com/arosenberger/nalloc

final class Optional[+A >: Null](val value: A) extends AnyVal {
  def get: A = value
  def isEmpty = value == null
  def getOrElse[B >: A](alt: => B): B = macro OptionalMacros.getOrElse[A, B]
  def map[B >: Null](f: A => B): Optional[B] = macro OptionalMacros.map[A, B]
  override def toString = if (isEmpty) "<empty>" else s"$value"
}

object OptionalMacros {
  import scala.reflect.macros._
  import blackbox.Context

  def getOrElse[A >: Null, B >: A](c: Context)(alt: c.Expr[B]): c.Expr[B] = {
    val helper = new OptionalMacros[c.type](c)
    c.Expr[B](helper.getOrElse(alt.tree))
  }

  def map[A >: Null, B >: Null](c: Context)(f: c.Expr[A => B]): c.Expr[Optional[B]] = {
    val helper = new OptionalMacros[c.type](c)
    c.Expr[Optional[B]](helper.map(f.tree))
  }

  class OptionalMacros[C <: Context](val c: C) extends Internal210 {
    import c.universe._
    val q"$prefix.${_}[..${_}](..$args)" = c.macroApplication
    val temp = c.fresh(newTermName("temp"))

    def getOrElse(alt: c.Tree): c.Tree = {
      q"""
        val $temp = ${splicer(prefix)}
        if ($temp.isEmpty) $alt else $temp.value
      """
    }

    def map(f: c.Tree): c.Tree = {
      import internal._, decorators._
      val tempSym = c.internal.enclosingOwner.newTermSymbol(temp).setInfoCompat(prefix.tpe)
      val tempDef = c.internal.valDef(tempSym, c.internal.changeOwner(prefix, c.internal.enclosingOwner, tempSym))

      val q"($inlinee => $body)" = f
      c.internal.changeOwner(body, f.symbol, c.internal.enclosingOwner)
      val mapped = c.internal.typingTransform(body)((tree, api) => tree match {
        case Ident(_) if tree.symbol == inlinee.symbol =>
          api.typecheck(q"$tempSym.value")
        case _ =>
          api.default(tree)
      })

      q"""
        $tempDef
        if ($tempSym.isEmpty) new Optional(null) else new Optional($mapped)
      """
    }

    // inspired by https://gist.github.com/retronym/10640845#file-macro2-scala
    // check out the gist for a detailed explanation of the technique
    private def splicer(tree: c.Tree): c.Tree = {
      import internal._, decorators._
      tree.updateAttachment(macroutil.OrigOwnerAttachment(c.internal.enclosingOwner))
      q"_root_.macroutil.Splicer.changeOwner($tree)"
    }
  }
}

package macroutil {
  case class OrigOwnerAttachment(sym: Any)
  object Splicer {
    import scala.reflect.macros._
    import blackbox.Context
    def impl[A](c: Context)(expr: c.Expr[A]): c.Expr[A] = {
      val helper = new Splicer[c.type](c)
      c.Expr[A](helper.changeOwner(expr.tree))
    }
    class Splicer[C <: Context](val c: C) extends Internal210 {
      def changeOwner(tree: c.Tree): c.Tree = {
        import c.universe._, internal._, decorators._
        val origOwner = tree.attachments.get[OrigOwnerAttachment].map(_.sym).get.asInstanceOf[Symbol]
        c.internal.changeOwner(tree, origOwner, c.internal.enclosingOwner)
      }
    }
    def changeOwner[A](expr: A): A = macro impl[A]
  }
}
