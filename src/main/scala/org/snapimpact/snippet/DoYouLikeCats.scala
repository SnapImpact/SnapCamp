package org.snapimpact
package snippet

import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.wizard._
import _root_.net.liftweb.common._
import _root_.java.util.Date
import Helpers._


/**
 * An example of a wizard in Lift
 */
object DoYouLikeCats extends Wizard {
  // define the first screen
  val question = new Screen {
    // it has a name field
    val question = new Field with StringField {
      def name = S ? "Do you like cats?"
    }

    // choose the next screen based on the age
    override def nextScreen = if (Helpers.toBoolean(question.is)) meow else woof
  }

  val meow = new Screen {
    override def screenTop = Full(<b>Dude... I like cats too!</b>)
      
    override def nextScreen = Empty
  }

  val woof = new Screen {
    override def screenTop = Full(<i>Sorry to hear you don't like cats</i>)

    override def nextScreen = Empty
  }

  // what to do on completion of the wizard
  def finish() {
    S.notice("Thank you for for telling me your cat preferences")
  }
}


