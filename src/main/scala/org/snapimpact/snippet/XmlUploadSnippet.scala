package org.snapimpact
package snippet

import _root_.net.liftweb._
import http._
import mapper._
import S._
import SHtml._

import common._
import util._
import Helpers._

import _root_.scala.xml.{NodeSeq, Text, Group}
// import _root_.java.util.Locale

import sitemap._
import Loc._

import scala.xml._

import etl.model.dto._
import model._


/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Mar 16, 2010
 * Time: 7:36:23 PM
 * To change this template use File | Settings | File Templates.
 */

object XmlUploadSnippet {
  def menuParams: LocParam[Unit] = Snippet("upload", upload)

  private def gotFile(file: FileParamHolder) {
    (for {
      mime <- Full(file.mimeType).filter(_ startsWith "text/xml") ?~ "It's not XML"
      xml <- tryo(XML.load(file.fileStream)) ?~ "Unable to parse the XML"
      info <- tryo(FootprintFeed.fromXML(xml)) ?~ "Couldn't ETL the XML"
    } yield {
      val store = PersistenceFactory.store.vend
      info.opportunities.opps.foreach {store.add _}
      S.notice("Thanks for the file")
      S.redirectTo("/")
    }) match {
      case Failure(msg, _, _) => S.error(msg)
      case _ => S.error("Unable to upload data")
    }
  }


  /**
   * Bind the appropriate XHTML to the form
   */
  def upload(xhtml: NodeSeq): NodeSeq =
    bind("upload", xhtml, "file" -> fileUpload(gotFile _))
}

