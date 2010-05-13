package org.snapimpact
package etl.model.dto

import org.snapimpact.etl.model.DataModel
import org.joda.time.DateTime
import xml.Node
import net.liftweb.util.Helpers
import net.liftweb.common._
import model._
import geocode._

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Feb 20, 2010
 * Time: 4:10:27 PM
 */


/**
 * This object contains a series of implicits that make
 * the code a lot cleaner for parsing the XML
 */
object ParseHelper {
  class ParseHelperHelper(node: Node) {
    // The optional elements
    def %[T](name: String)(implicit cvt: Node => Option[T]): Option[T] = { 
      //FIXME This does the wrong thing with repeatable optional elements, but at least the failure is explicit
      val ns = (node \ name)
      if (ns.length > 1)
        throw new RuntimeException("Can't handle repeated elements: "+ns)
      else ns.headOption.flatMap(cvt)
    }

    // The required elements
    def %%[T](name: String)(implicit cvt: Node => Option[T]): T = {
      %(name)(cvt) match {
        case None => throw new RuntimeException("Required tag not found: "+name)
        case a @ _ => a.get
      }
    }
  }

  implicit def nodeToHelp(in: Node): ParseHelperHelper = new ParseHelperHelper(in)

  implicit def cvtString: Node => Option[String] = s => Some(s.text)
  implicit def cvtDouble: Node => Option[Double] = s => Helpers.tryo(s.text.toDouble)
  implicit def cvtInt: Node => Option[Int] = n => Helpers.asInt(n.text)
  implicit def cvtYesNo: Node => Option[YesNoEnum] = n => YesNoEnum.fromXML(n)
  implicit def cvtReviews: Node => Option[Reviews] = n => Helpers.tryo(Reviews.fromXML(n))

  implicit def cvtTimeOlson: Node => Option[TimeOlson] =
    n => Some(new TimeOlson(n.text,
			    (n \ "@olsonTZ").headOption.
			    map(_.text)))
  implicit def cvtOrg: Node => Option[Organizations] =
    n => Some(Organizations.fromXML(n))

  implicit def cvtLocation: Node => Option[Location] =
    n => Some(Location.fromXML(n))

  implicit def cvtVolOp(implicit orgMap: Map[String, Organization]): Node => Option[VolunteerOpportunities] =
    n => Some(VolunteerOpportunities.fromXML(orgMap, n))
  
  implicit def cvtFeedInfo: Node => Option[FeedInfo] =
    n => Some(FeedInfo.fromXML(n))

  implicit def cvtSexRestrictedEnum: Node => Option[SexRestrictedEnum] =
    n => SexRestrictedEnum.fromXML(n)

  implicit def cvtDTOlson: Node => Option[DateTimeOlsonDefaultPacific] =
    n => Some(DateTimeOlsonDefaultPacific(n.text))

  implicit def cvtDateTime: Node => Option[DateTime] =
    n => Some(new DateTime(n.text))

  implicit def cvtDuration: Node => Option[Duration] =
    n => Some(new Duration(n.text))
}

import ParseHelper._

case class FootprintFeed(
  feedInfo: FeedInfo,
  // Organizations is optional
  organizations: Option[Organizations],
  // Spec lists VolunteerOpportunities as optional, why required here?
  opportunities: VolunteerOpportunities,
  // Reviews is optional
  reviews: Option[Reviews]) extends DataModel

object FootprintFeed {
  def fromXML(node: scala.xml.Node) =
    if (node \ "@schemaVersion" != "0.1")
      throw new RuntimeException("""missing required attribute schemaVersion="0.1" in FootprintFeed""")
    else {
      val orgs: Option[Organizations] = node % "Organizations"
      
      implicit val orgMap = Map((for {
        orgList <- orgs.toList
        org <- orgList.orgs
      } yield org.organizationID -> org) :_*)

      FootprintFeed(
        node %% "FeedInfo",
        orgs,
        node %% "VolunteerOpportunities",
        node % "Reviews")
    }
}

