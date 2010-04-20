package org.snapimpact.etl
/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Feb 20, 2010
 * Time: 5:52:11 PM
 * To change this template use File | Settings | File Templates.
 */
import _root_.junit.framework._
import Assert._
import _root_.scala.xml.XML
import model.dto._
import org.snapimpact.model.PersistenceFactory

object FootprintSpecTest {
  def suite: Test = {
    val suite = new TestSuite(classOf[FootprintSpecTest])
    suite
  }

  def main(args : Array[String]) {
    _root_.junit.textui.TestRunner.run(suite)
  }
}

/**
 * Read the sample file
 */
class FootprintSpecTest extends TestCase("app") {
  /*
   def testFootprintFeed() = {
   val ffXml = XML.fromFile("src/test/resources/sampleData0.1.r1254.xml")
   val xmlFeed = FootprintFeed.fromXML(subject)
   println(xmlFeed.toString)
   }
   */

  def testDuration() = {
    val subject = <duration>P0Y1347M0D</duration>

    val item = Duration.fromXML(subject)
    println(item.toString)
    assert(item.duration == "P0Y1347M0D")
  }

  def testDateTimeDur() = {
    val subject = <dateTimeDuration>
    <openEnded>No</openEnded>
    <startDate>2009-02-22</startDate>
    <endDate>2009-02-22</endDate>
    <startTime>18:45:00</startTime>
    <endTime>21:00:00</endTime>
    </dateTimeDuration>
    val item = DateTimeDuration.fromXML(subject)
    assertEquals(No, item.openEnded.get)
    assertEquals(22, item.startDate.get.dayOfMonth.get)
    assertEquals(None, item.endTime.get.olsonTZ)
  }

  def testNoFeedInfo() = {
    val subject = <FootprintFeed schemaVersion="0.1">
    <VolunteerOpportunities>
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
    </VolunteerOpportunities>
    </FootprintFeed>
    try {
      val item = FootprintFeed.fromXML(subject)
      fail("Should have thrown missing tag exception")
    } catch {
      case e: RuntimeException => // life is good
    }
  }

  def testFeedInfo() = {
    val subject =  <FeedInfo>
    <providerID>1</providerID>
    <providerName>Volunteer Match</providerName>
    <createdDateTime olsonTZ="America/Denver">2008-12-30T14:30:10.5</createdDateTime>
    <providerURL>http://www.volunteermatch.org</providerURL>
    </FeedInfo>
    val item = FeedInfo.fromXML(subject)
    assertEquals("1", item.providerID)
    assertEquals("Volunteer Match", item.providerName)
    assertEquals(30, item.createdDateTime.dayOfMonth.get)
  }
  def testOrganization() = {
    val subject = <Organization>
    <organizationID>1</organizationID>
    <nationalEIN>123456789</nationalEIN>
    <guidestarID>3421</guidestarID>
    <name>MicroMentor | An initiative of Mercy Corps</name>
    <missionStatement>MicroMentor's mission is to help underserved entrepreneurs increase business revenues and navigate the opportunities and challenges of running a small business.</missionStatement>
    <location>
    <city>Portland</city>
    <region>OR</region>
    <postalCode>97232</postalCode>
    </location>
    <organizationURL>http://www.micromentor.org</organizationURL>
    <donateURL>http://www.mercycorps.org/charityweb.php?Custom15=wm&amp;Custom16=1,1</donateURL>
    <logoURL>http://www.micromentor.org/images/MM_LogoTag.gif</logoURL>
    <detailURL>http://www.volunteermatch.org/search/org28450.jsp</detailURL>
    <description>MicroMentor, a Mercy Corps initiative, helps entrepreneurs grow their businesses through mentoring relationships with experienced business professionals. Our mentors volunteer their time to meaningfully impact the lives of those new to the world of small business.</description>
    </Organization>
    val item = Organization.fromXML(subject).open_!
    assertEquals("1", item.organizationID)
  }

  def testLocation() = {
    val subject = <location>
    <city>Trenton</city>
    <region>NJ</region>
    <postalCode>08608</postalCode>
    </location>
    val item = Location.fromXML(subject)
    assertEquals("Trenton", item.city.get)
    assertEquals(None, item.country)
  }

  def testSexRestrict() = {
    def parseSex(sexstr:String) = {
      VolunteerOpportunity.fromXML(
        <VolunteerOpportunity>
        <volunteerOpportunityID>2002</volunteerOpportunityID>
        <sponsoringOrganizationIDs><sponsoringOrganizationID>2</sponsoringOrganizationID></sponsoringOrganizationIDs>
        <title>YOUNG ADULT TO HELP GUIDE MERCER COUNTY TEEN VOLUNTEER CLUB</title>
        <sexRestrictedTo>{sexstr}</sexRestrictedTo>
        </VolunteerOpportunity>).sexRestrictedTo
    }
    assertEquals(Some(Male), parseSex("m"))
    assertEquals(Some(Male), parseSex("male"))
    assertEquals(Some(Male), parseSex("M"))
    assertEquals(Some(Male), parseSex("Male"))
    assertEquals(Some(Male), parseSex("man"))

    assertEquals(Some(Neither), parseSex("neither"))
    assertEquals(Some(Neither), parseSex("N"))
    assertEquals(Some(Neither), parseSex("nEiThEr"))

    assertEquals(Some(Female), parseSex("f"))
    assertEquals(Some(Female), parseSex("female"))
    assertEquals(Some(Female), parseSex("F"))
    assertEquals(Some(Female), parseSex("FEMALE"))
    assertEquals(Some(Female), parseSex("w"))
    assertEquals(Some(Female), parseSex("woman"))

    assertEquals(None, parseSex("FEMAIL"))
    assertEquals(None, parseSex("malefactor"))
    assertEquals(None, parseSex("mandroid"))
  }

  def testVolunteerOpportunity() = {
    
    val item = VolunteerOpportunity.fromXML(FootprintRawData.subject)
    assertEquals("2002", item.volunteerOpportunityID)
    assertEquals(3, item.volunteersNeeded.get)

  }

  def testTheSampleFile() = {
    val subject = XML.loadFile("src/test/resources/sampleData0.1.r1254.xml")
    val item = FootprintFeed.fromXML(subject)
    assertNotNull(item)
    assertEquals(3, item.organizations.size)
    assertEquals(3, item.opportunities.size)
    assertEquals(List(), item.reviews)
    assertEquals("1", item.feedInfo.providerID)
  }

  def testStoreAndRetrieve() = {
    val subject = XML.loadFile("src/test/resources/sampleData0.1.r1254.xml")
    val item = FootprintFeed.fromXML(subject)

    assertNotNull(item)
    val db = PersistenceFactory.opportunityStore.vend
    val guid = db.create(item.opportunities.head)
    val memitem = db.read(guid)
    assertFalse(memitem == None)
    assertEquals(item.opportunities.head, memitem.get)
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
