import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "PlayDate"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "org.codehaus.httpcache4j" % "httpcache4j-core" % "3.3",
      "org.codehaus.httpcache4j.resolvers" % "resolvers-httpcomponents-httpclient" % "3.3",
      "org.mindrot" % "jbcrypt" % "0.3m",
      "mysql" % "mysql-connector-java" % "5.1.18"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here      
    )

}
