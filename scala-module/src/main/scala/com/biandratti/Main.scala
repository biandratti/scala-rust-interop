package com.biandratti

object Main {

  def main(args: Array[String]): Unit = {
    val result = RustInterop.multiply_by_two(5)
    println(s"Result from Rust: $result")
  }  
}
