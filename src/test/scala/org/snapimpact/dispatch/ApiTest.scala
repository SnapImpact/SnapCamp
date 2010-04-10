package org.snapimpact.dispatch

/**
 * Created by IntelliJ IDEA.
 * User: okristjansson
 * Date: Mar 16, 2010
 * Time: 8:38:30 PM
 * To change this template use File | Settings | File Templates.
 */

import _root_.junit.framework._


import _root_.org.specs._
import _root_.org.specs.runner._
import _root_.org.specs.Sugar._

import net.liftweb.http.testing._

/*
class APITest extends Runner(new APISpec) with JUnit with Console

class APISpec extends Specification with TestKit {
  def baseUrl = "http://localhost:8989"
  RunWebApp.start

  "api" should {
    "Give a 401 without a key" in {

      get("/api/volopp") match {
	case r: HttpResponse => 
	  println("Got response "+r.code)
	r.code must_== 401
	case x =>
	  println("Got a "+x.getClass)
	true must_== false
      }
    }
  }
}

object ApiTest {
  def suite: Test = {
    val suite = new TestSuite(classOf[ApiTest])
    suite
  }

  def main(args : Array[String]) {
    _root_.junit.textui.TestRunner.run(suite)
  }
}

/**
 * Read the sample file
 */
class ApiTest extends TestCase("app")
{

  def testSearch() =
    {
      val lEvents = MockSearch.getEvents()
      println( "Event Count=" + lEvents.length );
      assert( lEvents.length > 0  )
    }

}

import _root_.org.mortbay.jetty.Connector
import _root_.org.mortbay.jetty.Server
import _root_.org.mortbay.jetty.webapp.WebAppContext
import org.mortbay.jetty.nio._

object RunWebApp extends Application {
  val server = new Server
  val scc = new SelectChannelConnector
  scc.setPort(8989)
  server.setConnectors(Array(scc))

  val context = new WebAppContext()
  context.setServer(server)
  context.setContextPath("/")
  context.setWar("src/main/webapp")

  server.addHandler(context)

  lazy val start = server.start()

  def end() = {
    server.stop()
    server.join()
  }

}
*/
