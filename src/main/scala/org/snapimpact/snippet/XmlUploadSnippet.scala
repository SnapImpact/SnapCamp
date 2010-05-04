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
  // the request-local variable that hold the file parameter
  // private object theUpload extends RequestVar[Box[FileParamHolder]](Empty)

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
    /*
    
    if (S.get_?) bind("ul", chooseTemplate("choose", "get", xhtml),
                    "file_upload" -> fileUpload(ul => theUpload(Full(ul))))
    else bind("ul", chooseTemplate("choose", "post", xhtml),
            "file_name" -> theUpload.is.map(v => Text(v.fileName)),
            "mime_type" -> theUpload.is.map(v => Box.legacyNullTest(v.mimeType).map(Text).openOr(Text("No mime type supplied"))), // Text(v.mimeType)),
            "length" -> theUpload.is.map(v => Text(v.file.length.toString)),
            "md5" -> theUpload.is.map(v => Text(hexEncode(md5(v.file))))
  );
  */

  /*

  def lang(xhtml: NodeSeq): NodeSeq =
    bind("showLoc", xhtml,
       "lang" -> locale.getDisplayLanguage(locale),
       "select" -> selectObj(locales.map(lo => (lo, lo.getDisplayName)),
                             Full(definedLocale.is), setLocale))

  private def locales =
    Locale.getAvailableLocales.toList.sortWith(_.getDisplayName < _.getDisplayName)

  private def setLocale(loc: Locale) = definedLocale.set(loc)
  */
}

// object definedLocale extends SessionVar(S.locale)
