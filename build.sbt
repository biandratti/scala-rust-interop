scalaVersion := "3.3.1"

logLevel := Level.Info

// import to add Scala Native options
import scala.scalanative.build._

// defaults set with common options shown
nativeConfig ~= { c =>
  c.withLTO(LTO.none) // thin
    .withMode(Mode.debug) // releaseFast
    .withGC(GC.immix) // commix
}

/*lazy val commonSettings = Seq(
  organization := "com.biandratti",
  scalaVersion := "3.3.1",
  logLevel := Level.Info
  scalacOptions ++= List(
    "-Wunused"
  )
)*/

lazy val root = (project in file("."))
  .aggregate(scalaModule)
  .settings()

lazy val scalaModule = project
  .in(file("scala-module"))
  .enablePlugins(ScalaNativePlugin)
  //.settings(commonSettings)
  .dependsOn(rustModule)
  .settings(
    nativeLinkingOptions := Seq(
      s"-L${baseDirectory.value.getParentFile}/rust-module/target/release/",
      "-lrust_code"
    ),
    libraryDependencies ++= Seq(
      //"org.typelevel" %% "cats-effect" % "3.5.2",
      "org.scalameta" %% "munit" % "0.7.29" % Test,
      "org.scalameta" %% "munit-scalacheck" % "0.7.29" % Test
    )
  )

lazy val rustModule = project
  .in(file("rust-module"))
  .settings()
