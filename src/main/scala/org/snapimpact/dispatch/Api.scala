package org.snapimpact
package dispatch

import scala.xml.NodeSeq
import net.liftweb.http._
import net.liftweb.common._
import net.liftweb.json._
import net.liftweb.util._
import org.snapimpact.lib.Serializers.anyToRss
import model._
import geocode._

sealed trait OutputType
final case object JsonOutputType extends OutputType
final case object RssOutputType extends OutputType
final case object HtmlOutputType extends OutputType

object Api {
  def volopps(r: Req): LiftResponse = {
    try {
    for{
      key <- r.param("key") ?~ missingKey ~> 401
      valKey <- validateKey(key) ?~ ("Invalid key. " + missingKey) ~> 401
    } yield {
      val loc: Option[GeoLocation] = 
        for {
          l <- r.param("vol_loc").map(_.trim).filter(_.length > 0)
          geo <- Geocoder(l)
        } yield geo

      val start = r.param("start").flatMap(Helpers.asInt).filter(_ > 0) openOr 1

      val num = r.param("num").flatMap(Helpers.asInt).filter(_ > 0) openOr 10

      val radius = r.param("vol_dist").flatMap(Helpers.asInt).filter(_ > 0).
      openOr(50)

      val store = PersistenceFactory.store.vend
      val res = store.read(store.search(query = r.param("q").map(_.trim).
                                        filter(_.length > 0),
                                        loc = loc,
                                        start = start - 1,
                                        num = num,
                                        radius = radius
                                      ))

      r.param("output") match {
        case Full("json") =>
          JsonResponse(Extraction.decompose(RetV1("Sat, 01 May 2010 16:51:10 +0000",
                                                1.0,
                                                "English",
                                                "http://dpp.im",
                                                "All for Good search results",
                                                res.flatMap(a => MapToV1(a._2)).toList)))

        case Full("rss") => XmlResponse(sampleRss, "application/rss+xml")
        case _ => XmlResponse(sampleHtml)
      }
    }
    } catch {
      case e => e.printStackTrace ; System.exit(0); throw e
    }
  }

  /*
   private class BoxString(in: Box[String]) {
   def asInt = in.flatMap(Helpers.asInt)
   }
   private implicit def bsify(in: Box[String]): BoxString = new BoxString(in)


   def find(query: Option[String] = r.param("q"),
   start: Int = r.param("start").asInt openOr 1,
   num: Int = r.param("num").asInt openOr 10,
   output: OutputType = r.param("output").map(_.toLowerCase) match {
   case Full("rss") => RssOutputType
   case Full("json") => JsonOutputType
   case _ => HtmlOutputType
   }): Unit = {}

   val params = List("timeperiod" -> List("today", "this_month", "this_weekend", "this_week"),
   "vol_startdate", "vol_enddate", "vol_distance", "vol_loc")
   */


  private implicit def respToBox(in: Box[LiftResponse]): LiftResponse = {
    def build(msg: String, code: Int) = {
      InMemoryResponse(msg.getBytes("UTF-8"), List("Content-Type" -> "text/plain"), Nil, code)
    }

    in match {
      case Full(r) => r
      case ParamFailure(msg, _, _, code: Int) => build(msg, code)
      case Failure(msg, _, _) => build(msg, 404)
      case _ => build("Not Found", 404)
    }
  }

  def validateKey(key: String): Box[String] = Full(key)

  val missingKey =
  """You seem to be missing the API key parameter ('key') in your query.
Please see the for how to get an API key at
http://www.allforgood.org/docs/api.html for directions."""

  case class Sample(categories: List[String], quality_score: Double, pageviews: Int)
  val sample = Sample(List("category1", "category2"), .5, 2312)

  implicit val formats = Serialization.formats(NoTypeHints)
  val sampleJson = Extraction.decompose(sample) // Serialization.write(sample) 

  val sampleHtml =
  <p>here's some info on volunteer opportunities</p>

  val sampleRss = <rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:fp="http://www.allforgood.org/" xmlns:georss="http://www.georss.org/georss" xmlns:gml="http://www.opengis.net/gml">
    {anyToRss(sample)}
  </rss>
}
