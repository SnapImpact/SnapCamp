import sbt._

class SnapCampProject(info: ProjectInfo) extends DefaultWebProject(info) {
  val mavenLocal = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"
  val liftVersion = "2.0-M2" 

  override def libraryDependencies = Set(
    "net.liftweb" % "lift-webkit" % liftVersion % "compile->default", 
    "net.liftweb" % "lift-mapper" % liftVersion % "compile->default", 
    "net.liftweb" % "lift-jpa" % liftVersion % "compile->default", 
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default", 
    "com.h2database" % "h2" % "1.2.121",
    "junit" % "junit" % "4.5" % "test->default",
    "org.scala-tools.testing" % "specs" % "1.6.1" % "test->default",
    "postgresql" % "postgresql" % "8.4-701.jdbc4",
    "org.slf4j" % "slf4j-log4j12" % "1.4.1",
    "org.eclipse.persistence" % "eclipselink" % "2.0.0",
    "javax.persistence" % "persistence-api" % "1.0",
  ) ++ super.libraryDependencies

  // required because Ivy doesn't pull repositories from poms 
  val smackRepo = "m2-repository-smack" at "http://maven.reucon.com/public"
}
