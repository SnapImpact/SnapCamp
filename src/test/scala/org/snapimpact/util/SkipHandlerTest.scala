//
package org.snapimpact.util
//
import _root_.org.specs.runner._
import _root_.org.specs._
//
import java.lang._



class SkipHandlerTest extends Runner(new SkipHandlerSpec) with JUnit with Console

class SkipHandlerSpec extends Specification {


    "skipTest" should {
      "be skipped as it's pending and it's set to fail" in {
        org.snapimpact.util.SkipHandler.pendingUntilFixed{
            System.out.println( "* Expected val=" + true + ", was=" + false )
            true mustEqual false
         }
        }
      }


   "sucessPendingTest" should {
   "fail as pending tests should not succeed and it's set to succeed" in {
      try {
      org.snapimpact.util.SkipHandler.pendingUntilFixed{
          System.out.println( "* Expected val=" + true + ", was=" + true )
          true mustEqual true
      }
      } catch {
        // check specifically for FailureException
        case exc: org.specs.execute.FailureException =>{
          System.out.println( "sucessPendingTest cought expected SkipHandler exception, in order to keep the test from failing"  )
      }

     }
     }
   }


}
