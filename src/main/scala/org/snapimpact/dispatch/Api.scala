package org.snapimpact.dispatch

import scala.xml.NodeSeq
import net.liftweb.http._
import net.liftweb.common._
import net.liftweb.json._
import net.liftweb.util._
import org.snapimpact.lib.Serializers.anyToRss
import org.snapimpact.etl.model.dto.VolunteerOpportunity
import org.joda.time.format.DateTimeFormat

sealed trait OutputType
final case object JsonOutputType extends OutputType
final case object RssOutputType extends OutputType
final case object HtmlOutputType extends OutputType

object Api {
  def volopps(r: Req): LiftResponse = {
    for{
      key <- r.param("key") ?~ missingKey ~> 401
      valKey <- validateKey(key) ?~ ("Invalid key. " + missingKey) ~> 401
    } yield
      r.param("output") match {
        case Full("json") => JsonResponse(sampleJson)
        case Full("rss") => XmlResponse(sampleRss, "application/rss+xml")
        case _ => XmlResponse(sampleHtml)
      }
  }

  /*
   private class BoxString(in: Box[String]) {
   def asInt = in.flatMap(Helpers.asInt)
   }
   private implicit def bsify(in: Box[String]): BoxString = new BoxString(in)


   def find(query: Option[String] = r.param("q"),
   start: Int = r.param("start").asInt openOr 1,
   num: Int = r.param("num").asInt openOr 10,
   output: OutputType = r.param("output").map(_.toLowerCase) match {
   case Full("rss") => RssOutputType
   case Full("json") => JsonOutputType
   case _ => HtmlOutputType
   }): Unit = {}

   val params = List("timeperiod" -> List("today", "this_month", "this_weekend", "this_week"),
   "vol_startdate", "vol_enddate", "vol_distance", "vol_loc")
   */


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

  def validateKey(key: String): Box[String] = Full(key)

  val missingKey =
  """You seem to be missing the API key parameter ('key') in your query.
Please see the for how to get an API key at
http://www.allforgood.org/docs/api.html for directions."""

  case class Sample(categories: List[String], quality_score: Double, pageviews: Int)
  val sample = Sample(List("category1", "category2"), .5, 2312)

  implicit val formats = Serialization.formats(NoTypeHints)
  val sampleJson = Extraction.decompose(sample) // Serialization.write(sample) 

  val sampleHtml =
  <p>here's some info on volunteer opportunities</p>

  val sampleRss = <rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:fp="http://www.allforgood.org/" xmlns:georss="http://www.georss.org/georss" xmlns:gml="http://www.opengis.net/gml">
    {anyToRss(sample)}
  </rss>
}

class VolOppV1(
        startDate: String,
        minAge: String,
        endDate: String,
        contactPhone: String,
        quality_score: Double,
        detailUrl: String,
        sponsoringOrganizationName: String,
        latlong: String,
        contactName: String,
        addr1: String,
        impressions: Int,
        id: String,
        city: String,
        location_name: String,
        openEnded: String,
        pubDate: String,
        title: String,
        base_url: String,
        virtual: String,
        backfill_title: String,
        provider: String,
        postalCode: String,
        groupid: String,
        audienceAge: String,
        audienceAll: String,
        description: String,
        street1: String,
        street2: String,
        interest_count: Int,
        xml_url: String,
        audienceSexRestricted: String,
        startTime: Int,
        contactNoneNeeded: String,
        categories: List[String],
        contactEmail: String,
        skills: String,
        country: String,
        region: String,
        url_short: String,
        addrname1: String,
        backfill_number: Int,
        endTime: Int
        )

case class RetV1(
        lastBuildDate: String,
        version: Double,
        language: String,
        href: String,
        description: String,
        items: List[VolOppV1]
        )

/*
FixMe Add in Organization lookup once ORganizationStore implemented, sort out addr1, impressions, pubDate,
base_url,backfill_title,
 */
object mapToV1 {
  def apply(in: VolunteerOpportunity): List[VolOppV1] = {
    val fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    for (date <- in.dateTimeDurations; loc <- in.locations)
    yield new VolOppV1(
      date.startDate match {
        case Some(d) => fmt.print(d)
        case None => ""
      },
      in.minimumAge match {
        case Some(a) => a.toString
        case None => ""
      },
      date.endDate match {
        case Some(d) => fmt.print(d)
        case None => ""
      },
      in.contactInfo.contactPhone.getOrElse(""),
      0.0, // quality_score
      in.detailURL.getOrElse(""),
      in.sponsoringOrganizationsIDs.head,
      loc.toLatLngString,
      in.contactInfo.contactName.getOrElse(""),
      "", // addr1
      0, // impressions
      in.volunteerOpportunityID,
      loc.city.getOrElse(""),
      loc.name.getOrElse(""),
      date.openEnded match {
        case Some(yne) => yne.value
        case None => ""
      },
      "", // pub_date
      in.title,
      "", // base_url
      loc.virtual match {
        case Some(yne) => yne.value
        case None => ""
      },
      "", // backfill_title
      "", // provider
      loc.postalCode.getOrElse(""),
      "", // groupid
      "", // audienceAge
      "", // audienceAll
      in.description.getOrElse(""),
      loc.streetAddress1.getOrElse(""),
      loc.streetAddress2.getOrElse(""),
      0, // interest_count
      "", // xml_url
      in.sexRestrictedTo match {
        case Some(yne) => yne.value
        case None => ""
      },
      date.startTime match {
        case Some(t) => t.time.toInt
        case None => 0
      },
      "", // contactNoneNeeded
      in.categoryTags,
      in.contactInfo.contactEmail.getOrElse(""),
      in.skills.getOrElse(""),
      loc.country.getOrElse(""),
      loc.region.getOrElse(""),
      "", // url_short
      "", //addrname1
      0, //backfill_number
      date.endTime match {
        case Some(t) => t.time.toInt
        case None => 0
      }
      )
  }
}

// "2010-05-08 09:00:00"