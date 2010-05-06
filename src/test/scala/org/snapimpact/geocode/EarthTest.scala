package org.snapimpact.geocode

import org.specs.Specification
import _root_.org.specs.runner._
import Earth._


class EarthTest extends Runner(new EarthSpec) with JUnit with Console

class EarthSpec extends Specification {
  "Distance between" should {
    "a point and itself be 0" in {
      val p = (40.0, -120.0)
      distance(p, p) must_== 0
    }
  }

  "Distance" should {
    "satisfy triangle inequality" in {
      val p1 = (40.0, -120.0)
      val p2 = (36.0, -115.0)
      val p3 = (33.0, -100.0)
      ((distance(p1, p2) + distance(p2, p3)) >= distance(p1, p3)) mustEqual true
    }
  }
  val lax = (33.94, -118.40)
  val bna = (36.13, -86.67)

  "Distance between LAX and BNA" should {
    ("be about about 2886 km but is " + distance(lax, bna)) in {
      val d = distance(lax, bna)
      (d > 2885000) mustEqual true
      (d < 2910000) mustEqual true
    }
  }

  "Earth" should {
    "calculate meters to miles and vice versa " in {

      val miles = 1.0
      val metersInMile = 1609.344

      // meters to miles
      //System.out.println( "* Expected val=" + miles + ", was=" + Earth.metersToMiles( metersInMile ) )
      Earth.metersToMiles( metersInMile ) mustEqual miles
      // miles to meters
      //System.out.println( "* Expected val=" + metersInMile + ", was=" + Earth.milesToMeters( miles ) )
      Earth.milesToMeters( miles ) mustEqual metersInMile
    }
  }
  
  "Earth" should {
    "calculate distance between points" in {

      val distanceInMeters = 2561883.1035829214
      val distanceInMiles = Earth.metersToMiles( distanceInMeters )
      val pnt1 = (40.0, -120.0)
      val pnt2 = (40.0, -150.0)

      // distance in meters
        //System.out.println( "* Expected val=" + distanceInMeters + ", was=" + Earth.distance( pnt1, pnt2 ) )
      Earth.distance( pnt1, pnt2 ) must beCloseTo( distanceInMeters, 0.00000009 );
      // distance in miles
      //System.out.println( "* Expected val=" + distanceInMiles + ", was=" + Earth.distanceInMiles( pnt1, pnt2 ) )
      Earth.distanceInMiles( pnt1, pnt2 ) must beCloseTo( distanceInMiles, 0.00000009 );

    }
  }

}
