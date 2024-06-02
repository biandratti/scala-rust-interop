package com.native

import scala.scalanative.unsafe.fromCString

@main def run(): Unit = {
  println(s"multiply_by_two: ${RustInterop.multiply_by_two(5)}")
  println(s"get_ip: ${fromCString(RustInterop.get_ip())}")
}
