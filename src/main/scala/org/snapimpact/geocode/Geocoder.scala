package org.snapimpact
package geocode

import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import Helpers._
import _root_.net.liftweb.json._
import JsonParser._
import JsonAST._
import net.liftweb.http.testing._

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
  private val cache = new LRUMap[String, Box[GeoLocation]](2500)

  def apply(in: String): Box[GeoLocation] = {
    Empty
    /*
    synchronized{cache.get(in)} openOr {
      val encoder = new Geocoder
      val ret = encoder.getGeoLocation(in)
      synchronized(cache(in) = ret)
      ret
    }
    */
  }
}
class Geocoder extends RequestKit {
  def baseUrl = "http://maps.google.com"

  protected def getString(url: String, params: (String, Any)*): Box[String] =
    for {
      resp <- get(url, params :_*).filter(_.code == 200) ?~ "Didn't get a 200"
      answer <- tryo(new String(resp.body, "UTF-8"))
    } yield answer

  private def getGeoLocation(in: String): Box[GeoLocation] = {
    val encodedString = urlEncode(in.trim)

    // val url = GOOGLE_GEO_URL + "address=" + encodedString + "&sensor=false"

    for {
      response <- getString("/maps/api/geocode/json", "address" -> in, "sensor" -> false)
      loc <- parseResponse(response)
    } yield new GeoLocation(loc.lng, loc.lat, true)
  }

  private def parseResponse(in: String): Box[GoogGeoLoc] = {
    implicit val formats = DefaultFormats

    for {
      json <- tryo(parse(in))
      geoRet <- tryo(json.extract[GoogGeoRet]).filter(_.status == "OK")
      first <- geoRet.results.headOption
    } yield first.geometry.location
  }
}

case class GoogGeoRet(status: String, results: List[GoogGeoResults])
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
