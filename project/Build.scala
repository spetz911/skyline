import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "skyline"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    "org.mongodb" %% "casbah" % "2.6.4",
    "net.debasishg" %% "redisclient" % "2.10",
    "org.apache.lucene" % "lucene-core" % "4.6.0",
    "org.apache.lucene" % "lucene-analyzers-common" % "4.6.0",
    "org.apache.lucene" % "lucene-queryparser" % "4.6.0"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
