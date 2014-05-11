class C { override def toString = "C" }

object Test {
  def main(args: Array[String]): Unit = {
    val x1 = new Optional(new C)
    val x2 = x1.map(_.toString)
    println(x2)
  }
}