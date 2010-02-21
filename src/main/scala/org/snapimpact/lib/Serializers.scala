package org.snapimpact.lib

import scala.xml.{NodeSeq, Elem, Null, TopScope, Text => XText}

object Serializers {
  val ns = "fp"

  // primitives stolen from lift-json
  val primitives = Map[Class[_], Unit]() ++ (List[Class[_]](
    classOf[String], classOf[Int], classOf[Long], classOf[Double], 
    classOf[Float], classOf[Byte], classOf[BigInt], classOf[Boolean], 
    classOf[Short], classOf[java.lang.Integer], classOf[java.lang.Long], 
    classOf[java.lang.Double], classOf[java.lang.Float], 
    classOf[java.lang.Byte], classOf[java.lang.Boolean], classOf[Number],
    classOf[java.lang.Short], classOf[java.util.Date], classOf[Symbol]).map((_, ())))

  /**
   * serialize anything to allforgood fp rss
   */
  def anyToRss(a: AnyRef): NodeSeq = a match { 
    case p if primitives contains p.getClass => XText(p.toString)
    case s: Seq[AnyRef]  => s.flatMap(x => anyToRss(x) ++ XText(" "))
    case _ => ccToRss(a)
  } 

  /**
   * serialize a case class to allforgood fp rss
   */
  def ccToRss(cc: AnyRef): NodeSeq = { 
    val fields = cc.getClass.getDeclaredFields
    fields.map( f => {
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
