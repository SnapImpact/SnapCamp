package org.snapimpact
package lib

/*
 * All for Good 2.0
 * (c) 2010 David Pollak and others
 * Distributed under an Apache 2.0 license
 */

import net.liftweb.sitemap._
import Loc._
import net.liftweb.http._

import snippet._

import scala.xml.NodeSeq

object MenuData extends DispatchSnippet {
  def home = "Home"
  def apiDocs = "APIDocs"
  def xmlUpload = "XMLUpload"
  def search = "Search"
  def about = "About"
  def contact = "Contact"
  def faq = "faq"
  def tos = "tos"

  lazy val nameSet = Set(
    home,
    apiDocs,
    xmlUpload,
    search,
    about,
    contact,
    faq,
    tos)

      // Build SiteMap
  def siteMap(): SiteMap = SiteMap(
    Menu.i(home) / "index",
    Menu(apiDocs, S ? "API Docs") / "docs" / "api",
    Menu(xmlUpload, "XML Upload") / "xml_upload" >>
    XmlUploadSnippet.menuParams,
    Menu.i(search) / "search" >> 
      Hidden >>  Snippet("search", a => ProcessSearch.render(a)),
    Menu.i(about) / "about",
    Menu(contact, S ? "Contact Us") / "contact",
    Menu(faq, S ? "faq") / "faq",
    Menu(tos, S ? "Terms of Service") / "tos"
  )

  def dispatch = {
    case x if nameSet.contains(x) => 
      ns => <lift:Menu.item name={x}>{ns}</lift:Menu.item>
  }


}
