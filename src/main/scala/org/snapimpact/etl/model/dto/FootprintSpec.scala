package org.snapimpact.etl.model.dto

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
  // Organizations is optional
  organizations: Option[Organizations],
  opportunities: VolunteerOpportunities,
  // Reviews is optional
  reviews: Option[Reviews]) extends DataModel {
}

object FootprintFeed {
  def fromXML(node: scala.xml.Node) =
    FootprintFeed(
      FeedInfo.fromXML((node \ "FeedInfo").headOption.get),
      (node \ "Organizations").headOption match {
        case None    => None
        case Some(x) => Some(Organizations.fromXML(x))
      },
      VolunteerOpportunities.fromXML((node \ "VolunteerOpportunities").headOption.get),
      (node \ "Reviews").headOption match {
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
      (node \ "feedID").headOption match {
            case None    => None
            case Some(x) => Some(x.text)
          },
      new DateTime((node \ "createdDateTime").text),
      (node \ "providerURL").headOption match {
            case None    => None
            case Some(x) => Some(x.text)
          },
      (node \ "termsOfUse").headOption match {
            case None    => None
            case Some(x) => Some(x.text)
          },
      (node \ "description").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      }
    )
}
case class Organization(
  organizationID: String,
  nationalEIN:Option[String],
  guidestarID:Option[Integer],
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
      (node \ "nationalEIN").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "guidestarID").headOption match {
        case None    => None
        case Some(x) => Some(new Integer(x.text))
      },
      (node \ "name").text,
      (node \ "missionStatement").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "description").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "location").headOption match {
        case None    => None
        case Some(x) => Some(Location.fromXML(x))
      },
      (node \ "phone").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "fax").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "email").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "organizationURL").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "donateURL").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "logoURL").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "detailURL").headOption match {
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
      (node\"virtual").headOption match {
        case None    => None
        case Some(x) => Some(YesNoEnum.fromXML(x))
      },
      (node\"name").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node\"streetAddress1").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node\"streetAddress2").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node\"streetAddress3").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node\"city").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node\"region").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node\"postalCode").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node\"country").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node\"latitude").headOption match {
        case None    => None
        case Some(x) => Some(x.text.toFloat)
      },
      (node\"longitude").headOption match {
        case None    => None
        case Some(x) => Some(x.text.toFloat)
      },
      (node\"directions").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      }
    )
}

/**
 * Turns out Scala uses Tuple as the parameter and tuples have only
 * been defined up through 22 :(
 */
case class VolunteerOpportunity extends DataModel {
  var volunteerOpportunityID:String = null
  var sponsoringOrganizationsIDs:Option[List[String/*sponsoringOrganizationID*/]] = _
  var volunteerHubOrganizationsIDs:Option[List[String/*volunteerHubOrganizationID*/]] = _
  var title:String = _
  var abstractStr:Option[String] = _ /* * is abstract in schema ** */
  var volunteersNeeded:Option[Integer] = _
  var rsvpCount:Option[Integer] = _
  var dateTimeDurations:List[DateTimeDuration] = null
  var locations:Option[List[Location]] = _
  var paid:Option[YesNoEnum] = _
  var audienceTags:Option[List[String]] = _
  var categoryTags:Option[List[String]] = _
  var minimumAge:Option[Integer] = _
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
    vo.sponsoringOrganizationsIDs = (node \ "sponsoringOrganizationsIDs").headOption match {
        case None    => None
        case Some(x) => Some(x.toList.map(_.text))
    }
    vo.volunteerHubOrganizationsIDs = (node \ "volunteerHubOrganizationsIDs").headOption match {
        case None    => None
        case Some(x) => Some(x.toList.map(_.text))
    }
    vo.title = (node \ "title").text
    vo.abstractStr = (node \ "abstractStr").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
    }
    vo.volunteersNeeded = (node\"volunteersNeeded").headOption match {
        case None    => None
        case Some(x) => Some(new Integer(x.text))
    }
    vo.rsvpCount = (node\"rsvpCount").headOption match {
        case None    => None
        case Some(x) => Some(new Integer(x.text))
    }
    vo.dateTimeDurations = (node \ "dateTimeDurations").toList.map(DateTimeDuration.fromXML(_))
    // TODO Locations
    vo.locations = (node \ "locations").headOption match {
        case None    => None
        case Some(x) => Some(x.toList.map(Location.fromXML(_)))
    }
    vo.paid = (node\"paid").headOption match {
        case None    => None
        case Some(x) => Some(YesNoEnum.fromXML(x))
    }
    // TODO audienceTags
    vo.audienceTags = (node \ "audienceTags").headOption match {
        case None    => None
        case Some(x) => Some(x.toList.map(_.text))
    }
    // TODO categoryTags
    vo.categoryTags = (node \ "categoryTags").headOption match {
        case None    => None
        case Some(x) => Some(x.toList.map(_.text))
    }
    vo.minimumAge = (node\"minimumAge").headOption match {
        case None    => None
        case Some(x) => Some(new Integer(x.text))
    }
    vo.sexRestrictedTo = (node\"sexRestrictedTo").headOption match {
        case None    => None
        case Some(x) => Some(SexRestrictedEnum.fromXML(x))
    }
    vo.skills = (node \ "skills").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
    }
    vo.contactName = (node \ "contactName").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
    }
    vo.contactPhone = (node \ "contactPhone").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
    }
    vo.contactEmail = (node \ "contactEmail").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
    }
    vo.detailURL = (node \ "detailURL").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
    }
    vo.language = (node \ "language").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
    }
    vo.description = (node \ "description").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
    }
    vo.lastUpdated = (node \ "lastUpdated").headOption match {
        case None    => None
        case Some(x) => Some(DateTimeOlsonDefaultPacific(x.text))
    }
    vo.expires = (node \ "expires").headOption match {
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
      (node \ "organizationID").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "volunteerOpportunityID").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node\"rating").headOption match {
        case None    => None
        case Some(x) => Some(x.text.toFloat)
      },
      (node\"ratingMaximum").headOption match {
        case None    => None
        case Some(x) => Some(x.text.toFloat)
      },
      (node \ "text").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "reviewerName").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "reviewerID").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "reviewerRole").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node \ "description").headOption match {
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
      (node \ "openEnded").headOption match {
        case None    => None
        case Some(x) => Some(YesNoEnum.fromXML(x))
      },
      (node \ "startDate").headOption match {
        case None    => None
        case Some(x) => Some(new DateTime(x.text))
      },
      (node \ "endDate").headOption match {
        case None    => None
        case Some(x) => Some(new DateTime(x.text))
      },
      (node \ "iCalRecurrence").headOption match {
        case None    => None
        case Some(x) => Some(x.text)
      },
      (node\"duration").headOption match {
        case None    => None
        case Some(x) => Some(new Duration(x.text))
      },
      (node\"startTime").headOption match {
        case None    => None
        case Some(x) => Some(
          new TimeOlson(x.text,(x\"@olsonTZ").headOption match {
            case None    => None
            case Some(y) => Some(y.text)
          }))
      },
      (node\"endTime").headOption match {
        case None    => None
        case Some(x) => Some(
          new TimeOlson(x.text,(x\"@olsonTZ").headOption match {
            case None    => None
            case Some(y) => Some(y.text)
          }))
      },
      (node\"timeFlexible").headOption match {
        case None    => None
        case Some(x) => Some(YesNoEnum.fromXML(x))
      },
      (node\"commitmentHoursPerWeek").headOption match {
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
