package org.snapimpact.dispatch

/**
 * Created by IntelliJ IDEA.
 * User: dave
 * Date: Apr 29, 2010
 * Time: 9:26:28 AM
 * To change this template use File | Settings | File Templates.
 */

import org.snapimpact.etl.model.dto.VolunteerOpportunity
import org.joda.time.format.DateTimeFormat

object VolOppV1 {
  def apply(
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
          ) = {
    new VolOppV1(
      startDate,
      minAge,
      endDate,
      contactPhone,
      quality_score,
      detailUrl,
      sponsoringOrganizationName,
      latlong,
      contactName,
      addr1,
      impressions,
      id,
      city,
      location_name,
      openEnded,
      pubDate,
      title,
      base_url,
      virtual,
      backfill_title,
      provider,
      postalCode,
      groupid,
      audienceAge,
      audienceAll,
      description,
      street1,
      street2,
      interest_count,
      xml_url,
      audienceSexRestricted,
      startTime,
      contactNoneNeeded,
      categories,
      contactEmail,
      skills,
      country,
      region,
      url_short,
      addrname1,
      backfill_number,
      endTime
      )
  }
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
        ) {}

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
      in.sponsoringOrganizationIDs.head,
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