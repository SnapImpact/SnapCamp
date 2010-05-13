package org.snapimpact
package snippet

import scala.xml.{NodeSeq, Text}
import net.liftweb.util._
import net.liftweb.http._
import net.liftweb.common._
import org.snapimpact.lib._
import Helpers._
import net.liftweb.json._
import JsonAST._
import net.liftweb.http.testing._

import model._
import geocode._

object ProcessSearch {
  def render(in: NodeSeq): NodeSeq = 
    for {
      q <- S.param("q").map(_.trim).filter(_.length > 0) ?~ "No query"
    } yield {
      val loc: Option[GeoLocation] = 
        for {
          l <- S.param("loc").map(_.trim).filter(_.length > 0)
          geo <- Geocoder(l)
        } yield geo

      val store = PersistenceFactory.store.vend
      store.read(store.search(Full(q), loc = loc)) match {
        case Nil => Text("No results")
        case xs => xs.flatMap{
          case (guid, vo, _) => bind("results", in,
                                  "title" -> vo.title,
                                  "description" -> 
                                  (vo.description getOrElse "N/A"))
        } : NodeSeq
      }
    }

  implicit def bnsToNS(in: Box[NodeSeq]): NodeSeq = in match {
    case Full(i) => i
    case Failure(msg, _, _) => S.error(msg); 
      S.redirectTo(S.referer openOr "/")
    case _ =>
      S.redirectTo(S.referer openOr "/")
  }

}

/*object ProcessSearch extends RequestKit {
  def baseUrl = "http://www.allforgood.org"

  def render(in: NodeSeq): NodeSeq = 
    for {
      apiKey <- Props.get("afg.api.key") ?~ "No API Keys Defined"
      q <- S.param("q") ?~ "Search Term Node Defined"
      longLat = boxPair(S.param("long"), S.param("lat"))
      results <- askFor(q, apiKey, longLat) ?~ "request on AFG failed"
    } yield (for {
        JArray(items) <- results \ "items"
        item <- items
        JString(sponsor) <- item \ "sponsoringOrganizationName"
        JString(description) <- item \ "description"
        node <- bind("results", in,
                     "sponsor" -> sponsor, "description" -> description)
      } yield node) : NodeSeq

  implicit def bnsToNS(in: Box[NodeSeq]): NodeSeq = in match {
    case Full(i) => i
    case Failure(msg, _, _) => S.error(msg); 
      S.redirectTo(S.referer openOr "/")
    case _ =>
      S.redirectTo(S.referer openOr "/")
  }

  def boxPair[A,B](ab: Box[A], bb: Box[B]): Box[(A, B)] =
    for {
      a <- ab
      b <- bb
    } yield (a, b)
	 
  private def askFor(query: String, key: String, longLat: Box[(String,String)]):
  Box[JValue] = {
    val params = ("key", key) :: ("q", query) :: ("output", "json") :: 
    longLat.toList.map{case (long, lat) => ("vol_loc", long+","+lat)}

    import net.liftweb.json._

    get("/api/volopps", params :_*) match {
      case r: HttpResponse if r.code != 200 =>
        Failure("AFG site returned code "+r.code, Empty, Empty)
        
      case r: HttpResponse =>
        for {
          body <- tryo{r.bodyAsString}
          json <- tryo(JsonParser.parse(body))
        } yield json

      case _ => Empty
    }
  }
}
*/
