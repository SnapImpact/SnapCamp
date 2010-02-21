package org.snapimpact.geocode

import org.snapimpact.model.Location

/**
 * Created by IntelliJ IDEA.
 * User: zkim
 * Date: Feb 21, 2010
 * Time: 1:24:15 PM
 * To change this template use File | Settings | File Templates.
 */

trait Geocoder {
    def latLongToAddress(loc: Location)
    def addressToLatLong(loc: Location)
}