
#### Compile Rust code into a shared library (dynamic library)
```
cd rust-module
cargo build --release
```

#### On Linux, use the LD_LIBRARY_PATH environment variable to specify additional directories to search for shared libraries at runtime.
```
export LD_LIBRARY_PATH=${PWD}/rust-module/target/release/:$LD_LIBRARY_PATH
```

#### Run app:
```
sbt scalaModule/run
```

#### Run binary:
```
sbt compile
./scala-module/target/scala-2.12/scalamodule-out
```