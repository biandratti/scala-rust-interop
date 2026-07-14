# IntelliJ IDEA: Run and Debug (Scala Native + Rust)

This project is **Scala Native** calling a **Rust** library. The app runs as a **native binary**, not on the JVM. Using the default "Scala App" / "Application" run config in IDEA will fail because it tries to run the main class on the JVM.

Use one of the approaches below so Run/Debug works from IDEA.

---

## Option 1: SBT Task run configuration (recommended)

This makes the **Run** button behave like `sbt scalaModule/run`: SBT compiles the Rust dependency (if needed), builds the Scala Native binary, and runs it.

### 1. Build the Rust library once (if not already done)

In a terminal at the project root:

```bash
cd rust-module && cargo build --release && cd ..
```

### 2. Create an SBT Task run config in IDEA

1. **Run** → **Edit Configurations...**
2. Click **+** (Add New Configuration) → **sbt Task**.
3. Set:
   - **Name:** `Scala Native Run`
   - **Tasks:** `scalaModule/run`
   - **Working directory:** leave default (project root) or set to `$PROJECT_DIR$`
4. **Environment variables** (important so the native binary finds the Rust library):
   - Click the **Environment variables** field (or the folder icon).
   - Add:
     - **Linux/macOS:**  
       `LD_LIBRARY_PATH` = `$PROJECT_DIR$/rust-module/target/release`  
       (or full path to `rust-module/target/release`)
     - **Windows:**  
       Add the folder that contains `rust_code.dll` to **PATH**, e.g.  
       `PATH` = `$PROJECT_DIR$\rust-module\target\release;%PATH%`
5. **Before launch** (optional but useful):
   - Add **Run Another Configuration** and choose a config that builds the Rust library (e.g. an "External tool" that runs `cargo build --release` in `rust-module`),  
   **or**
   - Remember to run `cargo build --release` in `rust-module` before running.
6. Check **Store as project file** so the config is shared (saved under `.idea/runConfigurations/`).
7. **Apply** → **OK**.

### 3. Run / Debug

- Select **Scala Native Run** in the run configuration dropdown.
- Click **Run** (green play) or **Debug**.

IDEA will run the SBT task `scalaModule/run`; SBT will build the native binary and execute it. The process you see in the Run window is the **native executable**, not the JVM.

**Debug note:** With an SBT Task, the debugger is attached to the SBT JVM process. To debug the **native** Scala Native binary (e.g. with breakpoints in Scala code compiled to native), use Option 2 and attach a native debugger (GDB/LLDB).

---

## Option 2: Run the native binary directly (for native debugging)

Use this when you want to attach **GDB** or **LLDB** to the Scala Native binary (e.g. to debug with breakpoints in the compiled native code).

### 1. Build everything

```bash
# Rust library
cd rust-module && cargo build --release && cd ..

# Scala Native binary
sbt nativeLink
```

### 2. Find the binary

The binary is under:

`scala-module/target/scala-<version>/scalamodule`  
(or `scala-module-out` depending on Scala Native version)

Example (replace Scala version if different):

```bash
./scala-module/target/scala-3.8.2/scalamodule
```

### 3. Set library path and run (Linux/macOS)

```bash
export LD_LIBRARY_PATH=$PWD/rust-module/target/release:$LD_LIBRARY_PATH
./scala-module/target/scala-3.8.2/scalamodule
```

On Windows, ensure the folder containing `rust_code.dll` is on **PATH**, then run the `.exe`.

### 4. Run configuration in IDEA (native executable)

1. **Run** → **Edit Configurations...** → **+** → **Application**.
2. Set:
   - **Name:** `Scala Native (native binary)`
   - **Program arguments:** *(leave empty)*
   - **Working directory:** `$PROJECT_DIR$`
   - **Environment variables:**
     - Linux/macOS: `LD_LIBRARY_PATH=$PROJECT_DIR$/rust-module/target/release`
     - Windows: add `rust-module\target\release` to `PATH`
3. **Build and run** → **Before launch:**
   - Add **Run External tool** (e.g. run `cargo build --release` in `rust-module`).
   - Add another step to run `sbt nativeLink` (or an external tool that runs it).
4. **Program:** full path to the native binary, e.g.  
   `$PROJECT_DIR$/scala-module/target/scala-3.8.2/scalamodule`  
   (adjust Scala version to match your `sbt scalaVersion` output.)

Then you can use **Run** to start the binary, and **Attach to Process** (or a GDB/LLDB run config if you have the right plugin) to debug the native process.

---

## Why the default Run/Debug fails

- IDEA’s default **Scala Application** (or **Application** with a Scala main class) runs the main class **on the JVM** (via `java` + SBT launcher or JVM classpath).
- This project has no JVM main: the entry point is a **Scala Native** `@main` that is compiled to a **native executable** and must be run as that binary, with `LD_LIBRARY_PATH` (or `PATH` on Windows) set so it can load the Rust library.

So you must either:

- Run via **SBT Task** `scalaModule/run` (Option 1), or  
- Run the **native binary** directly with the right env (Option 2).

---

## References

- [Scala Native IDE setup](https://scala-native.org/en/latest/contrib/ides.html) (import and module setup; Run/Debug is not covered there in detail).
- [IntelliJ Run/Debug: sbt Task](https://www.jetbrains.com/help/idea/run-debug-configuration-sbt-task.html).
- [GitHub issue #79](https://github.com/biandratti/scala-rust-interop/issues/79): discussion about IDEA Run/Debug for this project.
