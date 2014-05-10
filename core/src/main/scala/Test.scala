class C { override def toString = "C" }

object Test {
  def main(args: Array[String]): Unit = {
    val x = new Optional(new C)
    val x1 = x.getOrElse(new C)
    println(x, x1)

    val y = new Optional(null)
    val y1 = y.getOrElse({ println("hello"); new C })
    println(y, y1)
  }
}