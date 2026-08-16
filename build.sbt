// import to add Scala Native options
import scala.scalanative.build._

inThisBuild(
  List(
    scalaVersion := "3.8.4",
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision
  )
)

lazy val commonSettings = Seq(
  organization := "com.native",
  logLevel := Level.Info,
  scalacOptions ++= List(
    "-Wunused:imports"
  )
)

lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(name := "scala-rust-interop")
  .aggregate(scalaModule)

lazy val scalaModule = project
  .in(file("scala-module"))
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings)
  .settings(name := "scala-module")
  .dependsOn(rustModule)
  .aggregate(rustModule)
  .settings(
    nativeConfig := {
      nativeConfig.value
        .withLTO(LTO.none) // thin
        .withMode(Mode.debug) // releaseFast
        .withGC(GC.immix) // commix
        .withLinkingOptions(
          Seq(
            s"-L${baseDirectory.value.getParentFile}/rust-module/target/release/",
            "-lrust_code"
          )
        )
    },
    libraryDependencies += "org.scalameta" %% "munit" % "1.3.5" % Test
  )

lazy val rustModule = project
  .in(file("rust-module"))
  .settings(commonSettings)
  .settings(name := "rust-module")

addCommandAlias("checkFormat", ";scalafmtSbtCheck ;scalafmtCheckAll")
addCommandAlias("scalafixLint", ";compile ;scalafix")
