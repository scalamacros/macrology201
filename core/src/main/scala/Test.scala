class C { override def toString = "C" }

object Test {
  def main(args: Array[String]): Unit = {
    def x = { println("side-effect!"); new Optional(new C) }
    println(x.getOrElse(new C))
  }
}