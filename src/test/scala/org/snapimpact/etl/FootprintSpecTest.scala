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
import model.dto.{Duration, FootprintFeed}

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
  def testFootprintFeed() = {
    val ffXml = XML.fromFile("src/test/resources/sampleData0.1.r1254.xml")
    val xmlFeed = FootprintFeed.fromXML(subject)
    println(xmlFeed.toString)
  }

  def testDurations() = {
    val subject = <duration>P0Y1347M0D</duration>

    val item = Duration.fromXML(subject)
    println(item.toString)
  }
}

