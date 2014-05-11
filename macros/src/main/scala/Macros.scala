import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

// Allocation-free option type for Scala
// Inspired by https://github.com/arosenberger/nalloc

final class Optional[+A >: Null](val value: A) extends AnyVal {
  def get: A = value
  def isEmpty = value == null
  def getOrElse[B >: A](alt: => B): B = macro OptionalMacros.getOrElse
  def map[B >: Null](f: A => B): Optional[B] = macro OptionalMacros.map
  override def toString = if (isEmpty) "<empty>" else s"$value"
}

class OptionalMacros(val c: Context) {
  import c.universe._
  val q"$prefix.$_[..$_](..$args)" = c.macroApplication
  val temp = c.freshName(TermName("temp"))

  def getOrElse(alt: c.Tree): c.Tree = {
    q"""
      val $temp = ${splicer(prefix)}
      if ($temp.isEmpty) $alt else $temp.value
    """
  }

  def map(f: c.Tree): c.Tree = {
    val q"($inlinee => $body)" = f
    object inliner extends Transformer {
      override def transform(tree: Tree): Tree = tree match {
        case Ident(_) if tree.symbol == inlinee.symbol =>
          q"$temp"
        case _ =>
          super.transform(tree)
      }
    }
    val mapped = inliner.transform(body)
    q"""
      val $temp = ${splicer(prefix)}
      if ($temp.isEmpty) new Optional(null) else new Optional($mapped)
    """
  }

  // inspired by https://gist.github.com/retronym/10640845#file-macro2-scala
  // check out the gist for a detailed explanation of the technique
  private def splicer(tree: c.Tree): c.Tree = {
    import c.internal._, decorators._
    tree.updateAttachment(macroutil.OrigOwnerAttachment(enclosingOwner))
    q"_root_.macroutil.Splicer.changeOwner($tree)"
  }
}

package macroutil {
  case class OrigOwnerAttachment(sym: Any)
  object Splicer {
    def impl(c: Context)(tree: c.Tree): c.Tree = {
      import c.universe._, c.internal._, decorators._
      val origOwner = tree.attachments.get[OrigOwnerAttachment].map(_.sym).get.asInstanceOf[Symbol]
      c.internal.changeOwner(tree, origOwner, c.internal.enclosingOwner)
    }
    def changeOwner[A](tree: A): A = macro impl
  }
}
