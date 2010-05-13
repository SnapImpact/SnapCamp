package org.snapimpact
package util

/**
 * Created by IntelliJ IDEA.
 * User: okristjansson
 * Date: Apr 29, 2010
 * Time: 4:00:42 PM
 * To change this template use File | Settings | File Templates.
 */


import _root_.org.specs.execute._

// Skip todo tests
trait skipTodo
{
  def pendingUntilFixed( theTest: => Unit)
}

// skipHandler handles pending tests, implements skipTodo
object SkipHandler extends skipTodo
{
  def apply(theTest: => Unit) = pendingUntilFixed(theTest)

  // Skips tests that are wrapped with the pendingUntilFixed function
  // and fails test that succeed even when wrapped with pendingUntilFixed
  def pendingUntilFixed( theTest: => Unit) {

    // Figure out if test needs to be skipped
    val isPending =
     try {
       theTest
       false
     }
     catch{
        // Catch all errors / exceptions
        case exc =>{
              true
        }
     }

     // If it's pending advertise and throw SkippedException
     if (isPending){
        val reason = "~~~ Test marked pending was skipped, "
       //System.out.println( reason )
        throw new SkippedException( reason )
     }
     else
        throw new FailureException( "Test marked pending succeeded unexpectetly, " )
   }


}  // EOC
