package org.snapimpact.lib

import org.scala_tools.time.Imports._

/**
 * Classes related to search parameters.
 */

abstract class SearchQuery {}

abstract class GeoQuery extends SearchQuery() {}

case class State(id: String) extends GeoQuery {
    def isValid: Boolean = Constants.UspsStateAbbr.contains(id)
}
case class City(name: String, state: State) extends GeoQuery {}
case class County(name: String, state: State) extends GeoQuery {}
case class Zip(code: String) extends GeoQuery {}
case class LatLong(lat: Double, long: Double) extends GeoQuery {}
case object Discover extends GeoQuery {}

abstract class TemporalQuery extends SearchQuery() {}

case class TimeWindow(after: DateTime, before: DateTime)
    extends TemporalQuery {}

abstract class CategoryQuery extends SearchQuery() {}

case class Category(name: String) extends CategoryQuery() {}

abstract class TagQuery extends SearchQuery() {}

case class Tag(name: String) extends TagQuery() {}

abstract class TextQuery extends SearchQuery() {}

case class Text(str: String) extends SearchQuery() {}

/**
 * Classes related to Order preference
 */

class OrderPref(val order: Seq[OrderVal]) {}

abstract class OrderVal {}

abstract class TimeBucket {}

case object Today extends TimeBucket() {}
case object Tomorrow extends TimeBucket() {}
case object ThisWeek extends TimeBucket() {}
case object ThisMonth extends TimeBucket() {}

abstract class TimeVal extends OrderVal() {}

case object ByTime extends TimeVal() {}
case class ByTimeBuckets(buckets: Seq[TimeBucket]) extends TimeVal() {}

object Constants {
    val UspsStateAbbr: Map[String,String] = Map(
        "AL" -> "ALABAMA",
        "AK" -> "ALASKA",
        "AS" -> "AMERICAN SAMOA",
        "AZ" -> "ARIZONA",
        "AR" -> "ARKANSAS",
        "CA" -> "CALIFORNIA",
        "CO" -> "COLORADO",
        "CT" -> "CONNECTICUT",
        "DE" -> "DELAWARE",
        "DC" -> "DISTRICT OF COLUMBIA",
        "FM" -> "FEDERATED STATES OF MICRONESIA",
        "FL" -> "FLORIDA",
        "GA" -> "GEORGIA",
        "GU" -> "GUAM",
        "HI" -> "HAWAII",
        "ID" -> "IDAHO",
        "IL" -> "ILLINOIS",
        "IN" -> "INDIANA",
        "IA" -> "IOWA",
        "KS" -> "KANSAS",
        "KY" -> "KENTUCKY",
        "LA" -> "LOUISIANA",
        "ME" -> "MAINE",
        "MH" -> "MARSHALL ISLANDS",
        "MD" -> "MARYLAND",
        "MA" -> "MASSACHUSETTS",
        "MI" -> "MICHIGAN",
        "MN" -> "MINNESOTA",
        "MS" -> "MISSISSIPPI",
        "MO" -> "MISSOURI",
        "MT" -> "MONTANA",
        "NE" -> "NEBRASKA",
        "NV" -> "NEVADA",
        "NH" -> "NEW HAMPSHIRE",
        "NJ" -> "NEW JERSEY",
        "NM" -> "NEW MEXICO",
        "NY" -> "NEW YORK",
        "NC" -> "NORTH CAROLINA",
        "ND" -> "NORTH DAKOTA",
        "MP" -> "NORTHERN MARIANA ISLANDS",
        "OH" -> "OHIO",
        "OK" -> "OKLAHOMA",
        "OR" -> "OREGON",
        "PW" -> "PALAU",
        "PA" -> "PENNSYLVANIA",
        "PR" -> "PUERTO RICO",
        "RI" -> "RHODE ISLAND",
        "SC" -> "SOUTH CAROLINA",
        "SD" -> "SOUTH DAKOTA",
        "TN" -> "TENNESSEE",
        "TX" -> "TEXAS",
        "UT" -> "UTAH",
        "VT" -> "VERMONT",
        "VI" -> "VIRGIN ISLANDS",
        "VA" -> "VIRGINIA",
        "WA" -> "WASHINGTON",
        "WV" -> "WEST VIRGINIA",
        "WI" -> "WISCONSIN",
        "WY" -> "WYOMING"
    )

    val ByDefaultTimeBuckets: ByTimeBuckets =
        ByTimeBuckets( List(Today, Tomorrow, ThisWeek, ThisMonth) )

    val DefaultPolicy: Policy =
        new Policy(List(Discover), List(ByDefaultTimeBuckets, ByGeo))
}

case object ByGeo extends OrderVal() {}

/**
 * Class used to specify business policy
 */

case class Policy(
    search: Seq[SearchQuery], order: Seq[OrderVal], showDesc: Boolean) {

    def this(search: Seq[SearchQuery], order: Seq[OrderVal]) =
        this(search, order, true)
}

// vim: set ts=4 sw=4 et:
