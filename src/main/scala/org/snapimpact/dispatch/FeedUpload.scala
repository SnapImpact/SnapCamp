package org.snapimpact
package dispatch

import net.liftweb.common._
import org.snapimpact.etl.model.dto._
import model._
import net.liftweb._
import http._
import util.Helpers._


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
  def upload(r:Req): LiftResponse = 
    for {
      key <- r.param("key") ?~ Api.missingKey ~> 401
      valKey <- Api.validateKey(key) ?~ ("Invalid key. " + Api.missingKey) ~> 401
      xml <- r.xml ?~ "You didn't supply XML"
      info <- tryo(FootprintFeed.fromXML(xml)) ?~ "Couldn't ETL the XML"
    } yield {
      val store = PersistenceFactory.store.vend
      info.opportunities.opps.foreach {store.add _}
      OkResponse()
    }

    private implicit def respToBox(in: Box[LiftResponse]): LiftResponse = {
    def build(msg: String, code: Int) = {
      InMemoryResponse(msg.getBytes("UTF-8"), List("Content-Type" -> "text/plain"), Nil, code)
    }

    in match {
      case Full(r) => r
      case ParamFailure(msg, _, _, code: Int) => build(msg, code)
      case Failure(msg, _, _) => build(msg, 404)
      case _ => build("Not Found", 404)
    }
  }

}
  
