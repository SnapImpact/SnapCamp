package org.snapimpact.geocode

import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import Helpers._
import _root_.net.liftweb.json._
import JsonParser._
import JsonAST._

import org.snapimpact.model.GeoLocation

/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: Apr 20, 2010
 * Time: 10:10:55 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * @param in String Takes the String and returns an Option[GeoLocation]
 */
object Geocoder {
  def apply(in: String): Option[GeoLocation] = {
    val encoder = new Geocoder
    encoder.getGeoLocation(in)
  }
}
class Geocoder extends HttpClient {
  val GOOGLE_GEO_URL = "http://maps.google.com/maps/api/geocode/json?"
  val GOOGLE_MAPS_API_KEY = "ABQIAAAA8HXiU_-E98nF20YvZ37zAxQ3KXTeRMsCydUtpdwIbkIA5o2l6BSDT71ZHbxEWhRhZfsByNDyDiEtKA"

  private def getGeoLocation(in: String): Option[GeoLocation] = {
    val encodedString = urlEncode(in.trim)

    val url = GOOGLE_GEO_URL + "address=" + encodedString + "&sensor=false"

    getStringFromUrl(url) match {
      case Some(response) => {
        parseResponse(response) match {
          case Some(loc) => Some(new GeoLocation(loc.lng, loc.lat, true))
          case _ => None
        }
      }
      case _ => None
    }
  }

  private def parseResponse(in: String): Option[GoogGeoLoc] = {
    val json = parse(in)
    implicit val formats = DefaultFormats

    val ret = try {json.extract[GoogGeoRet]} catch {
      case e: Exception => {
        e.printStackTrace
        new GoogGeoRet("", Nil)
      }
    }

    ret.status match {
      case "OK" => {
        val first = ret.results.head
        val loc = first.geometry.location

        Some(loc)
      }
      case _ => None
    }
  }
}

case class GoogGeoRet(
        status: String,
        results: List[GoogGeoResults]
        )
case class GoogGeoResults(
        types: List[String],
        formatted_address: String,
        address_components: List[GoogGeoAddrComp],
        geometry: GoogGeoGeom
        )
case class GoogGeoAddrComp(
        long_name: String,
        short_name: String,
        types: List[String]
        )
case class GoogGeoGeom(
        location: GoogGeoLoc,
        location_type: String,
        viewport: GoogGeoViewport,
        bounds: Option[GoogGeoViewport]
        )
case class GoogGeoLoc(
        lat: Double,
        lng: Double
        )
case class GoogGeoViewport(
        southwest: GoogGeoLoc,
        northeast: GoogGeoLoc
        )

/*                                     ""
Example Return:

{
  "status": "OK",
  "results": [ {
    "types": [ "street_address" ],
    "formatted_address": "1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA",
    "address_components": [ {
      "long_name": "1600",
      "short_name": "1600",
      "types": [ "street_number" ]
    }, {
      "long_name": "Amphitheatre Pkwy",
      "short_name": "Amphitheatre Pkwy",
      "types": [ "route" ]
    }, {
      "long_name": "Mountain View",
      "short_name": "Mountain View",
      "types": [ "locality", "political" ]
    }, {
      "long_name": "California",
      "short_name": "CA",
      "types": [ "administrative_area_level_1", "political" ]
    }, {
      "long_name": "United States",
      "short_name": "US",
      "types": [ "country", "political" ]
    }, {
      "long_name": "94043",
      "short_name": "94043",
      "types": [ "postal_code" ]
    } ],
    "geometry": {
      "location": {
        "lat": 37.4219720,
        "lng": -122.0841430
      },
      "location_type": "ROOFTOP",
      "viewport": {
        "southwest": {
          "lat": 37.4188244,
          "lng": -122.0872906
        },
        "northeast": {
          "lat": 37.4251196,
          "lng": -122.0809954
        }
      }
    }
  } ]
}

*/