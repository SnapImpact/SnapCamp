package bootstrap.liftweb

import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.provider._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import _root_.net.liftweb.mapper.{DB, ConnectionManager, Schemifier, DefaultConnectionIdentifier, StandardDBVendor}
import _root_.java.sql.{Connection, DriverManager}
import _root_.org.snapimpact.model._


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    if (!DB.jndiJdbcConnAvailable_?) {
      val vendor = 
	new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
			     Props.get("db.url") openOr 
			     "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
			     Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    }

    // where to search snippet
    LiftRules.addToPackages("org.snapimpact")
    

    // Build SiteMap
    def entries = Menu(Loc("Home", List("index"), "Home")) ::
    Menu(Loc("docs.api", List("docs", "api"), "API Docs")) ::
    Menu(Loc("xmlupload", List("xml_upload"), "Xml Upload")) ::
    Menu(Loc("test", Link(List("test"), true, "/test/hello"), "TestOrn")) ::
    Nil

    LiftRules.setSiteMapFunc(() => SiteMap(entries:_*))

    /*
     * Show the spinny image when an Ajax call starts
     */
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    /*
     * Make the spinny image go away when it ends
     */
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    LiftRules.early.append(makeUtf8)

    LiftRules.statelessDispatchTable.append {
      case r @ Req("api" :: "upload" :: Nil, _, _) =>
        () => org.snapimpact.dispatch.FeedUpload.upload(r)
      case r @ Req("api" :: "volopps" :: Nil, _, _) =>
        println("volops")
        () => Full(org.snapimpact.dispatch.Api.volopps(r))
    }

    S.addAround(DB.buildLoanWrapper)
  }

  /**
   * Force the request to be UTF-8
   */
  private def makeUtf8(req: HTTPRequest) {
    req.setCharacterEncoding("UTF-8")
  }

  /**
   * Set up resources
   */
  ResourceServer.allow {
    case "css" :: _ => true
  }
}
