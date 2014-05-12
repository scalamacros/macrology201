import immutable.Immutable
import org.scalatest.FunSuite

class ImmutableSuite extends FunSuite {
  test("dummy class is immutable") {
    class Dummy
    assert(Immutable.is[Dummy])
  }

  test("class with var is not immutable") {
    class C(var x: Int)
    assert(!Immutable.is[C])
  }

  test("class with field that is of mutable is mutable") {
    class M(var x: Int)
    class C(val x: M)
    assert(!Immutable.is[C])
  }

  test("self recursive class is immutable") {
    class Recursive(recursive: Recursive)
    assert(Immutable.is[Recursive])
  }

  test("mutually recursive classes are immutable") {
    case class Mutually(rec: Rec)
    case class Rec(mutually: Mutually)
    assert(Immutable.is[Mutually])
    assert(Immutable.is[Rec])
  }
}
