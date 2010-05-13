//
package org.snapimpact.util
//
import _root_.org.specs.runner._
import _root_.org.specs._
import org.specs.execute.FailureException;
//
import java.lang._



class SkipHandlerTest extends Runner(new SkipHandlerSpec) with JUnit with Console

class SkipHandlerSpec extends Specification {

  // The skipHandler makes sure pending tests are skipped
  "skipTest" should {
    "be skipped as it's pending and it's set to fail" in {
      org.snapimpact.util.SkipHandler.pendingUntilFixed{
        true mustEqual false
      }
    }
  }
  
  
  // The skipHandler throws errors when skipped tests succeed
  "sucessPendingTest" should  {
    "fail as pending tests should not succeed and it's set to succeed" in {
      org.snapimpact.util.SkipHandler.pendingUntilFixed {
        true mustEqual true
      } must throwA[FailureException]
    }
  }
}
