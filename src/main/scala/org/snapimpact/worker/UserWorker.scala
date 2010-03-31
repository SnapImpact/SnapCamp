package org.snapimpact
package worker

import org.stambecco._

import net.liftweb._
import mapper._
import common._

import org.snapimpact.model.worker._

/**
 * Define an Id for the User class of worker
 */
final case class UserId(id: Long) extends WorkerId {
  type MyType = UserId // what is my type
  type MsgType = UserMsg // what type of messages does the User Worker accept
  type IdType = Long // what is the type of the User id

  /**
   * Generate a manifest for the UserId type
   */
  def myType: Manifest[MyType] = manifest[UserId]

  /**
   * Based on the UserId, calculate a unique name for a file
   */
  def uniqueNameForFile: String = "user_info_"+id
}

/**
 * All messages sent to a User worker must implement this trait
 */
sealed trait UserMsg extends QBase

final case class Property(name: String, value: Option[String]) extends QBase

final case class SetProperty(prop: Property) extends UserMsg

final case class GetProperty(name: String) extends UserMsg with 
MsgWithResponse[Property]

final case class Properties(props: Map[String, String]) extends QBase

final case class GetProperties() extends UserMsg with
MsgWithResponse[Properties]

/**
 * A UserWorker models a user.  The UserWorker needs to
 * know its UserId and needs to be able to calculate a JDBC connection
 * vendor.
 */
class UserWorker(id: UserId,
                 calcFunc: UserId => ConnectionManager) extends
WorkerImpl[UserId, UserMsg](id, calcFunc) {
  def doSetProperty(sp: SetProperty) {
    val p = sp.prop

    UserInfo.findAll(By(UserInfo.name, p.name)).foreach(_.delete_!)
    p.value.foreach (v =>
      UserInfo.create.name(p.name).value(v).save
    )
  }

  def doGetProperty(gp: GetProperty) = {
    Property(gp.name, 
	     UserInfo.find(By(UserInfo.name, gp.name)).
	   map(_.value.is))
  }

  def doGetProperties(pm: GetProperties) =
    Properties(Map(UserInfo.findAll().map(i => (i.name.is,
						i.value.is)) :_*))
}