case class Organizations(
  orgs: List[Organization]) extends DataModel {
}

object Organizations {
  def fromXML(node: scala.xml.Node) =
    Organizations((node \ "Organization").toList.map(Organization.fromXML(_)))
}

case class VolunteerOpportunities(opps: List[VolunteerOpportunity]) extends DataModel

object VolunteerOpportunities {
  def fromXML(orgMap: Map[String, Organization], node: scala.xml.Node) =
    VolunteerOpportunities((node \ "VolunteerOpportunity").toList.map(VolunteerOpportunity.fromXML(orgMap, _)))
}

case class Reviews(
  reviews: List[Review]) extends DataModel {
}

object Reviews {
  def fromXML(node: scala.xml.Node) =
    Reviews((node \ "Review").toList.map(Review.fromXML(_)))
}

case class FeedInfo(
    providerID: String,
    providerName: String,
    feedID: Option[String],
    createdDateTime: DateTime,
    providerURL: Option[String],
    termsOfUse:Option[String],
    description:Option[String]) extends DataModel

object FeedInfo {
  def fromXML(node: scala.xml.Node) =
    FeedInfo(
      (node \ "providerID").text,
      (node \ "providerName").text,
      node % "feedID",
      new DateTime((node \ "createdDateTime").text),
      node % "providerURL",
      node % "termsOfUse",
      node % "description")
}
case class Organization(
  organizationID: String,
  nationalEIN:Option[String],
  guidestarID:Option[Int],
  name:String,
  missionStatement:Option[String],
  description:Option[String],
  location:Option[Location],
  phone:Option[String],
  fax:Option[String],
  email:Option[String],
  organizationURL:Option[String],
  donateURL:Option[String],
  logoURL:Option[String],
  detailURL:Option[String]
  ) extends DataModel {
}
object Organization {
  def fromXML(node: scala.xml.Node) =
    Organization(
      (node \ "organizationID").text,
      node % "nationalEIN",
      node % "guidestarID",
      (node \ "name").text,
      node % "missionStatement",
      node % "description",
      node % "location",
      node % "phone",
      node % "fax",
      node % "email",
      node % "organizationURL",
      node % "donateURL",
      node % "logoURL",
      node % "detailURL")
}

case class Location(
  virtual:Option[YesNoEnum],
  name:Option[String],
  streetAddress1:Option[String],
  streetAddress2:Option[String],
  streetAddress3:Option[String],
  city:Option[String],
  region:Option[String],
  postalCode:Option[String],
  country:Option[String],
  latitude:Option[Double],
  longitude:Option[Double],
  directions:Option[String]
  ) {

  def toGeoLocation: Box[GeoLocation] = 
    (virtual, latitude, longitude) match {
    case (Some(Yes), _, _) => Full(GeoLocation(0,0, false))
    case (_, Some(lat), Some(long)) => Full(GeoLocation(long, lat, true))
      
      case _ => Geocoder(streetAddress1 ::
                       streetAddress2 ::
                       streetAddress3 ::
                       city ::
                       region ::
                       postalCode ::
                       country :: Nil)
  }

   private implicit def optStrListToStr(in: List[Option[String]]): String = {
     in.flatMap(a => a).mkString(", ")
   }

  def toLatLngString: String = {
    val latStr = this.latitude match {
      case Some(n) => n.toString
      case None => ""
    }
    val lngStr = this.longitude match {
      case Some(n) => n.toString
      case None => ""
    }
    if ( latStr.isEmpty || lngStr.isEmpty ) {
      ""
    } else {
      latStr + " " + lngStr
    }
  }
}
object Location {
  def fromXML(node: scala.xml.Node) =
    Location(
      node % "virtual",
      node % "name",
      node % "streetAddress1",
      node % "streetAddress2",
      node % "streetAddress3",
      node % "city",
      node % "region",
      node % "postalCode",
      node % "country",
      node % "latitude",
      node % "longitude",
      node % "directions")
}

