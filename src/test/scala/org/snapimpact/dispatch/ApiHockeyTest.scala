package org.snapimpact.dispatch

/**
 * Created by IntelliJ IDEA.
 * User: flipper
 * Date: May 5, 2010
 * Time: 9:24:15 PM
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
import scala.xml.XML

import org.snapimpact.util._

class ApiHockeyDataTest extends Runner(new ApiHockeyDataSpec) with JUnit with Console
//
class ApiHockeyDataSpec extends Specification with ApiSubmitTester with RequestKit
{
  def baseUrl = "http://localhost:8989"
  RunWebApp.start()

  "HockeySample loaded api"should
  {

    // Upload the sample file, that holds the test data
    "Accept valid xml upload" in {
      post("/api/upload?key=somekey", XML.loadFile("src/test/resources/HockeyData.xml")).map(_.code) must_== Full(200)
    }

    "return exact count of events" in {  
      SkipHandler.pendingUntilFixed{
        testParams("q" -> "game") {
          _ must be >= 250
        }
      }
    }

    "return exact count of events in category Regular Season" in { 
      SkipHandler.pendingUntilFixed{
        testParams("q" -> "category:Regular Season") {
          _ must be >= 47
        }
      }
    }


    "return exact count of events in category Playoffs" in { 
      SkipHandler.pendingUntilFixed{
        testParams("q" -> "category:Playoffs") {
          ret.items.length must be >= 58
        }
      }
    }
    


    "return exact count of events in category Exhibition" in { 
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "q" -> "category:Exhibition").open_!
        
        val count = 56
        ret.items.length must be >= count
        
        for( item <- ret.items ){
          item must notBe( null )}
      }
    }



    "return exact count of events in category Pre Season" in { 
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "q" -> "category:Pre Season").open_!
        
        val count = 36
        ret.items.length must be >= count
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events in category Post Season" in { 
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "q" -> "category:Post Season").open_!
        
        val count = 53
        ret.items.length must be >= count
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events in location 10001" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "vol_loc" -> "10001").open_!
        
        val count = 46
        ret.items.length must be >= count
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events in location 20004" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "vol_loc" -> "20004").open_!
        
        val count = 44
        ret.items.length must be >= count
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events in location 80204" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "vol_loc" -> "80204").open_!
        
        val count = 61
        ret.items.length must be >= count
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events in location 95113" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "vol_loc" -> "95113").open_!
        
        val count = 45
        ret.items.length must be >= count
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events in location 07102" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "vol_loc" -> "07102").open_!
        
        val count = 54
        ret.items.length must be >= count
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events for date 5/13/2010 12:00:00 AM" in { 
      SkipHandler.pendingUntilFixed{
        val fmt = DateTimeFormat.forPattern("yyyy-MM-dd")
        val now = new DateTime
        val plus7 = now.plusDays(5)
        val plus14 = now.plusDays(6)
        
        val ret = submitApiRequest( "output" -> "json", 
                                   "key" -> "UnitTest", 
                                   "vol_startdate" -> fmt.print(plus7),
                                   "vol_enddate" -> fmt.print(plus14)).open_!
        
        val count = 53
        ret.items.length must be >= count

        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events for date 5/18/2010 12:00:00 AM" in { 
      SkipHandler.pendingUntilFixed{
        val fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        val now = new DateTime
        val plus7 = now.plusDays(10)
        val plus14 = now.plusDays(11)
        
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "vol_startdate" -> fmt.print(plus7),
                                   "vol_enddate" -> fmt.print(plus14)).open_!
        
        val count = 52
        ret.items.length must be >= count

        for( item <- ret.items ){
          item must notBe( null )}
      }}


    def fmt = DateTimeFormat.forPattern("yyyy-MM-dd")

    def now = new DateTime

    def later(days: Int):String = fmt.print(now.plusDays(days))

    def testParams(p: (String, Any)*)(countTest: Int => Unit) {
      submitApiRequest("output" -> "json" :: 
                      "key" -> "UnitTest" :: p.toList :_*) match {
        case Full(ret) => {
          countTest(ret.items.length)
          for( item <- ret.items ) item must notBe( null )
        }

        case x => fail(x.toString)
      }
    }


    "return exact count of events for date 5/23/2010 12:00:00 AM" in { 
      SkipHandler.pendingUntilFixed{
        testParams("vol_startdate" -> later(15),
                   "vol_enddate" -> later(16)) {_ must be >= 58}
      }}



    "return exact count of events for date 5/28/2010 12:00:00 AM" in { 
      SkipHandler.pendingUntilFixed{
        val fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        val now = new DateTime
        val plus7 = now.plusDays(20)
        val plus14 = now.plusDays(21)
        
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "vol_startdate" -> fmt.print(plus7), "vol_enddate" -> fmt.print(plus14)) 
        
        val count = 45;
        ( ret.items.length == count ) must_== true
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events for date 6/2/2010 12:00:00 AM" in { 
      SkipHandler.pendingUntilFixed{
        val fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        val now = new DateTime
        val plus7 = now.plusDays(25)
        val plus14 = now.plusDays(26)
        
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "vol_startdate" -> fmt.print(plus7), "vol_enddate" -> fmt.print(plus14)) 
        
        val count = 42;
        ( ret.items.length == count ) must_== true
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events for game #249" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "q" -> "game #249" )
        
        val count = 1;
        ( ret.items.length == count ) must_== true
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events for game #76" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "q" -> "game #76" )
        
        val count = 1;
        ( ret.items.length == count ) must_== true
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events for game #141" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "q" -> "game #141" )
        
        val count = 1;
        ( ret.items.length == count ) must_== true
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events for game #54" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "q" -> "game #54" )
        
        val count = 1;
        ( ret.items.length == count ) must_== true
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events for game #103" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "q" -> "game #103" )
        
        val count = 1;
        ( ret.items.length == count ) must_== true
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events for game #116" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "q" -> "game #116" )
        
        val count = 1;
        ( ret.items.length == count ) must_== true
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events for game #192" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "q" -> "game #192" )
        
        val count = 1;
        ( ret.items.length == count ) must_== true
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events for game #49" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "q" -> "game #49" )
        
        val count = 1;
        ( ret.items.length == count ) must_== true
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events for game #27" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "q" -> "game #27" )
        
        val count = 1;
        ( ret.items.length == count ) must_== true
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events for game #67" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "q" -> "game #67" )
        
        val count = 1;
        ( ret.items.length == count ) must_== true
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events for game #0" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "q" -> "game #0" )
        
        val count = 1;
        ( ret.items.length == count ) must_== true
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return exact count of events for game #249" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "q" -> "game #249" )
        
        val count = 1;
        ( ret.items.length == count ) must_== true
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return events for NY Rangers" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "q" -> "NY Rangers" )
        
        val count = 50;
        ( ret.items.length >= count ) must_== true
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return events for Washington Caps" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "q" -> "Washington Caps" )
        
        val count = 50;
        ( ret.items.length >= count ) must_== true
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return events for Colorado Avalanche" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "q" -> "Colorado Avalanche" )
        
        val count = 50;
        ( ret.items.length >= count ) must_== true
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return events for San Jose Sharks" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "q" -> "San Jose Sharks" )
        
        val count = 50;
        ( ret.items.length >= count ) must_== true
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}



    "return events for New Jersey Devils" in {  
      SkipHandler.pendingUntilFixed{
        val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
                                   "q" -> "New Jersey Devils" )
        
        val count = 50;
        ( ret.items.length >= count ) must_== true
        
        for( item <- ret.items ){
          item must notBe( null )}
      }}




    // Done
    //RunWebApp.stop()

  }  //  api should

}   // ApiSampleDataSpec
