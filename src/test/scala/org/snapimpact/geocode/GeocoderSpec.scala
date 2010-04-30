package org.snapimpact.geocode

import org.slf4j.LoggerFactory

import org.specs._
import org.specs.runner._
import org.snapimpact.model.GeoLocation
import net.liftweb.common._
import org.specs.matcher._


/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: Apr 20, 2010
 * Time: 11:46:36 PM
 * To change this template use File | Settings | File Templates.
 */

class GeocoderSpecTest extends Runner(new GeocoderSpec) with
        JUnit with Console

class GeocoderSpec extends Specification {



  "Geocoder" should {
    "take a String and return a GeoLocation" in {
      val goog = "1600 Amphitheatre Parkway, Mountain View, CA"
      val gl = Geocoder(goog)
      val expectedLat = 37.422782
      val expectedLong = -122.085099
      //
      System.out.println( "* Expected lat=" + expectedLat + ", was=" + gl.get.latitude )
      System.out.println( "* Expected long=" + expectedLong + ", was=" + gl.get.longitude )
      //
      gl.get must haveClass[GeoLocation]
      gl.get.latitude must beCloseTo( expectedLat, 0.009999 )
      gl.get.longitude must beCloseTo( expectedLong, 0.009999 )
    }
  }

  "GeocoderEmpty" should {
    "take empty String and return empty / None object" in {
      val fubar = ""
      val glFubar = Geocoder(fubar)
      //
      System.out.println( "* Expected val=None, was=" + glFubar )
      //
      glFubar must_== Empty // beNone
    }
  }


  // Google API sample was used for extracting Lat and Long
  // http://gmaps-samples.googlecode.com/svn/trunk/geocoder/singlegeocode.html
  "GeocoderBoulderAddress" should {
    "take a Boulder location string and return a GeoLocation" in {
      val goog = "2525 Arapahoe Ave, Boulder, CO 80302"
      val gl = Geocoder(goog)
      val expectedLat = 40.015062
      val expectedLong = -105.260474
      //
      System.out.println( "* Expected lat=" + expectedLat + ", was=" + gl.get.latitude )
      System.out.println( "* Expected long=" + expectedLong + ", was=" + gl.get.longitude )
      //
      gl.get must haveClass[GeoLocation]
      gl.get.latitude must beCloseTo( expectedLat, 0.009999 )
      gl.get.longitude must beCloseTo( expectedLong, 0.009999 )
    }
  }


  "GeocoderBoulderWithinMiles" should {
    "make sure two Boulder location are within relative radius" in {

      val goog = "2525 Arapahoe Ave, Boulder, CO 80302"
      val goog2 = "200 Arapahoe Ave, Boulder, CO 80302"
      val glFirst = Geocoder(goog).get
      // the GeoLocation
      val glSecond = Geocoder(goog2).get  // asInstanceOf[GeoLocation] 
      val trueValue = true

      // Try 5 miles
      System.out.println( "* Expected val=" + trueValue + ", was=" + glFirst.withinMiles( 5, glSecond ) )
      glFirst.withinMiles( 5, glSecond ) must beTrue

      // Not within smaller radius
      System.out.println( "* Expected val=" + !trueValue + ", was=" + glFirst.withinMiles( 0, glSecond ) )
      glFirst.withinMiles( 0, glSecond ) must beFalse
      //
      System.out.println( "* Expected val=" + !trueValue + ", was=" + glFirst.withinMiles( 1, glSecond ) )
      glFirst.withinMiles( 1, glSecond ) must beFalse

      // Also compare in reverse, just to be sure
      System.out.println( "* Expected val=" + trueValue + ", was=" + glSecond.withinMiles( 5,  glFirst ) )
      glSecond.withinMiles( 5, glFirst ) must beTrue
    }
  }

}
