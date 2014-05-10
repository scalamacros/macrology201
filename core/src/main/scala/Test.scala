class C { override def toString = "C" }

object Test {
  def main(args: Array[String]): Unit = {
    println(new Optional(null).getOrElse(null))
  }
}