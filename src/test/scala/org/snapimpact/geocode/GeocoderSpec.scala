package org.snapimpact.geocode

import org.slf4j.LoggerFactory

import org.specs._
import org.specs.runner._
import org.snapimpact.model.GeoLocation
import net.liftweb.common._

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
      val fubar = ""

      val gl = Geocoder(goog)
      val glFubar = Geocoder(fubar)

      gl.get must haveClass[GeoLocation]
      (gl.get.latitude * 100d).toLong must beEqual((37.422782 * 100d).toLong)
      (gl.get.longitude * 100d).toLong must beEqual((-122.085099 * 100d).toLong)
      glFubar must_== Empty
    }
  }

}
