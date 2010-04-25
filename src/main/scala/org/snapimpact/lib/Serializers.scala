package org.snapimpact.lib

import scala.xml.{NodeSeq, Elem, Null, TopScope, Text => XText}

object Serializers {
  val ns = "fp"

  // primitives stolen from lift-json
  val primitives = Set[Class[_]](
    classOf[String], classOf[Int], classOf[Long], classOf[Double], 
    classOf[Float], classOf[Byte], classOf[BigInt], classOf[Boolean], 
    classOf[Short], classOf[java.lang.Integer], classOf[java.lang.Long], 
    classOf[java.lang.Double], classOf[java.lang.Float], 
    classOf[java.lang.Byte], classOf[java.lang.Boolean], classOf[Number],
    classOf[java.lang.Short], classOf[java.util.Date], classOf[Symbol])

  /**
   * serialize anything to allforgood fp rss
   */
  def anyToRss(a: Any): NodeSeq = a match {
    case p: AnyRef if primitives contains p.getClass => XText(p.toString)
    case s: Seq[_]  => s.flatMap(x => anyToRss(x) ++ XText(" "))
    case a: AnyRef => ccToRss(a)
    case _ => NodeSeq.Empty
  } 

  /**
   * serialize a case class to allforgood fp rss
   */
  def ccToRss(cc: AnyRef): NodeSeq = {
    val fields = cc.getClass.getDeclaredFields
    fields.toList.map( f => {
      val fname = f.getName
      //hack to avoid recursing indefinitely on enums
      if (fname == "MODULE$") {
        XText("")  
      } else { 
        f.setAccessible(true)
        Elem(ns, fname, Null, TopScope, anyToRss(f.get(cc)): _*)
      } 
    })
  }
  
}
