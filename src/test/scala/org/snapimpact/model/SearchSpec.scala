package org.snapimpact.model

import org.specs.Specification
import org.slf4j.LoggerFactory

import org.specs._
import org.specs.runner._
import org.specs.Sugar._

import net.liftweb.util._
import org.snapimpact.etl._
import org.snapimpact.etl.model.dto._

class SearchStoreTest extends Runner(new SearchStoreSpec) with 
JUnit with Console

class SearchStoreSpec extends Specification {
  private lazy val searchStore: SearchStore = 
    new MemoryLuceneStore

  private lazy val opStore = new MemoryOpportunityStore

  lazy val guid1 = GUID.create
  lazy val guid2 = GUID.create
  lazy val guid3 = GUID.create
  lazy val guid4 = GUID.create
  lazy val guid5 = GUID.create

  implicit def strToStore(in: String): Seq[(String, Option[String])] = 
    List(in -> None)

  "Search Store" should {
    "Support storing and searching" in {
      searchStore.add(guid1, "I like to eat fruit")
      
      searchStore.find("eat").map(_._1) must_== List(guid1)
    }

    "Searching works with a second record" in {
      searchStore.add(guid1, "I like to eat fruit")
      searchStore.add(guid2, "Moose are my favorite fruit")
      
      searchStore.find("eat").take(1).map(_._1) must_== List(guid1)
      searchStore.find("fruit").length must_== 2
    }


    "You can search for a VolOpp" in {
      val item = VolunteerOpportunity.fromXML(Map(), FootprintRawData.subject)
      
      val guid = opStore.create(item)

      searchStore.add(guid, item)

      searchStore.find("quest").length must_== 1
    }
  }
}


