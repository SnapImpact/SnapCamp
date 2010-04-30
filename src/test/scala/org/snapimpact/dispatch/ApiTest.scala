package org.snapimpact.dispatch

/**
 * Created by IntelliJ IDEA.
 * User: okristjansson
 * Date: Mar 16, 2010
 * Time: 8:38:30 PM
 * To change this template use File | Settings | File Templates.
 */

import _root_.org.specs._
import _root_.org.specs.runner._
import _root_.org.specs.Sugar._

import net.liftweb.http.testing._
import net.liftweb.common._
import net.liftweb.util.Helpers._
import net.liftweb.json._
import JsonParser._
import JsonAST._
import specification.Expectable

class APITest extends Runner(new APISpec) with JUnit with Console

class APISpec extends APITester {
  def baseUrl = "http://localhost:8989"
  RunWebApp.start()

  "api" should {

    "Give a 401 without a key" in {
      get("/api/volopps") match {
        case r: HttpResponse =>
          r.code must_== 401
        case x =>
          true must_== false
      }
    }

    "Give a 200 with a key" in {
      get("/api/volopps", "key" -> "test") match {
        case r: HttpResponse =>
          r.code must_== 200
        case x =>
          true must_== false
      }
    }

    skip("To be implemented")
    "provide common functionalities" in {sharedFunctionality}
  }

  //RunWebApp.stop()
}

class V1SysTest extends Runner(new V1SysSpec) with JUnit with Console

class V1SysSpec extends APITester {
  def baseUrl = "http://www.allforgood.org"

  "The API from the V1 system" should {
    "provide common functionalities" in {sharedFunctionality}
  }
}

trait APITester extends Specification with TestKit {
  def sharedFunctionality = {
    "be able to extract json return" in {
      get("/api/volopps", "key" -> "test", "q" -> "hunger", "output" -> "json") match {
        case r: HttpResponse => {
          implicit val formats = DefaultFormats
          r.code must_== 200
          val jString = tryo(new String(r.body, "UTF-8")).open_!
          val json = parse(jString)
          val ret = json.extract[RetV1]

          ret must haveClass[RetV1]
        }
        case _ => true must_== false
      }
    }
  }
}

import org.mortbay.jetty.Connector
import org.mortbay.jetty.Server
import org.mortbay.jetty.webapp.WebAppContext
import org.mortbay.jetty.nio._

object RunWebApp {
  {
    org.mortbay.log.Log.setLog(new org.mortbay.log.Logger {
      def debug(msg: String,a1: Object,a2: Object) {}
           
      def debug(msg: String,th: Throwable) {}
           
      def getLogger(name: String) = this
           
      def info(msg: String,a1: Object,a2: Object) {}
           
      def isDebugEnabled() = false
           
      def setDebugEnabled(e: Boolean) {} 
      
      def warn(msg: String,a1: Object,a2: Object) {}
           
      def warn(msg: String,th: Throwable) {}
    })
  }

  private val server = new Server
  private val scc = new SelectChannelConnector
  scc.setPort(8989)
  server.setConnectors(Array(scc))

  private val context = new WebAppContext()
  context.setServer(server)
  context.setContextPath("/")
  context.setWar("src/main/webapp")

  server.addHandler(context)

  def start() = server.start()

  def stop() = {
    server.stop()
    server.join()
  }
}

