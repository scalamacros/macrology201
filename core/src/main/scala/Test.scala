class C { override def toString = "C" }

object Test {
  def main(args: Array[String]): Unit = {
    val x1 = new Optional(null).getOrElse(new C)
    val x2 = new Optional(new C).map(x => x1)
  }
}