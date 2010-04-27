package org.snapimpact.dispatch

/**
 * Created by IntelliJ IDEA.
 * User: okristjansson
 * Date: Mar 16, 2010
 * Time: 8:38:30 PM
 * To change this template use File | Settings | File Templates.
 */
import java.io.Serializable
import net.liftweb.json._
import _root_.junit.framework._
import net.liftweb.util.JSONParser
import org.snapimpact.model.Event



import _root_.org.specs._
import _root_.org.specs.runner._
import _root_.org.specs.Sugar._

import net.liftweb.http.testing._

class APITest extends Runner(new APISpec) with JUnit with Console

class APISpec extends Specification with TestKit {
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

    "Return a list of opportunities for query" in {
      val ret = get("/api/volopps", "key" -> "test", "q" -> "hunger", "output" -> "json")

      ret match {
        case r: HttpResponse =>
          r.code must_== 200
        case x =>
          true must_== false
      }
    }
  }

  //RunWebApp.stop()
}

import org.mortbay.jetty.Connector
import org.mortbay.jetty.Server
import org.mortbay.jetty.webapp.WebAppContext
import org.mortbay.jetty.nio._

object RunWebApp {
  {
    org.mortbay.log.Log.setLog(new org.mortbay.log.Logger {
      def debug(msg: String, a1: Object, a2: Object) {}

      def debug(msg: String, th: Throwable) {}

      def getLogger(name: String) = this

      def info(msg: String, a1: Object, a2: Object) {}

      def isDebugEnabled() = false

      def setDebugEnabled(e: Boolean) {}

      def warn(msg: String, a1: Object, a2: Object) {}

      def warn(msg: String, th: Throwable) {}
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


  def testSerializeEvents() =
  {
    // Init serializer
    implicit val formats = Serialization.formats(NoTypeHints)


    // First make a simple test class
    case class Sample(categories: List[String], quality_score: Long, pageviews: Int, Other1: String, Other2: String)
    //val sample2 = Sample(List("category1", "category2"), 535, 2312, "Other1TestData", "Other2TestData")
    val sample = Serialization.write( Sample(List("category1", "category2"), 535, 2312, "Other1TestData", null) )
    //
    println( "Sample=" + sample );



    // Serialize event
    val lEvents = MockSearch.getEvents
    val myEvent = lEvents{0};
    val sampleJson = Serialization.write(myEvent)
    //
    println( "Event=" + sampleJson );

  }


}

