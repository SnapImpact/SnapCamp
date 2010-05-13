/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Feb 20, 2010
 * Time: 5:52:11 PM
 * To change this template use File | Settings | File Templates.
 */

package org.snapimpact.etl

import model.dto._
import _root_.scala.xml.XML
import org.specs.Specification
import _root_.org.specs.runner._
import org.snapimpact.model._

import org.slf4j.LoggerFactory
import net.liftweb.util._

class FootprintSpecTest extends Runner(new FootprintSpecs) with JUnit with Console

class FootprintSpecs extends Specification
{
  "FootprintSpec " should
  {
    "Parse a duration from XML" in {Duration.fromXML(<duration>P0Y1347M0D</duration>).duration must equalIgnoreCase("P0Y1347M0D")}

    "Parse a date-time from XML" in
    {
      val subject = <dateTimeDuration>
      <openEnded>No</openEnded> <startDate>2009-02-22</startDate> <endDate>2009-02-22</endDate> <startTime>18:45:00</startTime> <endTime>21:00:00</endTime>
      </dateTimeDuration>
      val item = DateTimeDuration.fromXML(subject)
      item.openEnded.get must beEqualTo(No);
      item.startDate.get.dayOfMonth.get mustEqual 22
      item.endTime.get.olsonTZ must beNone
    }

    "Parse a date time duration with a strange format" in
    {
      val dur = DateTimeDuration.fromXML(<dateTimeDuration>
                                         <openEnded>No</openEnded> <startDate>2010-03-03</startDate> <endDate>2010-03-21</endDate> <startTime>18:55:00</startTime> <endTime>20:20:00</endTime>
                                         </dateTimeDuration>)
      dur.startDate.get.monthOfYear.get mustEqual 3
      dur.endDate.get.monthOfYear.get mustEqual 3
    }

    "Parse organization info from XML" in
    {
      val item = Organization.fromXML(<Organization>
                                      <organizationID>1</organizationID> <nationalEIN>123456789</nationalEIN> <guidestarID>3421</guidestarID> <name>MicroMentor | An initiative of Mercy Corps</name> <missionStatement>MicroMentor's mission is to help underserved entrepreneurs increase business revenues and navigate the opportunities and challenges of running a small business.</missionStatement> <location>
                                      <city>Portland</city> <region>OR</region> <postalCode>97232</postalCode>
                                      </location> <organizationURL>http://www.micromentor.org</organizationURL> <donateURL>http://www.mercycorps.org/charityweb.php?Custom15=wm
                                      &amp;
                                      Custom16=1,1</donateURL> <logoURL>http://www.micromentor.org/images/MM_LogoTag.gif</logoURL> <detailURL>http://www.volunteermatch.org/search/org28450.jsp</detailURL> <description>MicroMentor, a Mercy Corps initiative, helps entrepreneurs grow their businesses through mentoring relationships with experienced business professionals.Our mentors volunteer their time to meaningfully impact the lives of those new to the world of small business.</description>
                                      </Organization>)
                                      item.organizationID mustEqual "1"
                                      item.nationalEIN must beSome("123456789")
                                      item.guidestarID must beSome(3421)
                                      item.name mustEqual "MicroMentor | An initiative of Mercy Corps"
                                      item.missionStatement must beSomething
                                      item.location must beSomething
                                      item.organizationURL must beSomething
                                      item.donateURL must beSomething
                                      item.logoURL must beSomething
                                      item.detailURL must beSomething
                                      item.description must beSomething
                                    }

      "Parse location info from XML" in
      {
        val item = Location.fromXML(<location>
                                    <city>Trenton</city> <region>NJ</region> <postalCode>08608</postalCode>
                                    </location>)
        item.city must beSome("Trenton")
        item.region must beSome("NJ")
        item.postalCode must beSome("08608")
      }

      "Parse XML from a file" in
      {
        val item = FootprintFeed.fromXML(XML.loadFile("src/test/resources/sampleData0.1.r1254.xml"))
        item.organizations.get.orgs.size mustEqual 3
        item.opportunities.opps.size mustEqual 3
        item.opportunities.opps(0).volunteerHubOrganizationIDs(0) mustEqual "3011"
        item.reviews must beNone
        item.feedInfo.providerID mustEqual "1"
      }

      val volunteerOpportunity = FootprintRawData.subject

      "Throw an exception when attempting to parse XML with a missing required element" in
      {
        val subject = <FootprintFeed schemaVersion="0.1">
        <VolunteerOpportunities>
        {volunteerOpportunity}
        </VolunteerOpportunities>
        </FootprintFeed>
        FootprintFeed.fromXML(subject) must throwA[RuntimeException] // Missing Tag Exception
      }

      val feedInfo = <FeedInfo>
      <providerID>1</providerID> <providerName>Volunteer Match</providerName> <createdDateTime olsonTZ="America/Denver">2008-12-30T14:30:10.5</createdDateTime> <providerURL>http://www.volunteermatch.org</providerURL>
      </FeedInfo>

      "Throw an exception when attempting to parse XML with an extra required element" in
      {
        val subject = <FootprintFeed schemaVersion="0.1">
        {feedInfo}
        {feedInfo}
        <VolunteerOpportunities>
        {volunteerOpportunity}
        </VolunteerOpportunities>
        </FootprintFeed>
        FootprintFeed.fromXML(subject) must throwA[RuntimeException]
      }

      "Throw an exception when attempting to parse XML without a schemaVersion" in
      {
        val subject = <FootprintFeed>
        {feedInfo}
        <VolunteerOpportunities>
        {volunteerOpportunity}
        </VolunteerOpportunities>
        </FootprintFeed>
        FootprintFeed.fromXML(subject) must throwA[RuntimeException]
      }


      "Parse FeedInfo XML" in
      {
        val item = FeedInfo.fromXML(feedInfo)
        item.providerID mustEqual "1"
        item.providerName must equalIgnoreCase("Volunteer Match")
        item.createdDateTime.getDayOfMonth mustEqual 30
      }

      "Parse Volunteer Opportunities from XML" in
      {
        val item = VolunteerOpportunity.fromXML(Map(), volunteerOpportunity)
        item.volunteerOpportunityID mustEqual "2002"
        item.sponsoringOrganizationIDs.size mustEqual 1
        item.sponsoringOrganizationIDs.head mustEqual "2"
        item.title must find(".*MERCER")
        item.volunteersNeeded must beSome(3)
        item.skills must beSomething
        item.detailURL must beSomething
        item.description must beSome[String].which(_.startsWith("Quixote"))
        item.lastUpdated must beSomething
      }

      "parse game #249" in {
        val res = VolunteerOpportunity.fromXML(Map(),
                                               <VolunteerOpportunity>
                                               <volunteerOpportunityID>GameId_504</volunteerOpportunityID>
                                               <volunteerHubOrganizationIDs />
                                               <title />
                                               <dateTimeDurations>
                                               <dateTimeDuration>
                                               <openEnded>No</openEnded>
                                               <startDate>2010-05-28T00:00:00-07:00</startDate>
                                               <endDate>2010-05-28T02:30:00-07:00</endDate>
                                               <iCalRecurrence />
                                               <duration>2.5</duration>
                                               <commitmentHoursPerWeek>0</commitmentHoursPerWeek>
                                               </dateTimeDuration>
                                               </dateTimeDurations>
                                               <locations>
                                               <location>
                                               <name>Pepsi Center</name>
                                               <streetAddress1>1000 Chopper Circle</streetAddress1>
                                               <streetAddress2 />
                                               <city>Denver</city>
                                               <region>CO</region>
                                               <postalCode>80204</postalCode>
                                               <country>USA</country>
                                               </location>
                                               </locations>
                                               <categoryTags>
                                               <categoryTag>Playoffs</categoryTag>
                                               </categoryTags>
                                               <sponsoringOrganizationIDs>
                                               <sponsoringOrganizationID>sponsoringOrgId_505</sponsoringOrganizationID>
                                               </sponsoringOrganizationIDs>
                                               <minimumAge />
                                               <skills>Bring your A game</skills>
                                               <detailURL />
                                               <description>New Jersey Devils vs NY Rangers game #249</description>
                                               </VolunteerOpportunity>)

        res.locations.length must_== 1
      }



      "Parse Sex Restrictions in Volunteer Opportunities" in
      {
        def parseSex(sexstr: String) =
          {
            VolunteerOpportunity.fromXML(Map(),
                                         <VolunteerOpportunity>
                                         <volunteerOpportunityID>2002</volunteerOpportunityID> <sponsoringOrganizationIDs>
                                         <sponsoringOrganizationID>2</sponsoringOrganizationID>
                                         </sponsoringOrganizationIDs> <title>YOUNG ADULT TO HELP GUIDE MERCER COUNTY TEEN VOLUNTEER CLUB</title> <sexRestrictedTo>
                                         {sexstr}
                                         </sexRestrictedTo>
                                         </VolunteerOpportunity>).sexRestrictedTo
          }
        parseSex("m") must beSome(Male)
        parseSex("M") must beSome(Male)
        parseSex("man") must beSome(Male)
        parseSex("male") must beSome(Male)
        parseSex("Male") must beSome(Male)

        parseSex("f") must beSome(Female)
        parseSex("F") must beSome(Female)
        parseSex("female") must beSome(Female)
        parseSex("Female") must beSome(Female)
        parseSex("w") must beSome(Female)
        parseSex("W") must beSome(Female)
        parseSex("woman") must beSome(Female)

        parseSex("feMail") must beNone
        parseSex("malefactor") must beNone
        parseSex("mandroid") must beNone
        parseSex("TentacleMonster") must beNone
        parseSex("Cthulhu") must beNone
      }


      "Parse XML from a file, store and retrieve a guid" in
      {

        val item = FootprintFeed.fromXML(XML.loadFile("src/test/resources/sampleData0.1.r1254.xml"))

        item must notBeNull
        val db = PersistenceFactory.searchStore.vend
        val guid = GUID.create
        db.add(guid, item.opportunities.opps.head)

        val memitem = db.find("MicroMentor")

        memitem must notBeNull

        memitem.map(_._1).find(_ == guid) must beEqual(Some(guid))
      }
    }
  }

  object FootprintRawData {
    val subject =   
      <VolunteerOpportunity>
    <volunteerOpportunityID>2002</volunteerOpportunityID>
    <sponsoringOrganizationIDs><sponsoringOrganizationID>2</sponsoringOrganizationID></sponsoringOrganizationIDs>
    <title>YOUNG ADULT TO HELP GUIDE MERCER COUNTY TEEN VOLUNTEER CLUB</title>
    <volunteersNeeded>3</volunteersNeeded>
    <dateTimeDurations>
    <dateTimeDuration>
    <openEnded>No</openEnded>
    <startDate>2009-01-01</startDate>
    <endDate>2009-05-31</endDate>
    <iCalRecurrence>FREQ=WEEKLY;INTERVAL=2</iCalRecurrence>
    <commitmentHoursPerWeek>2</commitmentHoursPerWeek>
    </dateTimeDuration>
    </dateTimeDurations>
    <locations>
    <location>
    <city>Mercer County</city>
    <region>NJ</region>
    <postalCode>08610</postalCode>
    </location>
    </locations>
    <audienceTags>
    <audienceTag>Teens</audienceTag>
    </audienceTags>
    <categoryTags>
    <categoryTag>Community</categoryTag>
    <categoryTag>Children &amp; Youth</categoryTag>
    </categoryTags>
    <skills>Be interested in promoting youth volunteerism. Be available two Tuesday evenings per month.</skills>
    <detailURL>http://www.volunteermatch.org/search/opp200517.jsp</detailURL>
    <description>Quixote Quest is a volunteer club for teens who have a passion for community service. The teens each volunteer for their own specific cause. Twice monthly, the club meets. At the club meetings the teens from different high schools come together for two hours to talk about their volunteer experiences and spend some hang-out time together that helps them bond as fraternity...family. Quixote Quest is seeking young adults roughly between 20 and 30 years of age who would be interested in being a guide and advisor to the teens during these two evening meetings a month.</description>
    <lastUpdated olsonTZ="America/Denver">2008-12-02T19:02:01</lastUpdated>
    </VolunteerOpportunity>
  }

