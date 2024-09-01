// import to add Scala Native options
import scala.scalanative.build._

name := "scala rust interoperability"

// defaults set with common options shown
nativeConfig ~= { c =>
  c.withLTO(LTO.none) // thin
    .withMode(Mode.debug) // releaseFast
    .withGC(GC.immix) // commix
}

lazy val commonSettings = Seq(
  organization := "com.native",
  scalaVersion := "3.5.0",
  logLevel := Level.Info /*,
  scalacOptions ++= List(
    "-Wunused"
  )*/
)

lazy val root = (project in file("."))
  .settings(commonSettings)
  .aggregate(scalaModule)

lazy val scalaModule = project
  .in(file("scala-module"))
  .enablePlugins(ScalaNativePlugin)
  .settings(commonSettings)
  .dependsOn(rustModule)
  .aggregate(rustModule)
  .settings(
    nativeConfig := {
      nativeConfig.value
        .withLinkingOptions(
          Seq(
            s"-L${baseDirectory.value.getParentFile}/rust-module/target/release/",
            "-lrust_code"
          )
        )
    },
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "1.0.1" % Test,
      "org.scalameta" %% "munit-scalacheck" % "1.0.0" % Test
    )
  )

lazy val rustModule = project
  .in(file("rust-module"))
  .settings(commonSettings)

addCommandAlias("checkFormat", ";scalafmtSbtCheck ;scalafmtCheckAll")
addCommandAlias("scalafixLint", ";compile ;scalafix")
