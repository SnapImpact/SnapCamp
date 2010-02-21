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
      FeedInfo(node \ "feedInfo"),
      Organizations(node \ "Organizations"),
      VolunteerOpportunities((node \ "VolunteerOpportunities")),
      Reviews((node \ "Reviews"))
    )
}

case class Organizations(
  orgs: List[Organization]) extends DataModel {
}

object Organizations {
  def fromXML(node: scala.xml.Node) =
    Organizations((node \ "Organizations").toList.map(Organizations.fromXML(_)))
}

case class VolunteerOpportunities(
  opps: List[VolunteerOpportunity]) extends DataModel {
}

object VolunteerOpportunities {
  def fromXML(node: scala.xml.Node) =
    VolunteerOpportunities((node \ "VolunteerOpportunity").toList.map(VolunteerOpportunities.fromXML(_)))
}

case class Reviews(
  reviews: List[Review]) extends DataModel {
}

object Reviews {
  def fromXML(node: scala.xml.Node) =
    Reviews((node \ "review").toList.map(Reviews.fromXML(_)))
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

case class VolunteerOpportunity(
  volunteerOpportunityID:String,
  sponsoringOrganizationsIDs:List[String/*sponsoringOrganizationID*/],
  volunteerHubOrganizationsIDs:List[String/*volunteerHubOrganizationID*/],
  title:String,
  abstractStr:String, /* * is abstract in schema ** */
  volunteersNeeded:Integer,
  rsvpCount:Integer,
  dateTimeDurations:List[DateTimeDuration],
  locations:List[Location],
  paid:YesNoEnum,
  audienceTags:List[String],
  categoryTags:List[String],
  minimumAge:Integer,
  sexRestrictedTo:SexRestrictedEnum,
  skills:String,
  contactName:String,
  contactPhone:String,
  contactEmail:String,
  detailURL:String,
  language:String,
  description:String,
  lastUpdated:DateTimeOlsonDefaultPacific,
  expires:DateTimeOlsonDefaultPacific
) extends DataModel {

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

object Reviews {
  def fromXML(node: scala.xml.Node) =
    Reviews((node \ "Review").toList.map(Review.fromXML(_)))
}

case class DateTimeDuration(
  openEnded:YesNoEnum,
  startDate:Date,
  endDate:Date,
  iCalRecurrence:String,
  duration:Duration,
  startTime:TimeOlson,
  endTime:TimeOlson,
  timeFlexible:YesNoEnum,
  commitmentHoursPerWeek:Float
  ) {

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

case class DateTimeOlsonDefaultPacific(
  dateTimeNoTZ:String
        ) {

}

case class TimeOlson() {
  
}