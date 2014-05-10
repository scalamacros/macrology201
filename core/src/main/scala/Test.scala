class C { override def toString = "C" }

object Test {
  def main(args: Array[String]): Unit = {
    def foo(x: Optional[C]) = x
    println(foo(new Optional(null)).getOrElse(new C))
  }
}