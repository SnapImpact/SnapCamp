package org.snapimpact.etl

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Apr 10, 2010
 * Time: 2:31:49 PM
 */

import _root_.junit.framework._
import Assert._
import _root_.scala.xml.XML
import model.dto._
import net.liftweb.json.JsonAST
import net.liftweb.json.JsonDSL._
import net.liftweb.json.Extraction._
//import net.liftweb.json.Printer._


object FootprintSpecMain {
  def suite: Test = {
    val suite = new TestSuite(classOf[FootprintSpecMain])
    suite
  }

  var filename:String = ""

  def main(args : Array[String]) {
    filename = args(0)
    _root_.junit.textui.TestRunner.run(suite)
  }
}

class FootprintSpecMain extends TestCase("app") {
  implicit val formats = net.liftweb.json.DefaultFormats

  def testFootprintFeed() = {
    val subject = XML.loadFile(FootprintSpecMain.filename)
//    println("XML: "+subject.toString)
    val xmlFeed = FootprintFeed.fromXML(subject)
    println(xmlFeed.toString)
//    val xmlFeed = VolunteerOpportunities.fromXML(subject)
//    println(pretty(JsonAST.render(decompose(xmlFeed))))
    assertTrue(xmlFeed != null)
  }
}