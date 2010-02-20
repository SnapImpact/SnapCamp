package org.snapimpact.dispatch

import scala.xml.NodeSeq
import net.liftweb.http.{S, LiftResponse, JsonResponse, XmlResponse}
import net.liftweb.common.{Box, Empty, Full}
import net.liftweb.http.js.JE.JsRaw 

object api { 
  def volopps(): Box[LiftResponse] = Full( S.param("key") match { 
    case Full(_) => S.param("output") match {
      case Full("json") => JsonResponse(sampleJson, ("Content-Type" -> "text/javascript") :: Nil, Nil, 200)
      case Full("rss") => XmlResponse(sampleRss, 200, "application/rss+xml", Nil)
      case _ => XmlResponse(sampleHtml, 200, "application/xhtml+xml; charset=utf-8", Nil)
    }
    case _ => XmlResponse(missingKey, 401, "application/xhtml+xml; charset=utf-8", Nil)
  } )

  val missingKey =
    <p>You seem to be missing the API key parameter ('key') in your query.  Please see the <a href="http://www.allforgood.org/docs/api.html">directions for how to get an API key</a>.</p>

  val sampleHtml =
    <p>here's some info on volunteer opportunities</p>

  val sampleJson =
    JsRaw("""{"here": "is some sample json"}""")

  val sampleRss =
    <p>here's some rss with the wrong doctype</p>
}
