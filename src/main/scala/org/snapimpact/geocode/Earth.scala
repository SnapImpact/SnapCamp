package org.snapimpact.geocode

/**
 * Created by IntelliJ IDEA.
 * User: vpatryshev
 * Date: Apr 11, 2010
 * Time: 10:20:50 AM
 * This object contains information and functions for geographic calculations
 */

object Earth {
  val METERS_IN_MILE = 1609.344

  def metersToMiles(meters: Double) = meters / METERS_IN_MILE

  def milesToMeters(miles: Double) = miles * METERS_IN_MILE

  type Point = (Double, Double)

  /**
   * Calculates distance (in meters) between point a and point b using the FCC formula
   * for Ellipsoidal Earth projected to a plane
   * { @see http ://en.wikipedia.org/wiki/Geographical_distance }
   */
  def distance(a: Point, b: Point) = {
    import Math.{toRadians, cos, sqrt}
    val (aLat: Double, aLon: Double) = a
    val (bLat: Double, bLon: Double) = b
    val dLat = aLat - bLat // in degrees, according to formula
    val dLon = aLon - bLon // in degrees, according to formula
    val midLat = toRadians((aLat + bLat) * .5)
    val k1 = 111132.09 - 566.05 * Math.cos(2 * midLat) + 1.2 * cos(4 * midLat)
    val k2 = 111415.13 * cos(midLat) - 94.55 * cos(3 * midLat) + 0.12 * cos(5 * midLat)
    sqrt((k1 * dLat) * (k1 * dLat) + (k2 * dLon) * (k2 * dLon))
  }

  def distanceInMiles(a: Point, b: Point) = metersToMiles(distance(a, b))
}