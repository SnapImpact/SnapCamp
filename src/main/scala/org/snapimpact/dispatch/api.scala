package org.snapimpact.dispatch

import scala.xml.NodeSeq
import net.liftweb.http._
import net.liftweb.common.{Box, Empty, Full}
import net.liftweb.json._
import org.snapimpact.lib.Serializers.anyToRss



object Api { 
  def volopps() = 
    for {
      key <- S.param("key") ?~ missingKey ~> 401
      valKey <- validateKey(key) ?~ ("Invalid key. "+ missingKey) ~> 401
    } yield
      S.param("output") match {
	case Full("json") => JsonResponse(sampleJson)
	case Full("rss") => XmlResponse(sampleRss, "application/rss+xml")
	case _ => XmlResponse(sampleHtml, "application/xhtml+xml; charset=utf-8")
    }

  def validateKey(key: String): Box[String] = Full(key)

  val missingKey =
 """You seem to be missing the API key parameter ('key') in your query.
Please see the for how to get an API key at 
http://www.allforgood.org/docs/api.html directions."""

  case class Sample(categories: List[String], quality_score: Double, pageviews: Int)
  val sample = Sample(List("category1", "category2"), .5, 2312)

  implicit val formats = Serialization.formats(NoTypeHints)
  val sampleJson = Extraction.decompose(sample) // Serialization.write(sample) 

  val sampleHtml =
    <p>here's some info on volunteer opportunities</p>

  val sampleRss = <rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:fp="http://www.allforgood.org/" xmlns:georss="http://www.georss.org/georss" xmlns:gml="http://www.opengis.net/gml">{anyToRss(sample)}</rss>
}
