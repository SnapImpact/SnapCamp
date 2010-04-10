package org.snapimpact.model

import org.specs.Specification
import helpers.{DbTestDataGenerator, DbTestUtil}

/**
 * Created by IntelliJ IDEA.
 * User: zkim
 * Date: Feb 20, 2010
 * Time: 9:31:28 PM
 * To change this template use File | Settings | File Templates.
 */

import _root_.org.specs._
import _root_.org.specs.runner._
import _root_.org.specs.Sugar._

class DbTestUtilTest extends Runner(new DbTestUtilSpec) with JUnit with Console

class DbTestUtilSpec extends Specification {
    import DbTestUtil.{withTx, clearDatabase}

    "DbTestUtil" should {
        "clear the database correctly" in {
            val ev = DbTestDataGenerator.genEvent
            withTx(_.merge(ev))
            Model.Event.findById(ev.getId) must not be equalTo(None)

            clearDatabase
            Model.Event.findById(ev.getId) must be equalTo(None)
        }
    }
}