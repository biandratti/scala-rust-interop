
#### Compile Rust code into a shared library (dynamic library)
```
cd rust-module
rustc --crate-type=dylib src/lib.rs -o target/librust_code.so #Linux
rustc --crate-type=dylib src/lib.rs -o target/librust_code.dylib #Mac
```

#### On Linux, use the LD_LIBRARY_PATH environment variable to specify additional directories to search for shared libraries at runtime.
```
export LD_LIBRARY_PATH=$FULL_PATH/rust-module/target/:$LD_LIBRARY_PATH
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