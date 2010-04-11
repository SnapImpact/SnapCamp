package org.snapimpact.geocode

import org.specs.Specification

import _root_.org.specs.runner._

class EarthTest extends Runner(new EarthSpec) with JUnit with Console

import Earth._

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

  // just checking if it tests anything
//  "God" should {
//    "love you " in {
//      true must_== false
//    }
//  }
}
