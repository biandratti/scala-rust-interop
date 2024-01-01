package com.biandratti

import scala.scalanative.unsafe._

@extern
object RustInterop {
  def multiply_by_two(num: Int): Int = extern

  def get_ip(): CString = extern
}
