package org.snapimpact
package model

import net.liftweb._
import mapper._

/**
 * This model class represents the most basic type of User...
 * just the username, a unique primary key, there can be
 * username aliases (e.g., Phone number, email address, etc.)
 * 
 */
class User extends LongKeyedMapper[User] with IdPK {
  def getSingleton = User

  lazy val lifecycleCreated = new MappedBoolean(this) {}

  lazy val userName = new MappedPoliteString(this, 256) {
    override def setFilter = List(notNull _,
				  trim _,
				  toLower _) ::: super.setFilter

    override def validations = 
      valUnique("This username already taken") _ ::
    super.validations
  }
}

object User extends User with LongKeyedMetaMapper[User] {
  override def dbIndexes = UniqueIndex(userName) :: super.dbIndexes
}

class UserNameAlias extends LongKeyedMapper[UserNameAlias] with IdPK {
  def getSingleton = UserNameAlias

  lazy val user = new MappedLongForeignKey(this, User) {}
  lazy val identifier = new MappedPoliteString(this, 256) {
    override def setFilter = List(notNull _,
				  trim _,
				  toLower _) ::: super.setFilter

    override def validations = 
      valUnique("This username already taken") _ ::
    super.validations
  }

  lazy val idType = new MappedEnum(this, IdType) {}
}

object IdType extends Enumeration {
  val email = Value("email")
  val phone = Value("phone")
}

object UserNameAlias extends UserNameAlias with 
LongKeyedMetaMapper[UserNameAlias] {
  override def dbIndexes = UniqueIndex(identifier, idType) :: super.dbIndexes
}
