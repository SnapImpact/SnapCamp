package org.snapimpact.lib

import _root_.java.util.{Calendar,Date,GregorianCalendar}
import _root_.org.joda.time._

/**
 * Classes related to search.
 *
 */

// Where in the world are the events of San Diego?
case class LatLong(lat: Double = 0.0, long: Double = 0.0) {}

// What do we care/not-care about?
case class RSWhere(
    radius: Double            = 50.0,   // how far to search from latLong
                                        // only events after
    after: GregorianCalendar  = new GregorianCalendar(1970, 1, 1, 00, 00),
                                        // only events before
    before: GregorianCalendar = new GregorianCalendar(2038, 7, 1, 00, 00),
    tags: Seq[String]         = List(), // for this set of tags
    categories: Seq[String]   = List(), // in these categories
    texts: Seq[String]        = List()  // and oh yeah, free form text
    ) {}

/**
 * Classes related to Order preference
 *
 * To date we have output displayed ByTime, by TimeBins and/or ByGeo.
 * TimeBins are predefined but can be ordered as preferred.  There
 * is a bit more flexibility here than really needed but should be
 * useful if new order types are defined.  E.g., ByCategory, ByTag, etc.
 *
 * Example:
 *
 *  val policy = new Policy(latLong, constraints, List( ByGeo, ByTime ) )
 *
 */

sealed trait OrderVal 

sealed trait TimeBin

case object Today extends TimeBin
case object Tomorrow extends TimeBin
case object ThisWeek extends TimeBin
case object ThisMonth extends TimeBin

sealed trait TimeVal extends OrderVal

final case object ByTime extends TimeVal
final case class ByTimeBins(bins: Seq[TimeBin] = List()) extends TimeVal

object Constants {
    val defaultTimeBins: ByTimeBins =
        ByTimeBins( List(Today, Tomorrow, ThisWeek, ThisMonth) )
}

case object ByGeo extends OrderVal          // geography is continuous
                                            // so List(ByGeo, defaultTimeBins)
                                            // wouldn't make much sense.

/**
 * Class used to specify Search/Display policy.
 *
 * A provider should be able to specify a default policy which
 * could be used to inform the user of input they can provide
 * to influence the search.  The user's input (itself representable
 * via a Policy) and the provider's Policy are combined to inform
 * the search engine.
 *
 * When entering parameters you must enter all three but you can
 * enter None for an empty param.  (You can wrap your params in
 * Some() if you care to but extra constructors will handle that
 * for you.)
 *
 */

case class Policy (
    val latLong: Option[LatLong]         = None,
    val rsWhere: Option[RSWhere]         = None,
    val orderPref: Option[Seq[OrderVal]] = None
    ) {

    // Extra constructors.
    def this(a: Option[LatLong], b: Option[RSWhere], c: Seq[OrderVal]) =
        this(a, b, Some(c))
    def this(a: Option[LatLong], b: RSWhere, c: Option[Seq[OrderVal]]) =
        this(a, Some(b), c)
    def this(a: Option[LatLong], b: RSWhere, c: Seq[OrderVal]) =
        this(a, Some(b), Some(c))
    def this(a: LatLong, b: Option[RSWhere], c: Option[Seq[OrderVal]]) =
        this(Some(a), b, c)
    def this(a: LatLong, b: Option[RSWhere], c: Seq[OrderVal]) =
        this(Some(a), b, Some(c))
    def this(a: LatLong, b: RSWhere, c: Option[Seq[OrderVal]]) =
        this(Some(a), Some(b), c)
    def this(a: LatLong, b: RSWhere, c: Seq[OrderVal]) =
        this(Some(a), Some(b), Some(c))

    // Combine policies.  Biz always wins but user can tighten constraints.
    def combine(bizPolicy: Policy, userPolicy: Policy): Policy = {
        def combineLL(bLL: Option[LatLong], uLL: Option[LatLong]): Option[LatLong] = bLL match {
                case None => uLL
                case _ => bLL
            }

        def combineWhere(bC: Option[RSWhere], uC: Option[RSWhere]): Option[RSWhere] = bC match {
                case None => uC
                case Some(RSWhere(br, ba, bb, bt, bc, btxt)) => uC match {
                    case None => bC
                    case Some(RSWhere(ur, ua, ub, ut, uc, utxt)) =>
                        val w = new RSWhere(
                            if (br < ur) { br } else { ur },
                            if (ba.getMillis > ua.getMillis) { ba } else { ua },
                            if (bb.getMillis < ua.getMillis) { bb } else { ub },
                            bt ++ ut,
                            bc ++ uc,
                            btxt ++ utxt
                        )
                        Some(w)
                }
            }

        def combineOrder(bOP: Option[Seq[OrderVal]],
            uOP: Option[Seq[OrderVal]]): Option[Seq[OrderVal]] = bOP match {
                case None => uOP
                case _ => bOP
            }

        new Policy(
            combineLL(bizPolicy.latLong, userPolicy.latLong),
            combineWhere(bizPolicy.rsWhere, userPolicy.rsWhere),
            combineOrder(bizPolicy.orderPref, userPolicy.orderPref)
        )
    }
}

// vim: set ts=8 sw=4 sts=4 sta et:
