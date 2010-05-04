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
 *
 * This method causes uploadedFiles to be populated:
 *
 *  curl -F "file=@sampleData0.1.r1254.xml" http://localhost:8080/api/upload
 *  caused the uploadedFiles to populate
 * (HTTP)  This  lets curl emulate a filled in form in which a user has pressed the submit
 *  button. This causes  curl  to  POST  data using the Content-Type multipart/form-data
 *  according to RFC1867. This enables uploading of binary files etc. To force  the
 * 'content' part to be a file, prefix the file name with an @ sign. To just get the content
 *  part from a file, prefix the file name with the  letter  <.  The  difference
 *  between @ and < is then that @ makes a file get attached in the post as a  file
 *  upload,  while the < makes a text field and just get the contents for that text
 *  field from a file.
 *
 * This method causes the body field to be populated:
 *  cat sampleData0.1.r1254.xml | curl -T - http://localhost:8080/api/upload
 *
 * Also, this tool provides good way to test PUT/POST but not multipart/form-data
 * http://code.google.com/p/rest-client/
 */

object FeedUpload {
  // the request-local variable that hold the file parameter
  private object theUpload extends RequestVar[Box[FileParamHolder]](Empty)

  def upload(r:Req): Box[LiftResponse] = {
    var s = if (r.body.isEmpty) {"Empty"} else {
      val subject = XML.load(new ByteArrayInputStream(r.body.open_!))
      val item = FootprintFeed.fromXML(subject)
      // println("FeedUpload.upload: "+item.toString)
      r.body.get.size.toString
    }
//println("FeedUpload.upload: body: "+s);
    s = if (r.uploadedFiles.isEmpty) {"Empty"} else {r.uploadedFiles.size.toString}
//println("FeedUpload.upload: uploadedFiles: "+s);
    // This will only be set if mime-type is text/xml
    // Regardless, body will still be populated
    s = if (r.xml_?) {"xml"} else {"not xml"}
//println("FeedUpload.upload: the xml is: "+s);
    /* list [FileParamHolder] */
    r.uploadedFiles.foreach(v => {
      val subject = XML.load(new ByteArrayInputStream(v.file))
      val item = FootprintFeed.fromXML(subject)
      //println("FeedUpload.upload: "+item.toString)
    })
//    r.uploadedFiles /* list [FileParamHolder] */
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
  
