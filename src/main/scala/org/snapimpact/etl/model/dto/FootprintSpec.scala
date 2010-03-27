package org.snapimpact.etl.model.dto

import org.snapimpact.etl.model.DataModel
import org.joda.time.DateTime
import xml.Node
import net.liftweb.util.Helpers

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Feb 20, 2010
 * Time: 4:10:27 PM
 * To change this template use File | Settings | File Templates.
 */


/**
 * This object contains a series of implicits that make
 * the code a lot cleaner for parsing the XML
 */
object ParseHelper {
  class ParseHelperHelper(node: Node) {
    def %[T](name: String)(implicit cvt: Node => Option[T]): Option[T] =
    (node \ name).headOption.flatMap(cvt)
  }

  implicit def nodeToHelp(in: Node): ParseHelperHelper = new ParseHelperHelper(in)

  implicit def cvtString: Node => Option[String] = s => Some(s.text)
  implicit def cvtFloat: Node => Option[Float] = s => Helpers.tryo(s.text.toFloat)
  implicit def cvtInt: Node => Option[Int] = n => Helpers.asInt(n.text)
  implicit def cvtYesNo: Node => Option[YesNoEnum] = n => Helpers.tryo(YesNoEnum.fromXML(n))
  implicit def cvtReviews: Node => Option[Reviews] = n => Helpers.tryo(Reviews.fromXML(n))
}

import ParseHelper._

case class FootprintFeed(
  feedInfo: FeedInfo,
  // Organizations is optional
  organizations: Option[Organizations],
  opportunities: VolunteerOpportunities,
  // Reviews is optional
  reviews: Option[Reviews]) extends DataModel {
}

object FootprintFeed {
  def fromXML(node: scala.xml.Node) =
    FootprintFeed(
      FeedInfo.fromXML((node \ "FeedInfo").firstOption.get),
      (node \ "Organizations").firstOption match {
        case None    => None
        case Some(x) => Some(Organizations.fromXML(x))
      },
      VolunteerOpportunities.fromXML((node \ "VolunteerOpportunities").firstOption.get),
      node % "Reviews"
    )
}

case class Organizations(
  orgs: List[Organization]) extends DataModel {
}

object Organizations {
  def fromXML(node: scala.xml.Node) =
    Organizations((node \ "Organization").toList.map(Organization.fromXML(_)))
}

case class VolunteerOpportunities(
  opps: List[VolunteerOpportunity]) extends DataModel {
}

object VolunteerOpportunities {
  def fromXML(node: scala.xml.Node) =
    VolunteerOpportunities((node \ "VolunteerOpportunity").toList.map(VolunteerOpportunity.fromXML(_)))
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
    description:Option[String]
  ) extends DataModel {
}

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
      (node \ "location").firstOption match {
        case None    => None
        case Some(x) => Some(Location.fromXML(x))
      },
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
  latitude:Option[Float],
  longitude:Option[Float],
  directions:Option[String]
  ) {
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
      node % "directions"
    )
}

/**
 * Turns out Scala uses Tuple as the parameter and tuples have only
 * been defined up through 22 :(
 */
case class VolunteerOpportunity() extends DataModel {
  var volunteerOpportunityID:String = null
  var sponsoringOrganizationsIDs:Option[List[String/*sponsoringOrganizationID*/]] = _
  var volunteerHubOrganizationsIDs:Option[List[String/*volunteerHubOrganizationID*/]] = _
  var title:String = _
  var abstractStr:Option[String] = _ /* * is abstract in schema ** */
  var volunteersNeeded:Option[Int] = _
  var rsvpCount:Option[Int] = _
  var dateTimeDurations:List[DateTimeDuration] = null
  var locations:Option[List[Location]] = _
  var paid:Option[YesNoEnum] = _
  var audienceTags:Option[List[String]] = _
  var categoryTags:Option[List[String]] = _
  var minimumAge:Option[Int] = _
  var sexRestrictedTo:Option[SexRestrictedEnum] = _
  var skills:Option[String] = _
  var contactName:Option[String] = _
  var contactPhone:Option[String] = _
  var contactEmail:Option[String] = _
  var detailURL:Option[String] = _
  var language:Option[String] = _
  var description:Option[String] = _
  var lastUpdated:Option[DateTimeOlsonDefaultPacific] = _
  var expires:Option[DateTimeOlsonDefaultPacific] = _
}

