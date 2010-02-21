package org.snapimpact.lib

import scala.xml.{NodeSeq, Elem, Null, TopScope, Text}

object Serializers {
  /**
   * serialize a case class to allforgood fp rss
   */
  def ccToRss(cc: AnyRef): NodeSeq = { 
    val ns = "fp"
    val fields = cc.getClass.getDeclaredFields
    fields.map( f => { 
      f.setAccessible(true)
      Elem(ns, f.getName, Null, TopScope, Text(f.get(cc).toString))
    })
  }
  
}
