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

class APITest extends Runner(new APISpec) with JUnit with Console

class APISpec extends Specification with TestKit {
  def baseUrl = "http://localhost:8989"

  "api" should {
    doFirst {
      RunWebApp.start()
    }

    doLast {
      RunWebApp.stop()
    }

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

