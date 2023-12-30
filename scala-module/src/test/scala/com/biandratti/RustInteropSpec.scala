package com.biandratti

import munit.FunSuite

class RustInteropSpec extends FunSuite {

  test("multiply_by_two called") {
    assertEquals(RustInterop.multiply_by_two(5), 10)
  }
}
