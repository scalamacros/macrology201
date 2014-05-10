class C { override def toString = "C" }

object Test {
  def main(args: Array[String]): Unit = {
    val Optional = "uh-oh"
    println(new Optional(null).getOrElse(null))
  }
}