package org.snapimpact.dispatch

import scala.xml.NodeSeq
import net.liftweb.http.{S, LiftResponse, InMemoryResponse}
import net.liftweb.common.{Box, Empty, Full}

object api { 
  def volopps(): Box[LiftResponse] = {
    val hi: Array[Byte] = Array('h'.toByte, 'i'.toByte)
    Full(InMemoryResponse(hi, ("Content-Type" -> "text/plain") :: Nil, Nil, 200))
  }
}
