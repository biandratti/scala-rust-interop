package com.biandratti

import scala.scalanative.unsafe.fromCString

object Main {

  def main(args: Array[String]): Unit = {
    println(s"multiply_by_two: ${RustInterop.multiply_by_two(5)}")
    println(s"get_ip: ${fromCString(RustInterop.get_ip())}")
  }
}
