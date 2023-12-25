scalaVersion := "3.3.1"


// set to Debug for compilation details (Info is default)
logLevel := Level.Info

// import to add Scala Native options
import scala.scalanative.build._

// defaults set with common options shown
nativeConfig ~= { c =>
  c.withLTO(LTO.none) // thin
    .withMode(Mode.debug) // releaseFast
    .withGC(GC.immix) // commix
}

lazy val root = (project in file("."))
  .aggregate(scalaModule)
  .settings(
    // Common settings or dependencies for the entire monorepo
  )

lazy val scalaModule = project
  .in(file("scala-module"))
  .enablePlugins(ScalaNativePlugin)
  .settings(
    // Scala-specific settings or dependencies
  )