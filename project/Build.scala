import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play-aloha"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.webjars" % "webjars-play" % "2.1.0-1",
    "org.webjars" % "bootstrap" % "2.3.1",
    "org.webjars" % "html5shiv" % "3.6.1",
    "org.jsoup" % "jsoup" % "1.7.2",
    "commons-io" % "commons-io" % "2.4",

    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
