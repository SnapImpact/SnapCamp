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

import org.snapimpact.util.SkipHandler

class ApiHockeyDataTest extends Runner(new ApiHockeyDataSpec) with JUnit with Console
//
class ApiHockeyDataSpec extends Specification with ApiSubmitTester with RequestKit
{
  def baseUrl = "http://localhost:8989"
  RunWebApp.start()

  override def testParams(p: (String, Any)*)(tf: Int => Unit) {
    if (p.find(_._1 == "num").isDefined) super.testParams(p :_*)(tf)
    else super.testParams("num" -> 100 :: p.toList :_*)(tf)
  }

  "HockeySample loaded api"should
  {

    // Upload the sample file, that holds the test data
    "Accept valid xml upload" in {
      post("/api/upload?key=somekey",
           XML.loadFile("src/test/resources/HockeyData.xml")).
      map(_.code) must_== Full(200)
    }

    
    "return exact count of events" in {  
      testParams("q" -> "game", "num" -> 250) {
        _ must be >= 200
      }
    }

    "return exact count of events in category Regular Season" in { 
      testParams("q" -> "category:Regular Season") {
        _ must be >= 47
      }
    }

    "return exact count of events in category Playoffs" in { 
      testParams("q" -> "category:Playoffs") {
        _ must be >= 58
      }
    }

    "return exact count of events in category Exhibition" in { 
      testParams("q" -> "category:Exhibition") {
        _ must be >= 56
      }
    }

    "return exact count of events in category Pre Season" in { 
      testParams("q" -> "category:Pre Season") {
        _ must be >= 36
      }
    }

    "return exact count of events in category Post Season" in { 
      testParams("q" -> "category:Post Season") {
        _ must be >= 53
      }
    }

    "return exact count of events in location 10001" in {  
      testParams("vol_loc" -> "10001") {
        _ must be >= 46
      }
    }

    "return exact count of events in location 20004" in {  
      testParams("vol_loc" -> "20004") {
        _ must be >= 44
      }
    }

    "return exact count of events in location 80204" in {  
      testParams("vol_loc" -> "80204") {
        _ must be >= 61
      }
    }

    "return exact count of events in location 95113" in {  
      testParams("vol_loc" -> "95113") {
        _ must be >= 45
      }
    }

    "return exact count of events in location 07102" in {  
      testParams("vol_loc" -> "07102") {
          _ must be >= 54
      }
    }

    "return exact count of events for date 5/13/2010 12:00:00 AM" in { 
      SkipHandler {
        testParams("vol_startdate" -> later(5),
                   "vol_enddate" -> later(6)) {
                     _ must be >= 53
                   }
      }
    }

    "return exact count of events for date 5/18/2010 12:00:00 AM" in { 
      SkipHandler {
        testParams("vol_startdate" -> later(10),
                   "vol_enddate" -> later(11)) {
                     _ must be >= 52
                   }
      }
    }

    "return exact count of events for date 5/23/2010 12:00:00 AM" in { 
      SkipHandler {
        testParams("vol_startdate" -> later(15),
                   "vol_enddate" -> later(16))
        {
          _ must be >= 58
        }
      }
    }

    "return exact count of events for date 5/28/2010 12:00:00 AM" in {
      SkipHandler {
        testParams("vol_startdate" -> later(20),
                   "vol_enddate" -> later(21)) {
                     _ must be >= 45
                   }
      }
    }
 

    "return exact count of events for date 6/2/2010 12:00:00 AM" in { 
      SkipHandler {
        testParams("vol_startdate" -> later(25),
                   "vol_enddate" -> later(26)) {
                     _ must be >= 42
                   }
      }
    }

    "return exact count of events for game #249" in {
      testParams("q" -> "\"game #249\"") {
        _ must_== 1
      }
    }
    
    "return exact count of events for game #76" in {  
      testParams("q" -> "\"game #76\"" ) {
        _ must_== 1
      }
    }
    
    "return exact count of events for game #141" in {  
      testParams("q" -> "#141" ) {
        _ must_== 1
      }
    }

    "return exact count of events for game #54" in {  
      testParams("q" -> "\"game #54\"" ) {
        _ must_== 1
      }
    }

    "return exact count of events for game #103" in {  
      testParams("q" -> "#103" ) {
        _ must_== 1
      }
    }

    "return exact count of events for game #116" in {  
      testParams("q" -> "#116" ) {
        _ must_== 1
      }
    }

    "return exact count of events for game #192" in {  
      testParams("q" -> "#192" ) {
        _ must_== 1
      }
    }

    "return exact count of events for game #49" in {  
      testParams("q" -> "#49" ) {
        _ must_== 1
      }
    }

    "return exact count of events for game #27" in {  
      testParams("q" -> "#27" ) {
        _ must_== 1
      }
    }

    "return exact count of events for game #67" in {  
      testParams("q" -> "\"game #67\"" ) {
        _ must_== 1
      }
    }

    "return exact count of events for game #0" in {  
      testParams("q" -> "#0" ) {
        _ must_== 1
      }
    }

    "return exact count of events for game #249" in {  
      testParams("q" -> "#249" ) {
          _ must_== 1
      }
    }

    "return events for NY Rangers" in {  
      testParams("q" -> "NY Rangers" ) {
        _ must be >= 50
      }
    }

    "return events for Washington Caps" in {  
      testParams("q" -> "Washington Caps" ) {
        _ must be >= 50
      }
    }

    "return events for Colorado Avalanche" in {  
      testParams("q" -> "Colorado Avalanche" ) {
        _ must be >= 50
      }
    }

    "return events for San Jose Sharks" in {  
      testParams("q" -> "San Jose Sharks" ) {
        _ must be >= 50
      }
    }

    "return events for New Jersey Devils" in {  
      testParams("q" -> "New Jersey Devils"  ) {
        _ must be >= 50
      }
    }
  }  //  api should

}   // ApiSampleDataSpec