/**
 * Turns out Scala uses Tuple as the parameter and tuples have only
 * been defined up through 22 :(
 */
case class VolunteerOpportunity(
  volunteerOpportunityID:String,
  sponsoringOrganizationIDs:List[String/*sponsoringOrganizationID*/],
  volunteerHubOrganizationIDs:List[String/*volunteerHubOrganizationID*/],
  organizations: List[Organization],
  title:String,
  abstractStr:Option[String], /* * is abstract in schema ** */
  volunteersNeeded:Option[Int],
  rsvpCount:Option[Int],
  dateTimeDurations:List[DateTimeDuration],
  locations: List[Location],
  paid:Option[YesNoEnum],
  audienceTags: List[String],
  categoryTags: List[String],
  minimumAge:Option[Int],
  sexRestrictedTo:Option[SexRestrictedEnum],
  skills:Option[String],
  contactInfo: ContactInfo,
  detailURL:Option[String],
  language:Option[String],
  description:Option[String],
  lastUpdated:Option[DateTimeOlsonDefaultPacific],
  expires:Option[DateTimeOlsonDefaultPacific]
) extends DataModel

case class ContactInfo(contactName:Option[String],
		       contactPhone:Option[String],
		       contactEmail:Option[String]) extends DataModel

object ContactInfo {
  def fromXML(node: Node) = new ContactInfo(
    contactName = node % "contactName",
    contactPhone = node % "contactPhone",
    contactEmail = node % "contactEmail")
}


object VolunteerOpportunity {
  implicit def voToSearch(in: VolunteerOpportunity):
  Seq[(String, Option[String])] = {
    (in.title -> None) ::
    in.organizations.map(_.name -> None) :::
    in.description.map(d => (d -> None)).toList :::
    in.abstractStr.map(d => (d -> Some("abstract"))).toList :::
    in.audienceTags.map(t => (t -> Some("audience"))) :::
    in.categoryTags.map(t => (t -> Some("category"))) :::
    in.skills.map(s => (s -> Some("skill"))).toList :::
    in.language.map(s => (s -> Some("language"))).toList
  }
  
  implicit def voToGeo(in: VolunteerOpportunity): List[GeoLocation] = 
    in.locations match {
      case Nil => List(GeoLocation(0,0, false))
      case xs => xs.flatMap(_.toGeoLocation)
    }
    

  implicit def voToTags(in: VolunteerOpportunity): List[Tag] =
    (in.audienceTags.map(Tag.apply) :::
     in.categoryTags.map(Tag.apply)).removeDuplicates

  def fromXML(orgMap: Map[String, Organization], 
              node: scala.xml.Node) = {
    
    val sponsors =
      (node \ "sponsoringOrganizationIDs").toList.map(_.text.trim)

    val hubs = 
      (node \ "volunteerHubOrganizationIDs").toList.map(_.text)

    val orgs = (sponsors ::: hubs).flatMap(orgMap.get)

    new VolunteerOpportunity(
    volunteerOpportunityID = (node \ "volunteerOpportunityID").text,
    sponsoringOrganizationIDs = sponsors,
      volunteerHubOrganizationIDs = hubs,
      organizations = orgs,
      title = (node \ "title").text,
      abstractStr = node % "abstractStr",
      volunteersNeeded = node % "volunteersNeeded",
      rsvpCount = node % "rsvpCount",
      dateTimeDurations =
	(node \ "dateTimeDurations").toList.map(DateTimeDuration.fromXML(_)),
      locations = (node \ "locations").toList.flatMap(
        x =>
          (x \ "location").map(Location.fromXML(_))
      ),
      paid = node % "paid",
      audienceTags = (node \ "audienceTags").toList.map(_.text),
      categoryTags = (node \ "categoryTags").toList.map(_.text),
      minimumAge = node % "minimumAge",
      sexRestrictedTo = node % "sexRestrictedTo",
      skills = node % "skills",
      contactInfo = ContactInfo.fromXML(node),
      detailURL = node % "detailURL",
      language = node % "language",
      description = node % "description",
      lastUpdated = node % "lastUpdated",
      expires = node % "expires")
  }
}

