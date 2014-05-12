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
}
