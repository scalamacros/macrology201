import immutable.Immutable
import org.scalatest.FunSuite

class ImmutableSuite extends FunSuite {
  test("dummy class is immutable") {
    final class Dummy
    assert(Immutable.is[Dummy])
  }

  test("class with var is not immutable") {
    final class C(var x: Int)
    assert(!Immutable.is[C])
  }

  test("class with field that is of mutable is mutable") {
    final class M(var x: Int)
    final class C(val x: M)
    assert(!Immutable.is[C])
  }

  test("self recursive class is immutable") {
    final class Recursive(recursive: Recursive)
    assert(Immutable.is[Recursive])
  }

  test("mutually recursive classes are immutable") {
    final case class Mutually(rec: Rec)
    final case class Rec(mutually: Mutually)
    assert(Immutable.is[Mutually])
    assert(Immutable.is[Rec])
  }

  test("polymorphic class might or might not be immutable") {
    final class M(var x: Int)
    final class C[T](val x: T)
    assert(Immutable.is[C[Int]])
    assert(!Immutable.is[C[M]])
  }

  test("open class might not be immutable") {
    class C
    assert(!Immutable.is[C])
  }

  test("objects with immutable fields are immutable") {
    object O { val x: Int = 0 }
    assert(Immutable.is[O.type])
  }

  test("objects with mutable fields are mutable") {
    object O { var x: Int = 0 }
    assert(!Immutable.is[O.type])
  }

  test("class with nested object can be in immutable") {
    final class C { object O { val x: Int = 0 } }
    assert(Immutable.is[C])
  }
}
