package org.snapimpact.view

import scala.xml.NodeSeq
import net.liftweb.http.LiftView

class api extends LiftView { 
  override def dispatch = { 
    case "index" => index _
    case "volopps" => volopps _

  }
  
  def index(): NodeSeq = { 
    <p>here's some info about the api</p>
  }

  def volopps(): NodeSeq = {
    <p>here's some info on volunteer opportunities</p>
  }
}

