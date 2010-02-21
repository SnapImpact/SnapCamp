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
//  def testFootprintFeed() = {
//    val ffXml = XML.fromFile("src/test/resources/sampleData0.1.r1254.xml")
//    val xmlFeed = FootprintFeed.fromXML(subject)
//    println(xmlFeed.toString)
//  }

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
    val item = Organization.fromXML(subject)
    assertEquals("1", item.organizationID)

  }
}
