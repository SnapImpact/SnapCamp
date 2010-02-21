package org.snapimpact.model

import org.specs.Specification
import helpers.{DbTestDataGenerator, DbTestUtil}

/**
 * Created by IntelliJ IDEA.
 * User: zkim
 * Date: Feb 21, 2010
 * Time: 9:55:46 AM
 * To change this template use File | Settings | File Templates.
 */

object InterestAreaSpec extends Specification {
    import DbTestUtil.{withTx, clearDatabase}

    "InterestAreas" should {
        val ia1 = DbTestDataGenerator.genInterestArea
        val ia2 = DbTestDataGenerator.genInterestArea

        doBefore {
            clearDatabase

            withTx {
                em =>
                    em.merge(ia1)
                    em.merge(ia2)
                    em.flush
            }
        }

        doAfter {
            clearDatabase
            Model.close
        }

        "FindAll should find 2 records" in {
            Model.InterestArea.findAll.size must be equalTo(2)
        }

        "The inserted record should exist" in {
            Model.InterestArea.findById(ia1.getId) must not be equalTo(None)
        }

        "Find by title should find the first interest area" in {
            Model.InterestArea.findByName(ia1.getName).size must be equalTo(1)    
        }

    }
}