package org.snapimpact.dispatch
import _root_.scala.xml.{NodeSeq, Text, Group, XML}
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.S
import _root_.net.liftweb.mapper._
import _root_.net.liftweb.http.S._
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.util._
import _root_.java.util.Locale
import net.liftweb.common.{Box, Empty, Full}
import org.snapimpact.etl.model.dto.FootprintFeed
import java.io.ByteArrayInputStream

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Feb 23, 2010
 * Time: 8:49:19 PM
 * To change this template use File | Settings | File Templates.
 *
 * Almost worked: wget --post-file sampleData0.1.r1254.xml localhost:8080/api/upload
 * 
 */

object FeedUpload {
  // the request-local variable that hold the file parameter
  private object theUpload extends RequestVar[Box[FileParamHolder]](Empty)

  def upload(r:Req): Box[LiftResponse] = {
    /* list [FileParamHolder] */
    r.uploadedFiles.map(v => {
      val subject = XML.load(new ByteArrayInputStream(v.file))
      val item = FootprintFeed.fromXML(subject)
      println("FeedUpload.upload: "+item.toString)
      v.fileName
    })

//    r.body/* Box[Array[Byte]] */
//    r.xml/* Box[Elem] */
//    "file_name" -> theUpload.is.map(v => Text(v.fileName)),
//    "mime_type" -> theUpload.is.map(v => Box.legacyNullTest(v.mimeType).map(Text).openOr(Text("No mime type supplied"))), // Text(v.mimeType)),
//    "length" -> theUpload.is.map(v => Text(v.file.length.toString)),
//    "md5" -> theUpload.is.map(v => Text(hexEncode(md5(v.file))))

    // so far this isn't getting set
  //    theUpload.is.map(v => {
//      val subject = XML.load(new ByteArrayInputStream(v.file))
//      val item = FootprintFeed.fromXML(subject)
//      println("FeedUpload.upload: "+item.toString)
//      v.fileName
//    })

    Full(OkResponse())
  }
}
  
