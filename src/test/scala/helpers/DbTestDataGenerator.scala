package helpers

import java.util.{ArrayList, UUID}
import org.snapimpact.model.{InterestArea, Event}

/**
 * Created by IntelliJ IDEA.
 * User: zkim
 * Date: Feb 20, 2010
 * Time: 9:00:42 PM
 * To change this template use File | Settings | File Templates.
 */

object DbTestDataGenerator {
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