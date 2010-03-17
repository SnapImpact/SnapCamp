package org.snapimpact.dispatch
//import _root_.net.liftweb.example.model._
import _root_.scala.xml.{NodeSeq, Text, Group}
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.S
import _root_.net.liftweb.mapper._
import _root_.net.liftweb.http.S._
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.util._
import _root_.java.util.Locale
import net.liftweb.common.{Box, Empty, Full}

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Feb 23, 2010
 * Time: 8:49:19 PM
 * To change this template use File | Settings | File Templates.
 */

object FeedUpload {
  // the request-local variable that hold the file parameter
  private object theUpload extends RequestVar[Box[FileParamHolder]](Empty)

  def upload(r:Req): Box[LiftResponse] = {
//    r.uploadedFiles /* list [FileParamHolder] */
//    r.body/* Box[Array[Byte]] */
//    r.xml/* Box[Elem] */
    Empty
  }
}
  