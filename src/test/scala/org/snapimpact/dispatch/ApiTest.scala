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
import net.liftweb.common._
import net.liftweb.util.Helpers._
import net.liftweb.json._
import JsonParser._
import JsonAST._
import specification.Expectable

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

    skip("To be implemented")
    "Return a list of opportunities for query" in {
      get("/api/volopps", "key" -> "test", "q" -> "hunger", "output" -> "json") match {
        case r: HttpResponse => TestResponse(r)
        case _ => true must_== false
      }
    }
  }

  //RunWebApp.stop()
}

class V1SysTest extends Runner(new V1SysSpec) with JUnit with Console

class V1SysSpec extends Specification with TestKit {
  def baseUrl = "http://www.allforgood.org"

  "The API from the V1 system" should {

    "be able to be extracted into v2 case classes" in {
      get("/api/volopps", "key" -> "test", "q" -> "hunger", "output" -> "json") match {
        case r: HttpResponse => TestResponse(r)
        case _ => true must_== false
      }
    }
  }
}

object TestResponse extends SpecsMatchers {
  def apply(r: HttpResponse) = {
    implicit val formats = DefaultFormats
    r.code must_== 200
    val jString = tryo(new String(r.body, "UTF-8")).open_!
    val json = parse(jString)
    val ret = json.extract[RetV1]

    ret must haveClass[RetV1]
  }
}



/*
{
 "lastBuildDate": "Tue, 27 Apr 2010 02:58:23 +0000",
 "version": 1.0,
 "language": "en-us",
 "items": [
  {
   "startDate": "2010-05-08 09:00:00",
   "minAge": "",
   "endDate": "2010-05-08 16:00:00",
   "contactPhone": "",
   "quality_score": 0.1,
   "detailUrl": "",
   "sponsoringOrganizationName": "NAMI Maine",
   "latlong": "43.6629964,-70.2568775",
   "contactName": "",
   "addr1": "",
   "impressions": 0,
   "id": "b4d13ce62988acf120e6321faac4f312",
   "city": "",
   "location_name": "Portland, ME 04101",
   "openEnded": "",
   "pubDate": "",
   "title": "NAMI Walk Assistant",
   "base_url": "b4d13ce62988acf120e6321faac4f312",
   "virtual": "",
   "backfill_title": "",
   "provider": "volunteermatch",
   "postalCode": "",
   "groupid": "M96fd495ff67a2736568f01dfca8dec0e",
   "audienceAge": "",
   "audienceAll": "",
   "description": "Assist NAMI staff with a variety of tasks associated with NAMI Maine's fundraising walk on Saturday May 8th 2010 at Portland ME. Assignments include setting up tables, handing out food and beverages, face-painting, putting up signs along the walk path, and packing up after the event. This is a fun, ",
   "street1": "",
   "street2": "",
   "interest_count": 0,
   "xml_url": "http:\/\/www.volunteermatch.org\/search\/opp655577.jsp#b4d13ce62988acf120e6321faac4f312",
   "audienceSexRestricted": "",
   "startTime": 900,
   "contactNoneNeeded": "",
   "categories": [
    "Hunger"
   ],
   "contactEmail": "",
   "skills": "",
   "country": "",
   "region": "",
   "url_short": "www.volunteermatch.org",
   "addrname1": "",
   "backfill_number": 0,
   "endTime": 1600
  },
  {
   "startDate": "2010-05-08 14:00:00",
   "minAge": "",
   "endDate": "2010-05-08 18:00:00",
   "contactPhone": "",
   "quality_score": 0.1,
   "detailUrl": "",
   "sponsoringOrganizationName": "The Salvation Army",
   "latlong": "43.6629964,-70.2568775",
   "contactName": "",
   "addr1": "",
   "impressions": 0,
   "id": "46c2b3c1c307128ed5bce722ee46b872",
   "city": "",
   "location_name": "Portland, ME 04101",
   "openEnded": "",
   "pubDate": "",
   "title": "Food Drive Volunteer",
   "base_url": "46c2b3c1c307128ed5bce722ee46b872",
   "virtual": "",
   "backfill_title": "",
   "provider": "volunteermatch",
   "postalCode": "",
   "groupid": "M6eb4888015e56771aa6eeaec601786c3",
   "audienceAge": "",
   "audienceAll": "",
   "description": "Each year The Salvation Army of Greater Portland is the grateful recipient of tons (literally!) of food collected by local postal workers during the Annual Postal Worker Food Drive. Volunteers are essential to make this day a success. Join us Saturday, May 8th, from 2:00 - 6:00 p.m. to load and unlo",
   "street1": "",
   "street2": "",
   "interest_count": 0,
   "xml_url": "http:\/\/www.volunteermatch.org\/search\/opp653092.jsp#46c2b3c1c307128ed5bce722ee46b872",
   "audienceSexRestricted": "",
   "startTime": 1400,
   "contactNoneNeeded": "",
   "categories": [
    "Hunger"
   ],
   "contactEmail": "",
   "skills": "",
   "country": "",
   "region": "",
   "url_short": "www.volunteermatch.org",
   "addrname1": "",
   "backfill_number": 0,
   "endTime": 1800
  }
   ],
 "href": "http:\/\/www.allforgood.org\/api\/volopps?key=snapimpact&q=hunger&output=json",
 "description": "All for Good search results"
 }
 */

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

