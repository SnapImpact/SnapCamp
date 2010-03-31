package org.snapimpact.dispatch

/**
 * Created by IntelliJ IDEA.
 * User: okristjansson
 * Date: Mar 16, 2010
 * Time: 8:38:30 PM
 * To change this template use File | Settings | File Templates.
 */

import _root_.junit.framework._

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

}

