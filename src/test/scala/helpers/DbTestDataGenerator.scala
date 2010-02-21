package org.snapimpact.model

import java.util.UUID

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

        e
    }


    def rndStr = UUID.randomUUID.toString
    def rndLong = (Math.random * 1000).asInstanceOf[Long]

}