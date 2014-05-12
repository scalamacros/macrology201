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
}
