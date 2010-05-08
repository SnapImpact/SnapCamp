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


class ApiSampleDataTest extends Runner(new ApiSampleDataSpec) with JUnit with Console
//
class ApiSampleDataSpec extends Specification with ApiSubmitTester with TestKit
{
  def baseUrl = "http://localhost:8989"
  RunWebApp.start()

  "Sample loaded api" should
  {

    // Upload the sample file, that holds the test data
    "Accept valid xml upload" in {
       post("/api/upload?key=somekey", XML.loadFile("src/test/resources/sampleData0.1.r1254.xml")) match {
         case r: HttpResponse =>
           r.code must_== 200
         case x => x must fail
       }
     }


      "not return events for something that is not available in the data " in
      {
            val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", "q" -> "zx_NotThere_xz" )
            // no events will be returned on this criteria zx_NotThere_xz
            val count = 0;
            //System.out.println( "* Expected zx_NotThere_xz val=" + count + ", was=" + ret.items.length )
            ( ret.items.length == count ) must_== true
      }

      "return events from description MicroMentor " in
      {
        org.snapimpact.util.SkipHandler.pendingUntilFixed{
            val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", "q" -> "MicroMentor" )
            // no events will be returned on this criteria zx_NotThere_xz
            val count = 1;
            ( ret.items.length == count ) must_== true
        }
      }

      "return events from description dodgeball " in
      {
            val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", "q" -> "dodgeball" )
            // no events will be returned on this criteria zx_NotThere_xz
            val count = 1;
            //System.out.println( "* Expected Dodgeball val=" + count + ", was=" + ret.items.length )
            ( ret.items.length == count ) must_== true
      }

      "return events from description volunteer" in
      {
        org.snapimpact.util.SkipHandler.pendingUntilFixed{
            val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", "q" -> "volunteer" )
            // no events will be returned on this criteria zx_NotThere_xz
            val count = 5;
            ( ret.items.length == count ) must_== true
        }
      }

    "return events from locaiton 97232 and Micro*" in
    {
      org.snapimpact.util.SkipHandler.pendingUntilFixed{
          val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", "q" -> "Micro*", "vol_loc" -> "97232" )
          // no events will be returned on this criteria zx_NotThere_xz
          val count = 1;
          ( ret.items.length == count ) must_== true
      }
    }

    "return events from locaiton Portland,OR and Micro*" in
    {
      org.snapimpact.util.SkipHandler.pendingUntilFixed{
          val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", "q" -> "Micro*", "vol_loc" -> "Portland,OR" )
          // no events will be returned on this criteria zx_NotThere_xz
          val count = 1;
          ( ret.items.length == count ) must_== true
      }
    }

    "return events from locaiton Portland,OR and MicroMentor" in
    {
      org.snapimpact.util.SkipHandler.pendingUntilFixed{
          val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", "q" -> "MicroMentor", "vol_loc" -> "Portland,OR" )
          // no events will be returned on this criteria zx_NotThere_xz
          val count = 1;
          ( ret.items.length == count ) must_== true
      }
    }


    "return events from locaiton CA and In" in
    {
      org.snapimpact.util.SkipHandler.pendingUntilFixed{
          val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", "q" -> "In", "vol_loc" -> "CA" )
          // no events will be returned on this criteria zx_NotThere_xz
          val count = 2;
          ( ret.items.length == count ) must_== true
      }
    }


    "return events from Hunger category" in
    {
      org.snapimpact.util.SkipHandler.pendingUntilFixed{
          val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", "q" -> "Hunger" )
          // no events will be returned on this criteria zx_NotThere_xz
          val count = 1;
          ( ret.items.length == count ) must_== true
      }
    }

    "return events from community category and descriptions" in
    {
      org.snapimpact.util.SkipHandler.pendingUntilFixed{
          val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", "q" -> "community" )
          // no events will be returned on this criteria zx_NotThere_xz
          val count = 6;
          ( ret.items.length == count ) must_== true
      }
    }

    // hunger cats
    "return events for hunger category" in {
      org.snapimpact.util.SkipHandler.pendingUntilFixed{
          val ret = submitApiRequest( "output" -> "json", "key" -> "UnitTest", "q" -> "category:Hunger" )

          val count = 0;
          //System.out.println( "* Expected Hunger val=" + count + ", was=" + ret.items.length )
          ( ret.items.length > count ) must_== true

          // Make sure they are not null
          for( item <- ret.items ){
            item must notBe( null )
          }
      }
    }


    // Done
    //RunWebApp.stop()

  }  //  "api" should

}   // ApiSampleDataSpec