object VolunteerOpportunity {
  def fromXML(node: scala.xml.Node) = {
    val vo = new VolunteerOpportunity()
    vo.volunteerOpportunityID = (node \ "volunteerOpportunityID").text
    vo.sponsoringOrganizationsIDs = (node \ "sponsoringOrganizationsIDs").firstOption match {
        case None    => None
        case Some(x) => Some(x.toList.map(_.text))
    }
    vo.volunteerHubOrganizationsIDs = (node \ "volunteerHubOrganizationsIDs").firstOption match {
        case None    => None
        case Some(x) => Some(x.toList.map(_.text))
    }
    vo.title = (node \ "title").text
    vo.abstractStr = node % "abstractStr"
    vo.volunteersNeeded = node % "volunteersNeeded"
    vo.rsvpCount = node % "rsvpCount"
    vo.dateTimeDurations = (node \ "dateTimeDurations").toList.map(DateTimeDuration.fromXML(_))
    // TODO Locations
    vo.locations = (node \ "locations").firstOption match {
        case None    => None
        case Some(x) => Some(x.toList.map(Location.fromXML(_)))
    }
    vo.paid = node % "paid"
    // TODO audienceTags
    vo.audienceTags = (node \ "audienceTags").firstOption match {
        case None    => None
        case Some(x) => Some(x.toList.map(_.text))
    }
    // TODO categoryTags
    vo.categoryTags = (node \ "categoryTags").firstOption match {
        case None    => None
        case Some(x) => Some(x.toList.map(_.text))
    }
    vo.minimumAge = node % "minimumAge"
    vo.sexRestrictedTo = (node\"sexRestrictedTo").firstOption match {
        case None    => None
        case Some(x) => Some(SexRestrictedEnum.fromXML(x))
    }
    vo.skills = node % "skills"
    vo.contactName = node % "contactName"
    vo.contactPhone = node % "contactPhone"
    vo.contactEmail = node % "contactEmail"
    vo.detailURL = node % "detailURL"
    vo.language = node % "language"
    vo.description = node % "description"
    vo.lastUpdated = (node \ "lastUpdated").firstOption match {
        case None    => None
        case Some(x) => Some(DateTimeOlsonDefaultPacific(x.text))
    }
    vo.expires = (node \ "expires").firstOption match {
        case None    => None
        case Some(x) => Some(DateTimeOlsonDefaultPacific(x.text))
    }
    vo
  }
}

case class Review(
    reviewID:String,
    organizationID:Option[String],
    volunteerOpportunityID:Option[String],
    rating:Option[Float],
    ratingMaximum:Option[Float],
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
      (node \ "description").firstOption match {
        case None    => None
        case Some(x) => Some(DateTimeOlsonDefaultPacific(x.text))
      }
    )
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
  commitmentHoursPerWeek:Option[Float]
  ) {

}

object DateTimeDuration {
  def fromXML(node: scala.xml.Node) =
    DateTimeDuration(
      node % "openEnded",
      (node \ "startDate").firstOption match {
        case None    => None
        case Some(x) => Some(new DateTime(x.text))
      },
      (node \ "endDate").firstOption match {
        case None    => None
        case Some(x) => Some(new DateTime(x.text))
      },
      node % "iCalRecurrence",
      (node\"duration").firstOption match {
        case None    => None
        case Some(x) => Some(new Duration(x.text))
      },
      (node\"startTime").firstOption match {
        case None    => None
        case Some(x) => Some(
          new TimeOlson(x.text,(x\"@olsonTZ").firstOption match {
            case None    => None
            case Some(y) => Some(y.text)
          }))
      },
      (node\"endTime").firstOption match {
        case None    => None
        case Some(x) => Some(
          new TimeOlson(x.text,(x\"@olsonTZ").firstOption match {
            case None    => None
            case Some(y) => Some(y.text)
          }))
      },
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
case class Duration(
  duration:String
  ) {
}

object Duration {
  def fromXML(node: scala.xml.Node) =
    Duration(node.text)
}

case class DateTimeOlsonDefaultPacific(
  dateTimeNoTZ:String
) {

}

case class TimeOlson(
  time:String,
  olsonTZ:Option[String]
) {
}

sealed trait YesNoEnum {
  def value: String
}
object YesNoEnum {
  def fromXML(node: scala.xml.Node) = node.text.toLowerCase match {
    case "yes" => Yes
    case "no" => No
  }
  
}

case object Yes extends YesNoEnum { val value = "Yes" }
case object No extends YesNoEnum { val value = "No" }

sealed trait SexRestrictedEnum {
  def value: String
}

object SexRestrictedEnum {
  def fromXML(node: scala.xml.Node ) = node.text.toLowerCase match {
    case "male" => Male
    case "female" => Female
    case _ /* "Neither"  don't break if it's not specified */ => Neither
  }
}
final case object Male extends SexRestrictedEnum { val value = "Male"}
final case object Female extends SexRestrictedEnum { val value = "Female" }
final case object Neither extends SexRestrictedEnum { val value = "Neither" }
