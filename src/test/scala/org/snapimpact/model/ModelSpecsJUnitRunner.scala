package org.snapimpact.model

import org.specs.Specification
import org.snapimpact.snippet.HelloWorld
import org.snapimpact.lib.DependencyFactory
import net.liftweb.http.{S, LiftSession}
import org.specs.specification.Examples
import net.liftweb.common.Empty
import java.math.BigInteger
import java.util.UUID
import org.specs.runner.{JUnit3, JUnit4}
import org.slf4j.{LoggerFactory, Logger}
import javax.persistence.EntityManager

class ModelSpecsTest extends JUnit3(EventSpec)

object EventSpec extends Specification {
    import DbTestUtil.{withTx, clearDatabase}

    val log = LoggerFactory.getLogger("EventSpec")

    "Events" should {

        val ev1 = DbTestDataGenerator.genEvent
        val ev2 = DbTestDataGenerator.genEvent
        val ev3 = DbTestDataGenerator.genEvent

        doBefore {
            clearDatabase

            //OMFG: Can't use straight Model.merge, causes strange behavior, have to access underlying EntityManager
            withTx {
                em =>
                    em.merge(ev1)
                    em.merge(ev2)
                    em.merge(ev3)
                    em.flush
            }
        }

        doAfter {
            clearDatabase
        }

        "Be created successfully" in {

            val eventFromDb = Model.findAll[Event]("Event.findById", "id" -> ev1.getId).first
            eventFromDb.getId must be(ev1.getId)
        }

        "findAll should find 3 Event records" in {
            Model.Event.findAll.size must be equalTo (3)
        }

        "findById should find the first Event" in {
            Model.Event.findById(ev1.getId) must not be equalTo (None)
        }

        "findByTitle should find the second Event" in {
            Model.Event.findByTitle(ev2.getTitle).size must be equalTo(1)
        }

        "findByDescription should find the third Event" in {
            Model.Event.findByDescription(ev3.getDescription).size must be equalTo(1)
        }

        "findByDuration should find the first Event" in {
            Model.Event.findByDuration(ev1.getDuration).size must be equalTo(1)
        }

        "findByContact should find the second Event" in {
            Model.Event.findByContact(ev2.getContact).size must be equalTo(1)
        }

        "findByUrl should find the third Event" in {
            Model.Event.findByUrl(ev3.getUrl).size must be equalTo(1)
        }

        "findByPhone should find the first Event" in {
            Model.Event.findByPhone(ev1.getPhone).size must be equalTo(1)
        }

        "findByEmail should find the second Event" in {
            Model.Event.findByEmail(ev2.getEmail).size must be equalTo(1)
        }

        "findBySourceKey should find the third Event" in {
            Model.Event.findBySourceKey(ev3.getSourceKey).size must be equalTo(1)
        }

        "findBySourceUrl should find the first Event" in {
            Model.Event.findBySourceUrl(ev1.getSourceUrl).size must be equalTo(1)
        }






    }


}

