package org.snapimpact
package worker

import org.stambecco._

import net.liftweb._
import mapper._
import common._

import org.snapimpact.model.worker._

/**
 * Define an Id for the VolOp class of worker
 */
final case class VolOpId(id: Long) extends WorkerId {
  type MyType = VolOpId // what is my type
  type MsgType = VolOpMsg // what type of messages does the VolOp Worker accept
  type IdType = Long // what is the type of the Vol Op

  /**
   * Generate a manifest for the UserId type
   */
  def myType: Manifest[MyType] = manifest[VolOpId]

  /**
   * Based on the UserId, calculate a unique name for a file
   */
  def uniqueNameForFile: String = "volunteer_op_"+id
}

/**
 * All messages sent to a VolOp worker must implement this trait
 */
sealed trait VolOpMsg extends QBase

final case class UserLikes(userId: UserId) extends QBase

final case class UserUnlikes(userId: UserId) extends QBase

final case class UserClicked(userId: UserId) extends QBase



/**
 * A VolOpWorker models a Volunteer Opportunity.
 */
class VolOpWorker(id: VolOpId,
                  calcFunc: VolOpId => ConnectionManager) extends
WorkerImpl[VolOpId, VolOpMsg](id, calcFunc) {
}

