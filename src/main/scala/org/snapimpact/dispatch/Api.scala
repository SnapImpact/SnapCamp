package org.snapimpact.dispatch

import scala.xml.NodeSeq
import net.liftweb.http._
import net.liftweb.common._
import net.liftweb.json._
import net.liftweb.util._
import org.snapimpact.lib.Serializers.anyToRss

sealed trait OutputType
final case object JsonOutputType extends OutputType
final case object RssOutputType extends OutputType
final case object HtmlOutputType extends OutputType

trait RestHelper extends LiftRules.DispatchPF {
  object JsonGet {
    def unapply(r: Req): Option[(List[String], Req)] = None
  }

  object JsonPost {
    def unapply(r: Req): Option[(List[String], (JsonAST.JValue, Req))] = None
  }

  object -> {
    def unapply[A, B](s: (A, B)): Option[(A, B)] = Some(s._1 -> s._2)
  }

  def isDefinedAt(in: Req) = false
  def apply(in: Req): () => Box[LiftResponse] = null

  protected def serve[B](handler: PartialFunction[Req, B])(implicit cvt: B => (() => Box[LiftResponse])): Unit = {}
}

object Api extends RestHelper { 
  implicit def infoToResp(in: Info): () => Box[LiftResponse] = null

  case class Info(name: String)
  serve {
    case "api" :: id :: _ JsonGet _ => Info(id)
  }

  def foo(req: Req): PartialFunction[Req, () => Box[LiftResponse]] = {
    case "api" :: "cat" :: Nil JsonGet req => null

    case "api" :: "cat" :: _ JsonPost json -> req => null
  }
  
  def volopps(r: Req): LiftResponse = {
    for {
      key <- r.param("key") ?~ missingKey ~> 401
      valKey <- validateKey(key) ?~ ("Invalid key. "+ missingKey) ~> 401
    } yield
      r.param("output") match {
	case Full("json") => JsonResponse(sampleJson)
	case Full("rss") => XmlResponse(sampleRss, "application/rss+xml")
	case _ => XmlResponse(sampleHtml)
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

  val sampleRss = <rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:fp="http://www.allforgood.org/" xmlns:georss="http://www.georss.org/georss" xmlns:gml="http://www.opengis.net/gml">{anyToRss(sample)}</rss>
}
