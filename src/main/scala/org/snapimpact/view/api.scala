package org.snapimpact.view

import scala.xml.NodeSeq
import net.liftweb.http.{S, LiftView}
import net.liftweb.common.{Box, Empty, Full}

class api extends LiftView { 
  override def dispatch = { 
    case "index" => index _
    case "volopps" => volopps _

  }
  
  def index(): NodeSeq = { 
    <p>here's some info about the api</p>
  }

  def volopps(): NodeSeq = S.param("key") match {
    case Full(x) =>
        <p>here's some info on volunteer opportunities</p>
    case _ =>
      <p>You seem to be missing the API key parameter ('key') in your query.  Please see the <a href="http://www.allforgood.org/docs/api.html">directions for how to get an API key</a>.</p>
  }
}

