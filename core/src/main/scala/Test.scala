class C { override def toString = "C" }

object Test {
  def main(args: Array[String]): Unit = {
    val temp = 100
    println(new Optional(if (temp < 100) new C else null).getOrElse(new C))
  }
}