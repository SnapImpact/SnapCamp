package org.snapimpact.etl.model.dto

import java.util.Date
import org.snapimpact.etl.model.DataModel
import org.joda.time.DateTime

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Feb 20, 2010
 * Time: 4:10:27 PM
 * To change this template use File | Settings | File Templates.
 */

case class FootprintFeed(
  feedInfo: FeedInfo,
  organizations: Organizations,
  opportunities: VolunteerOpportunities,
  reviews: Reviews) extends DataModel {
}

object FootprintFeed {
  def fromXML(node: scala.xml.Node) =
    FootprintFeed(
      FeedInfo.fromXML((node \ "FeedInfo").headOption.get),
      Organizations.fromXML((node \ "Organizations").headOption.get),
      VolunteerOpportunities.fromXML((node \ "VolunteerOpportunities").headOption.get),
      Reviews.fromXML((node \ "Reviews").headOption.get)
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
  feedID: String,
  createdDateTime: DateTime,
  providerURL: String,
  termsOfUse:String,
  description:String
        ) extends DataModel {
}

object FeedInfo {
  def fromXML(node: scala.xml.Node) =
    FeedInfo(
      (node \ "providerID").text,
      (node \ "providerName").text,
      (node \ "feedID").text,
      new DateTime((node \ "createdDateTime").text),
      (node \ "providerURL").text,
      (node \ "termsOfUse").text,
      (node \ "description").text
    )
}
case class Organization(
  organizationID: String,
  nationalEIN:String,
  guidestarID:Integer,
  name:String,
  missionStatement:String,
  description:String,
  location:Location,
  phone:String,
  fax:String,
  email:String,
  organizationURL:String,
  donateURL:String,
  logoURL:String,
  detailURL:String
  ) extends DataModel {
}
object Organization {
  def fromXML(node: scala.xml.Node) =
    Organization(
      (node \ "organizationID").text,
      (node \ "nationalEIN").text,
      new Integer((node \ "guidestarID").text),
      (node \ "name").text,
      (node \ "missionStatement").text,
      (node \ "description").text,
      Location.fromXML((node \ "location").headOption.get),
      (node \ "phone").text,
      (node \ "fax").text,
      (node \ "email").text,
      (node \ "organizationURL").text,
      (node \ "donateURL").text,
      (node \ "logoURL").text,
      (node \ "detailURL").text
    )
}

case class Location(
  virtual:YesNoEnum,
  name:String,
  streetAddress1:String,
  streetAddress2:String,
  streetAddress3:String,
  city:String,
  region:String,
  postalCode:String,
  country:String,
  latitude:Float,
  longitude:Float,
  directions:String
  ) {
}
object Location {
  def fromXML(node: scala.xml.Node) =
    Location(
      YesNoEnum.fromXML((node\"virtual").headOption.get),
      (node\"name").text,
      (node\"streetAddress1").text,
      (node\"streetAddress2").text,
      (node\"streetAddress3").text,
      (node\"city").text,
      (node\"region").text,
      (node\"postalCode").text,
      (node\"country").text,
      (node\"latitude").text.toFloat,
      (node\"longitude").text.toFloat,
      (node\"directions").text
    )
}

/**
 * Turns out Scala uses Tuple as the parameter and tuples have only
 * been defined up through 22 :(
 */
case class VolunteerOpportunity extends DataModel {
  var volunteerOpportunityID:String = null
  var sponsoringOrganizationsIDs:List[String/*sponsoringOrganizationID*/] = null
  var volunteerHubOrganizationsIDs:List[String/*volunteerHubOrganizationID*/] = null
  var title:String = null
  var abstractStr:String = null /* * is abstract in schema ** */
  var volunteersNeeded:Integer = null
  var rsvpCount:Integer = null
  var dateTimeDurations:List[DateTimeDuration] = null
  var locations:List[Location] = null
  var paid:YesNoEnum = null
  var audienceTags:List[String] = null
  var categoryTags:List[String] = null
  var minimumAge:Integer = null
  var sexRestrictedTo:SexRestrictedEnum = null
  var skills:String = null
  var contactName:String = null
  var contactPhone:String = null
  var contactEmail:String = null
  var detailURL:String = null
  var language:String = null
  var description:String = null
  var lastUpdated:DateTimeOlsonDefaultPacific = null
  var expires:DateTimeOlsonDefaultPacific = _
}

object VolunteerOpportunity {
  def fromXML(node: scala.xml.Node) = {
    val vo = new VolunteerOpportunity()
    vo.volunteerOpportunityID = (node \ "volunteerOpportunityID").text
    vo.sponsoringOrganizationsIDs = (node \ "sponsoringOrganizationsIDs").toList.map(_.text)
    vo.volunteerHubOrganizationsIDs = (node \ "volunteerHubOrganizationsIDs").toList.map(_.text)
    vo.title = (node \ "title").text
    vo.abstractStr = (node \ "abstractStr").text
    vo.volunteersNeeded = new Integer((node\"volunteersNeeded").text)
    vo.rsvpCount = new Integer((node\"rsvpCount").text)
    vo.dateTimeDurations = (node \ "dateTimeDurations").toList.map(DateTimeDuration.fromXML(_))
    // TODO Locations
    vo.locations = (node \ "locations").toList.map(Location.fromXML(_))
    vo.paid = YesNoEnum.fromXML((node\"paid").headOption.get)
    // TODO audienceTags
    vo.audienceTags = (node \ "audienceTags").toList.map(_.text)
      // TODO categoryTags
    vo.categoryTags = (node \ "categoryTags").toList.map(_.text)
    vo.minimumAge = new Integer((node\"minimumAge").text)
    vo.sexRestrictedTo = SexRestrictedEnum.fromXML((node\"sexRestrictedTo").headOption.get)
    vo.skills = (node \ "skills").text
    vo.contactName = (node \ "contactName").text
    vo.contactPhone = (node \ "contactPhone").text
    vo.contactEmail = (node \ "contactEmail").text
    vo.detailURL = (node \ "detailURL").text
    vo.language = (node \ "language").text
    vo.description = (node \ "description").text
    vo.lastUpdated = DateTimeOlsonDefaultPacific((node \ "lastUpdated").text)
    vo.expires = DateTimeOlsonDefaultPacific((node \ "expires").text)
    vo
  }
}

case class Review(
    reviewID:String,
    organizationID:String,
    volunteerOpportunityID:String,
    rating:Float,
    ratingMaximum:Float,
    text:String,
    reviewerName:String,
    reviewerID:String,
    reviewerRole:String,
    lastUpdated:DateTimeOlsonDefaultPacific
  ) extends DataModel {
}
object Review {
  def fromXML(node: scala.xml.Node) =
    Review(
      (node \ "reviewID").text,
      (node \ "organizationID").text,
      (node \ "volunteerOpportunityID").text,
      (node\"rating").text.toFloat,
      (node\"ratingMaximum").text.toFloat,
      (node \ "text").text,
      (node \ "reviewerName").text,
      (node \ "reviewerID").text,
      (node \ "reviewerRole").text,
      DateTimeOlsonDefaultPacific((node \ "description").text)
    )
}

case class DateTimeDuration(
  openEnded:YesNoEnum,
  startDate:DateTime,
  endDate:DateTime,
  iCalRecurrence:String,
  duration:Duration,
  startTime:TimeOlson,
  endTime:TimeOlson,
  timeFlexible:YesNoEnum,
  commitmentHoursPerWeek:Float
  ) {

}

object DateTimeDuration {
  def fromXML(node: scala.xml.Node) =
    DateTimeDuration(
      YesNoEnum.fromXML((node \ "openEnded").headOption.get),
      new DateTime((node \ "startDate").text),
      new DateTime((node \ "endDate").text),
      (node \ "iCalRecurrence").text,
      new Duration((node\"duration").text),
      new TimeOlson((node\"startTime").text,((node\"startTime")\"@olsonTZ").text),
      new TimeOlson((node\"endTime").text, ((node\"endTime")\"@olsonTZ").text),
      YesNoEnum.fromXML((node\"timeFlexible").headOption.get),
      (node\"commitmentHoursPerWeek").text.toFloat
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
  olsonTZ:String
) {
}

sealed trait YesNoEnum {
  def value: String
}
object YesNoEnum {
  def fromXML(node: scala.xml.Node) = node.text match {
    case "Yes" => Yes
    case "No" => No
  }
  
}

case object Yes extends YesNoEnum { val value = "Yes" }
case object No extends YesNoEnum { val value = "No" }

sealed trait SexRestrictedEnum {
  def value: String
}

object SexRestrictedEnum {
  def fromXML(node: scala.xml.Node ) = node.text match {
    case "Male" => Male
    case "Female" => Female
    case "Neither" => Neither
  }
}
case object Male extends SexRestrictedEnum { val value = "Male"}
case object Female extends SexRestrictedEnum { val value = "Female" }
case object Neither extends SexRestrictedEnum { val value = "Neither" }

