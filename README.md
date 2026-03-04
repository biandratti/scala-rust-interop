# Scala-Rust Interop

Demonstrates interoperability between **Scala Native** and **Rust** by calling Rust functions from Scala through FFI (Foreign Function Interface).

## Prerequisites

- [Rust](https://rustup.rs/) (Cargo)
- [Scala](https://www.scala-lang.org/) with sbt
- [Scala Native](https://scala-native.org/) dependencies

## Quick Start

**1. Build the Rust library:**
```bash
cd rust-module
cargo build --release
cd ..
```

**2. Set library path (Linux/macOS):**
```bash
export LD_LIBRARY_PATH=${PWD}/rust-module/target/release/:$LD_LIBRARY_PATH
```

**3. Run the application:**
```bash
sbt scalaModule/run
```

## Additional Commands

**Run tests:**
```bash
sbt test
```

**Build native binary:**
```bash
sbt nativeLink
SCALA_VERSION=$(sbt "scalaVersion" | tail -1 | awk '{print $NF}')
./scala-module/target/scala-$SCALA_VERSION/scalamodule
```

## IntelliJ IDEA Run/Debug

To run or debug from IntelliJ IDEA (instead of the terminal), use an **SBT Task** run configuration for `scalaModule/run` and set the library path so the native binary finds the Rust library. Step-by-step instructions and a shared run configuration are in **[docs/IDEA-RUN-DEBUG.md](docs/IDEA-RUN-DEBUG.md)**.

## Project Structure

- `rust-module/` - Rust library exposing C-compatible functions
- `scala-module/` - Scala Native application calling Rust code
