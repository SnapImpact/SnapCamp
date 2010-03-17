package org.snapimpact.dispatch

import org.snapimpact.model.{Event, InterestArea}
import java.util.{ArrayList, UUID}

/**
 * Created by IntelliJ IDEA.
 * User: okristjansson
 * Date: Feb 23, 2010
 * Time: 9:13:25 PM
 * To change this template use File | Settings | File Templates.
 */

class MockSearch
{

  def getEvents() =
  {
    var lEvents = genEvent :: Nil;
    //
    lEvents = genEvent :: lEvents
    lEvents = genEvent :: lEvents
    lEvents = genEvent :: lEvents
    lEvents = genEvent :: lEvents

    // returns the list
    lEvents
  }


      def genEvent = {
        val e = new Event
        e.setId(rndStr)
        e.setTitle(rndStr)
        e.setDescription(rndStr)
        e.setDuration(rndLong)
        e.setContact(rndStr)
        e.setUrl(rndStr)
        e.setPhone(rndStr)
        e.setEmail(rndStr)
        e.setSourceKey(rndStr)
        e.setSourceUrl(rndStr)
        e.setInterestAreaCollection(new ArrayList[InterestArea]())
        e.getInterestAreaCollection.add(genInterestArea)
        e.getInterestAreaCollection.add(genInterestArea)
        e.getInterestAreaCollection.add(genInterestArea)

        e
    }

    def genInterestArea = {
        val i = new InterestArea
        i.setId(rndStr)
        i.setName(rndStr)

        i
    }

    def rndStr = UUID.randomUUID.toString
    def rndLong = (Math.random * 1000).asInstanceOf[Long]

}
