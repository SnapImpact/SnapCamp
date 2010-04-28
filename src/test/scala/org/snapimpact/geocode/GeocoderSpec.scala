package org.snapimpact.geocode

import org.slf4j.LoggerFactory

import org.specs._
import org.specs.runner._
import org.snapimpact.model.GeoLocation


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
      gl.get.latitude must beEqual( expectedLat )
      gl.get.longitude must beEqual( expectedLong )
    }
  }

  "GeocoderEmpty" should {
    "take empty String and return empty / None object" in {
      val fubar = ""
      val glFubar = Geocoder(fubar)
      //
      System.out.println( "* Expected val=None, was=" + glFubar )
      //
      glFubar must beNone
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
      System.out.println( "* Expected long=-" + expectedLong + ", was=" + gl.get.longitude )
      //
      gl.get must haveClass[GeoLocation]
      gl.get.latitude must beEqual( expectedLat )
      gl.get.longitude must beEqual( expectedLong )
    }
  }


}