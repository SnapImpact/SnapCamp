package org.snapimpact.model

import org.specs.Specification
import org.slf4j.LoggerFactory

import org.specs._
import org.specs.runner._
import org.specs.Sugar._

import net.liftweb.util._


class SearchStoreTest extends Runner(new SearchStoreSpec) with 
JUnit with Console

class SearchStoreSpec extends Specification {
  lazy val searchStore: SearchStore = 
    new MemoryLuceneStore

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
      
      searchStore.find("eat") must_== List(guid1)
    }

    "Searching works with a second record" in {
      searchStore.add(guid1, "I like to eat fruit")
      searchStore.add(guid2, "Moose are my favorite fruit")
      
      searchStore.find("fruit")

      searchStore.find("eat").take(1) must_== List(guid1)
    }
  }
}


