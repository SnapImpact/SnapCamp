import sbt._

class SnapCampProject(info: ProjectInfo) extends DefaultWebProject(info) {
  val mavenLocal = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"
  val liftVersion = "2.0-scala280-SNAPSHOT"

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
    "joda-time" % "joda-time" % "1.6",
    // "org.scala-tools" % "time" % "2.8.0-0.1",
    "commons-httpclient" % "commons-httpclient" % "3.1"
  ) ++ super.libraryDependencies

  // required because Ivy doesn't pull repositories from poms
  // val smackRepo = "m2-repository-smack" at "http://maven.reucon.com/public"


  //UGLY: Growl notifications for successful / failed tests 
  override def testListeners : Seq[TestReportListener] = super.testListeners ++ Seq(new TestReportListener {
      def startGroup(name: String) = {}
      /** called for each test method or equivalent */
      def testEvent(event: TestEvent) = {}
      /** called if there was an error during test */
      def endGroup(name: String, t: Throwable) = {}
      /** called if test completed */
      def endGroup(name: String, result: Result.Value) = {

          import Process._

          result match {
              case Result.Passed => "growlnotify -m pass" !
              case _ => "growlnotify -m FAIL " + name + "" !
          }
      }
      /** Used by the test framework for logging test results*/
      //override def contentLogger: Option[org.scalatools.testing.Logger] = None
  })
}
