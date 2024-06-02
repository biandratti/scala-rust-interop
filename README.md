# Interoperability Between Scala and Rust
This repository demonstrates how to achieve interoperability between Scala and Rust by integrating Rust code as a shared library within a Scala Native project.

### Setup
###### Rust Module
1. Navigate to the rust-module directory.
2. Build the Rust code into a shared library (dynamic library) using Cargo:
```
cd rust-module
cargo build --release
```
###### In the root
1. Run the following command to specify additional directories to search for shared libraries at runtime (required on Linux):
```
export LD_LIBRARY_PATH=${PWD}/rust-module/target/release/:$LD_LIBRARY_PATH
```

### Running the Application
###### Run app:
```
sbt scalaModule/run
```
###### Run binary:
```
sbt nativeLink
./scala-module/target/scala-3.4.2/scalamodule
```
###### Run test:
```
sbt test
```
