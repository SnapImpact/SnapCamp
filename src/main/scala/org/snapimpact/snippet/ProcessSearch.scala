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

object ProcessSearch {
  def render(in: NodeSeq): NodeSeq = 
    for {
      apiKey <- Props.get("afg.api.key") ?~ "No API Keys Defined"
      q <- S.param("q") ?~ "Search Term Node Defined"
      longLat = S.param("long").
      flatMap(long => S.param("lat").map(lat => (long, lat)))
      results <- askFor(q, longLat) ?~ "request failed"
      // items <- results \ "items"
    } yield <b>Ignore Me</b>

  implicit def bnsToNS(in: Box[NodeSeq]): NodeSeq = in match {
    case Full(i) => i
    case Failure(msg, _, _) => S.error(msg); 
    S.redirectTo(S.referer openOr "/")
    case _ =>
    S.redirectTo(S.referer openOr "/")
  }
	 
  private def askFor(query: String, longLat: Box[(String,String)]):
  Box[JValue] = Empty
}

