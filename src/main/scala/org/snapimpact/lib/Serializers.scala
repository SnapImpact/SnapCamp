package org.snapimpact.lib

import scala.xml.{NodeSeq, Elem, Null, TopScope, Text}

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
    case p if primitives contains p.getClass => Text(p.toString)
    case s: Seq[AnyRef]  => s.flatMap(x => anyToRss(x) ++ Text(" "))
    case _ => ccToRss(a)
  } 

  /**
   * serialize a case class to allforgood fp rss
   */
  def ccToRss(cc: AnyRef): NodeSeq = { 
    val fields = cc.getClass.getDeclaredFields
    fields.map( f => { 
      f.setAccessible(true)
      Elem(ns, f.getName, Null, TopScope, anyToRss(f.get(cc)): _*)
    })
  }
  
}
