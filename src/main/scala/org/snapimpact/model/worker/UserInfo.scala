package org.snapimpact
package model
package worker

import net.liftweb._
import mapper._

/**
 * Each Stambecco worker has its own private data store.  This is the
 * Info about the user (e.g., name, address, etc.)
 */
class UserInfo extends LongKeyedMapper[UserInfo] with IdPK {
  def getSingleton = UserInfo

  /**
   * The name of the property
   */
  lazy val name = new MappedPoliteString(this, 256) {
    override def dbIndexed_? = true
  }
  
  /**
   * The value of the property
   */
  lazy val value = new MappedPoliteString(this, 4096) {}
  
}

object UserInfo extends UserInfo with LongKeyedMetaMapper[UserInfo]

