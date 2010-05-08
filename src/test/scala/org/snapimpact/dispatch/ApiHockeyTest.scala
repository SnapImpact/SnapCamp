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


class ApiHockeyDataTest extends Runner(new ApiHockeyDataSpec) with JUnit with Console
//
class ApiHockeyDataSpec extends Specification with ApiSubmitTester with TestKit
{
  def baseUrl = "http://localhost:8989"
  RunWebApp.start()

  "HockeySample loaded api"should
  {

    // Upload the sample file, that holds the test data
    "Accept valid xml upload" in {
       post("/api/upload?key=somekey", XML.loadFile("src/test/resources/HockeyData.xml")) match {
         case r: HttpResponse =>
           r.code must_== 200
         case x => x must fail
       }
     }

    "return exact count of events" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "game" )
      
      val count = 250;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events in category Regular Season" in { 
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "category:Regular Season")
      
      val count = 47;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events in category Playoffs" in { 
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "category:Playoffs")
      
      val count = 58;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events in category Exhibition" in { 
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "category:Exhibition")
      
      val count = 56;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events in category Pre Season" in { 
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "category:Pre Season")
      
      val count = 36;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events in category Post Season" in { 
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "category:Post Season")
      
      val count = 53;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events in location 10001" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "vol_loc" -> "10001")
      
      val count = 46;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events in location 20004" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "vol_loc" -> "20004")
      
      val count = 44;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events in location 80204" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "vol_loc" -> "80204")
      
      val count = 61;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events in location 95113" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "vol_loc" -> "95113")
      
      val count = 45;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events in location 07102" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "vol_loc" -> "07102")
      
      val count = 54;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events for date 5/13/2010 12:00:00 AM" in { 
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
      val now = new DateTime
      val plus7 = now.plusDays(5)
      val plus14 = now.plusDays(6)
      
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "vol_startdate" -> fmt.print(plus7), "vol_enddate" -> fmt.print(plus14)) 
      
      val count = 53;
      ( ret.items.length == count ) must_== true
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events for date 5/18/2010 12:00:00 AM" in { 
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
      val now = new DateTime
      val plus7 = now.plusDays(10)
      val plus14 = now.plusDays(11)
      
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "vol_startdate" -> fmt.print(plus7), "vol_enddate" -> fmt.print(plus14)) 
      
      val count = 52;
      ( ret.items.length == count ) must_== true
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events for date 5/23/2010 12:00:00 AM" in { 
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
      val now = new DateTime
      val plus7 = now.plusDays(15)
      val plus14 = now.plusDays(16)
      
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "vol_startdate" -> fmt.print(plus7), "vol_enddate" -> fmt.print(plus14)) 
      
      val count = 58;
      ( ret.items.length == count ) must_== true
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events for date 5/28/2010 12:00:00 AM" in { 
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
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
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
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
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "game #249" )
      
      val count = 1;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events for game #76" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "game #76" )
      
      val count = 1;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events for game #141" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "game #141" )
      
      val count = 1;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events for game #54" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "game #54" )
      
      val count = 1;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events for game #103" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "game #103" )
      
      val count = 1;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events for game #116" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "game #116" )
      
      val count = 1;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events for game #192" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "game #192" )
      
      val count = 1;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events for game #49" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "game #49" )
      
      val count = 1;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events for game #27" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "game #27" )
      
      val count = 1;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events for game #67" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "game #67" )
      
      val count = 1;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events for game #0" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "game #0" )
      
      val count = 1;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return exact count of events for game #249" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "game #249" )
      
      val count = 1;
      ( ret.items.length == count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return events for NY Rangers" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "NY Rangers" )
      
      val count = 50;
      ( ret.items.length >= count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return events for Washington Caps" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "Washington Caps" )
      
      val count = 50;
      ( ret.items.length >= count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return events for Colorado Avalanche" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "Colorado Avalanche" )
      
      val count = 50;
      ( ret.items.length >= count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return events for San Jose Sharks" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
      val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", 
      "q" -> "San Jose Sharks" )
      
      val count = 50;
      ( ret.items.length >= count ) must_== true
      
      for( item <- ret.items ){
      item must notBe( null )}
}}



"return events for New Jersey Devils" in {  
  org.snapimpact.util.SkipHandler.pendingUntilFixed{
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
