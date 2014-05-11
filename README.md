### Step-by-step walkthrough of part 1

[/commits/part1](../../commits/part1)

### Summary of part 1

Inspired by the work of Adam Rosenberger in [nalloc](https://github.com/arosenberger/nalloc), we've sketched a macro-powered zero-overhead option-like class. Thanks to macros, both Adam and us have been able to provide high-order combinators `getOrElse` and `map` that get compiled down to first-order code.

```
final class Optional[+A >: Null](val value: A) extends AnyVal {
  def get: A = value
  def isEmpty = value == null
  def getOrElse[B >: A](alt: => B): B = macro OptionalMacros.getOrElse
  def map[B](f: A => B): Optional[B] = macro OptionalMacros.map
  override def toString = if (isEmpty) "<empty>" else s"$value"
}
```

### Basic macrology

  * [https://github.com/scalamacros/sbt-example](https://github.com/scalamacros/sbt-example) provides a nice template to get a macro project started.
  * Use macro bundles and quasiquotes - they significantly simplify writing macros.
  * Know your debugging tools: `showCode`, `showRaw`, `-Xprint:typer` and other compiler flags. In the meanwhile we'll be working on IDE integration and other improvements to developer experience.
  * Careful with control flow (preserve evaluation order, be sure to cache values of macro arguments if they are used multiple times in expansions)
  * Mind hygiene (use `c.freshName` when generating local variables, when possible use `_root_`-qualified names to refer to external dependencies). This is something that we hope to do automatically in the future, but for now hygiene needs your attention.
  * Most of what we've seen here applies to Scala 2.10, except for macro bundles, relaxed signatures for macro impls and `showCode`, which require Scala 2.11. Quasiquotes aren't shipping with official Scala 2.10, but they are provided by [the macro paradise plugin](http://docs.scala-lang.org/overviews/macros/paradise.html).

### Advanced macrology

  * Make sure that your macro isn't susceptible to owner chain corruptions. A good way to test that is to pass owner-chain sensitive tree shapes in its prefix and arguments (see provided tests for a simple yet characteristic tree shape).
  * If your macro crashes in GenICode or LambdaLift, it's most likely owner chain corruption, and you need to do `c.internal.changeOwner`.
  * If your macro crashes in RefChecks, then it's probably an unsupported kind of mix of typed and untyped trees, and you need to go for `c.internal.typingTransform`.
  * All this applies equally to both 2.10 and 2.11. However, `c.internal` only exists in 2.11, on 2.10 you'll have to cast your way to `scala.tools.nsc.Global` - both techniques are demonstrated in the accompanying code.
  * We're working on making all these advanced recipes unnecessary, but there's still a long way to go.

### Let's keep on moving!

Materialization, reflection and blackbox/whitebox macros: [/tree/part2](../../tree/part2).
