package org.snapimpact.dispatch

/**
 * Created by IntelliJ IDEA.
 * User: okristjansson
 * Date: Mar 16, 2010
 * Time: 8:38:30 PM
 * To change this template use File | Settings | File Templates.
 */
import java.io.Serializable
import net.liftweb.json._
import _root_.junit.framework._
import net.liftweb.util.JSONParser
import org.snapimpact.model.Event


object ApiTest {
  def suite: Test = {
    val suite = new TestSuite(classOf[ApiTest])
    suite
  }

  def main(args : Array[String]) {
    _root_.junit.textui.TestRunner.run(suite)
  }
}

/**
 * Read the sample file
 */
class ApiTest extends TestCase("app")
{

  def testSearch() =
  {
    val lEvents = MockSearch.getEvents()
    println( "Event Count=" + lEvents.length );
    assert( lEvents.length > 0  )
  }


  def testSerializeEvents() =
  {
    // Init serializer
    implicit val formats = Serialization.formats(NoTypeHints)


    // First make a simple test class
    case class Sample(categories: List[String], quality_score: Long, pageviews: Int, Other1: String, Other2: String)
    //val sample2 = Sample(List("category1", "category2"), 535, 2312, "Other1TestData", "Other2TestData")
    val sample = Serialization.write( Sample(List("category1", "category2"), 535, 2312, "Other1TestData", null) )
    //
    println( "Sample=" + sample );



    // Serialize event
    val lEvents = MockSearch.getEvents
    val myEvent = lEvents{0};
    val sampleJson = Serialization.write(myEvent)
    //
    println( "Event=" + sampleJson );

  }


}