case class Review(
    reviewID:String,
    organizationID:Option[String],
    volunteerOpportunityID:Option[String],
    rating:Option[Double],
    ratingMaximum:Option[Double],
    text:Option[String],
    reviewerName:Option[String],
    reviewerID:Option[String],
    reviewerRole:Option[String],
    lastUpdated:Option[DateTimeOlsonDefaultPacific]
  ) extends DataModel {
}
object Review {
  def fromXML(node: scala.xml.Node) =
    Review(
      (node \ "reviewID").text,
      node % "organizationID",
      node % "volunteerOpportunityID",
      node % "rating",
      node % "ratingMaximum",
      node % "text",
      node % "reviewerName",
      node % "reviewerID",
      node % "reviewerRole",
      node % "description")
}

case class DateTimeDuration(
  openEnded:Option[YesNoEnum],
  startDate:Option[DateTime],
  endDate:Option[DateTime],
  iCalRecurrence:Option[String],
  duration:Option[Duration],
  startTime:Option[TimeOlson],
  endTime:Option[TimeOlson],
  timeFlexible:Option[YesNoEnum],
  commitmentHoursPerWeek:Option[Double]
  ) {

}

object DateTimeDuration {
  def fromXML(node: scala.xml.Node) =
    DateTimeDuration(
      node % "openEnded",
      node % "startDate",
      node % "endDate",
      node % "iCalRecurrence",
      node % "duration",
      node % "startTime",
      node % "endTime",
      node % "timeFlexible",
      node % "commitmentHoursPerWeek"
    )
}

/**
 * From:
 * http://www.w3.org/TR/xmlschema-2/#duration
 *
 * Similarly, the duration element "PT3H" gives a period
 * ("P") of 2 hours ("H"). Periods are given in descending
 * value of time. Thus a period of two days, six hours and
 * eleven minutes is rendered as "P2DT6H11N". Again, while
 * not immediately legible, this format is easy to parse.
 * The T indicates that the values following are granular
 * units of time less than one day.
 */
case class Duration(duration:String)

object Duration {
  def fromXML(node: scala.xml.Node) =
    Duration(node.text)
}

case class DateTimeOlsonDefaultPacific(dateTimeNoTZ:String)


case class TimeOlson(time:String, olsonTZ:Option[String])

sealed trait YesNoEnum {
  def value: String
}
object YesNoEnum {
  def fromXML(node: scala.xml.Node) = node.text.toLowerCase match {
    case "yes" => Some(Yes)
    case "no" => Some(No)
    case _ => None
  }

}

case object Yes extends YesNoEnum { val value = "Yes" }
case object No extends YesNoEnum { val value = "No" }

sealed trait SexRestrictedEnum {
  def value: String
}

object SexRestrictedEnum {
  val male_rx = "m(ale|an)?".r
  val female_rx = """(f(emale)?|w(oman)?)""".r
/*  val female_rx = "f(emale)?".r */
/*  val female_rx = """(f|female|w|woman)""".r */
  val neither_rx = "n(either)?".r
  def fromXML(node: scala.xml.Node ) = {
       node.text.toLowerCase.trim match {
    case male_rx(capgroup) => Some(Male)
    case female_rx(capgroup,_,_) => Some(Female)
    case neither_rx(capgroup) => Some(Neither)
    case _ => None
    }
  }
}
final case object Male extends SexRestrictedEnum { val value = "Male"}
final case object Female extends SexRestrictedEnum { val value = "Female" }
final case object Neither extends SexRestrictedEnum { val value = "Neither" }
