package org.snapimpact.model

import net.liftweb._
import mapper._

/**
* An example of a Model for the database
*/
class SampleModel extends LongKeyedMapper[SampleModel] with IdPK {
  def getSingleton = SampleModel

  object stringField extends MappedString(this, 128)

  object longField extends MappedLong(this) {
    override def dbIndexed_? = true
  }
}

object SampleModel extends SampleModel with LongKeyedMetaMapper[SampleModel]
