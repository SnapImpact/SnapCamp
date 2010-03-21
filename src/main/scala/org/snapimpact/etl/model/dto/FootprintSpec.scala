package org.snapimpact.etl.model.dto

import org.snapimpact.etl.model.DataModel
import org.joda.time.DateTime
import runtime.RichString

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Feb 20, 2010
 * Time: 4:10:27 PM
 * To change this template use File | Settings | File Templates.
 */

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
      (node \ "Reviews").firstOption match {
        case None    => None
        case Some(x) => Some(Reviews.fromXML(x))
      }
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
      (node \ "feedID").firstOption match {
            case None    => None
            case Some(x) => Some(x.text)
          },
      new DateTime((node \ "createdDateTime").text),
      (node \ "providerURL").firstOption match {
            case None    => None
            case Some(x) => Some(x.text)
          },
      (node \ "termsOfUse").firstOption match {
            case None    => None
            case Some(x) => Some(x.text)
          },
      (node \ "description").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      }
    )
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
      (node \ "nationalEIN").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "guidestarID").firstOption match {
        case None    => None
        case Some(x) => Some(x.text.toInt)
      },
      (node \ "name").text,
      (node \ "missionStatement").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "description").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "location").firstOption match {
        case None    => None
        case Some(x) => Some(Location.fromXML(x))
      },
      (node \ "phone").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "fax").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "email").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "organizationURL").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "donateURL").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "logoURL").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "detailURL").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      }
    )
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
      (node\"virtual").firstOption match {
        case None    => None
        case Some(x) => Some(YesNoEnum.fromXML(x))
      },
      (node\"name").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node\"streetAddress1").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node\"streetAddress2").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node\"streetAddress3").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node\"city").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node\"region").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node\"postalCode").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node\"country").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node\"latitude").firstOption match {
        case None    => None
        case Some(x) => Some(x.text.toFloat)
      },
      (node\"longitude").firstOption match {
        case None    => None
        case Some(x) => Some(x.text.toFloat)
      },
      (node\"directions").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      }
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
    vo.abstractStr = (node \ "abstractStr").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
    }
    vo.volunteersNeeded = (node\"volunteersNeeded").firstOption match {
        case None    => None
        case Some(x) => Some(x.text.toInt)
    }
    vo.rsvpCount = (node\"rsvpCount").firstOption match {
        case None    => None
        case Some(x) => Some(x.text.toInt)
    }
    vo.dateTimeDurations = (node \ "dateTimeDurations").toList.map(DateTimeDuration.fromXML(_))
    // TODO Locations
    vo.locations = (node \ "locations").firstOption match {
        case None    => None
        case Some(x) => Some(x.toList.map(Location.fromXML(_)))
    }
    vo.paid = (node\"paid").firstOption match {
        case None    => None
        case Some(x) => Some(YesNoEnum.fromXML(x))
    }
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
    vo.minimumAge = (node\"minimumAge").firstOption match {
        case None    => None
        case Some(x) => Some(x.text.toInt)
    }
    vo.sexRestrictedTo = (node\"sexRestrictedTo").firstOption match {
        case None    => None
        case Some(x) => Some(SexRestrictedEnum.fromXML(x))
    }
    vo.skills = (node \ "skills").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
    }
    vo.contactName = (node \ "contactName").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
    }
    vo.contactPhone = (node \ "contactPhone").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
    }
    vo.contactEmail = (node \ "contactEmail").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
    }
    vo.detailURL = (node \ "detailURL").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
    }
    vo.language = (node \ "language").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
    }
    vo.description = (node \ "description").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
    }
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
      (node \ "organizationID").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "volunteerOpportunityID").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node\"rating").firstOption match {
        case None    => None
        case Some(x) => Some(x.text.toFloat)
      },
      (node\"ratingMaximum").firstOption match {
        case None    => None
        case Some(x) => Some(x.text.toFloat)
      },
      (node \ "text").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "reviewerName").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "reviewerID").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "reviewerRole").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
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
      (node \ "openEnded").firstOption match {
        case None    => None
        case Some(x) => Some(YesNoEnum.fromXML(x))
      },
      (node \ "startDate").firstOption match {
        case None    => None
        case Some(x) => Some(new DateTime(x.text))
      },
      (node \ "endDate").firstOption match {
        case None    => None
        case Some(x) => Some(new DateTime(x.text))
      },
      (node \ "iCalRecurrence").firstOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
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
      (node\"timeFlexible").firstOption match {
        case None    => None
        case Some(x) => Some(YesNoEnum.fromXML(x))
      },
      (node\"commitmentHoursPerWeek").firstOption match {
        case None    => None
        case Some(x) => Some(x.text.toFloat)
      }
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
