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
        val (lat,lng) = parse(response)
        Some(new GeoLocation(lng, lat, true))
      }
      case _ => None
    }
  }

  private def parse(in: String): (Double, Double) = {
    val json = parse(in)

    val status = (json \ "status").extract[JString]

/*    status match {
      case "OK" => {
        val location = (json \ "result")(0) \\ "location" \\ "*"

        Log.info("loc: " + location)
        (1.0,1.0)
      }
      case _ => (1.0,1.0)
    }*/
    (1.0,1.0)
  }
}

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