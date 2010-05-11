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
import _root_.org.specs.execute._
import org.joda.time._
import org.joda.time.format._


// Tests against the local server
class APITest extends Runner(new APISpec) with JUnit with Console

class APISpec extends Specification with ApiSubmitTester with RequestKit
{
  def baseUrl = "http://localhost:8989"
  RunWebApp.start()

  "api" should {

    "Give a 401 without a key" in {
      get("/api/volopps", "q" -> "hunger").map(_.code) must_== Full(401)
      /*match {
        case r: HttpResponse =>
          r.code must_== 401
        case x =>
          true must_== false
      }*/
    }

    "Give a 200 with a key" in {
      get("/api/volopps", "key" -> "test", "q" -> "hunger").
      map(_.code)  must_== Full(200)
      /*match {
        case r: HttpResponse =>
          r.code must_== 200
        case x =>
          true must_== false
      }*/
    }



    // SkipHandler skips tests as long as they do not pass
    // once these pass that means somebody is making progress on the API development
    // and the pendingUntilFixed wrapper can be excluded
    "provide common functionalities" in
    {
         sharedFunctionality
    }

    // Searches
    "search for something not there" in {
                searchFor_zx_NotThere_xz
    }
    "search for hunger" in {
                searchForHunger
    }
    "search for specific dates" in {
      org.snapimpact.util.SkipHandler.pendingUntilFixed{searchForSpecificDates}
    }
    "search for zip code" in {
      org.snapimpact.util.SkipHandler.pendingUntilFixed{searchForZip}
    }
    "search for date then zip code" in {
      org.snapimpact.util.SkipHandler.pendingUntilFixed{searchForDateThenZip}
    }

  }  //  "api" should
}   // ApiSpec





// These tests are going against the current AllForGood live webserver
// http://www.allforgood.org and should succeed at all times

class V1SysTest extends Runner(new V1SysSpec) with JUnit with Console

class V1SysSpec extends Specification with RequestKit with ApiSubmitTester
{
  def baseUrl = "http://www.allforgood.org"

  "The API from the old V1 system" should
  {
    "extracts common" in {sharedFunctionality}
  }

  // This times out the server, let's skip it
  //  "search for something not there" in {zx_NotThere_xz }
  

  "The API from the old V1 system" should
  {
	"search for hunger" in {searchForHunger}
	
    "search for specific dates" in {searchForSpecificDates}

    "search for zip code" in {searchForZip}

    "search for date then zip code" in {searchForDateThenZip}
  }
}  // V1SysSpec





// Does the actual interaction with the webserver and includes the common tests
trait ApiSubmitTester // extends  // with TestKit
{
  self: Specification with RequestKit =>

    // Returns RetV1 object from volopps API search
    def submitApiRequest( pars: (String, Any)*): Box[RetV1] =
    {
      for {
        answer <- get( "/api/volopps", pars :_* ) if answer.code == 200
        val jString = new String(answer.body)
        json <- tryo(parse(jString))
        ret <- tryo(json.extract[RetV1])
      } yield ret
    }


    // Test for root and props set
    def sharedFunctionality = {

        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", "q" -> "a" ).open_!

        // Root of correct type
        ret must haveClass[RetV1]

        // All good prop
        val descr = "All for Good search results"
        ret.description mustEqual descr
        
        // version prop
        val ver = 1.0
        ret.version mustEqual ver

        // See if the date is good and can be parsed
        // sample string -> Sat, 01 May 2010 16:51:10 +0000
        val dateFormatter = DateTimeFormat.forPattern("E, dd MMM yyyy HH:mm:ss Z");
        val item =  dateFormatter.parseDateTime( ret.lastBuildDate )
        // Make sure it's a DateTime
        item must haveClass[DateTime]
   }

   // Search for something not available in the database
   def searchFor_zx_NotThere_xz = {
     val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", "q" -> "zx_NotThere_xz" ).open_!
     
     val count = 0
     
     ret.items.length must_== count
  }

    // *** Note *** This test assumes that there are always hunger events available in the database
    def searchForHunger= {
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", "q" -> "hunger" ).open_!

      val count = 0
      ret.items.length must be > count

      // Make sure they are not null
      for( item <- ret.items ){
        item must notBe( null )
      }
   }

  // Search by date - always assumes there are events bewteen now + 7 days and now + 14 days
  def searchForSpecificDates = {
    val fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
    val now = new DateTime
    val plus7 = now.plusDays(7)
    val plus14 = now.plusDays(14)

    val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest",
      "vol_startdate" -> fmt.print(plus7), "vol_enddate" -> fmt.print(plus14)).open_!

    val count = 0
    ret.items.length must be > count
    
    // Make sure they are not null
    for( item <- ret.items ){
      item must notBe( null )
    }
  }

  // Search by zip code - always assumes there are events in 94117 (San Francisco)
  def searchForZip = {
    val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", "vol_loc" -> "94117" ).open_!

    val count = 0
    ret.items.length must be > count
    
    // Make sure they are not null
    for( item <- ret.items ){
      item must notBe( null )
    }
  }

  // Search by date then zip code - always assumes there are events bewteen now + 7 days and now + 14 days
  // near the zip 94117 (San Francisco)
  def searchForDateThenZip = {
    val fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
    val now = new DateTime
    val plus7 = now.plusDays(7)
    val plus14 = now.plusDays(14)

    val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest",
                               "vol_startdate" -> fmt.print(plus7), 
                               "vol_enddate" -> fmt.print(plus14),
                               "vol_loc" -> "94117").open_!
    
    val count = 0
    ret.items.length must be > count
    
    // Make sure they are not null
    for( item <- ret.items ){
      item must notBe( null )
    }
  }

}  // trait ApiSubmitTester




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

  def start() ={
    import java.io.{File => JFile}
    server.start()
    for {
      dir <- tryo{new JFile("./docs/test_data")}
      files <- Box !! dir.listFiles
    } Uploader.upload(files.toList)
  }

  def stop() = {
    server.stop()
    server.join()
  }
}

object Uploader extends RequestKit {
  import java.io.{File => JFile, ByteArrayInputStream}
  import scala.xml.XML

  def baseUrl = "http://localhost:8989"

  def upload(in: List[JFile]) {
    for {
      file <- in.take(1)
      bytes <- tryo{readWholeFile(file)}
      xml <- tryo{XML.load(new ByteArrayInputStream(bytes))}
    } {
      post("/api/upload?key=test", xml)
    }
  }
}
