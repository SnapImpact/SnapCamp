package org.snapimpact.model

import org.specs.Specification
import org.slf4j.LoggerFactory
//import helpers.{DbTestDataGenerator, DbTestUtil}

import _root_.org.specs._
import _root_.org.specs.runner._
import _root_.org.specs.Sugar._

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
      searchStore.add(guid2, "Moose are my favorite")
      
      searchStore.find("eat").take(1) must_== List(guid1)
    }
  }
}

class GeoStoreTest extends Runner(new GeoStoreSpec) with JUnit with Console

class GeoStoreSpec extends Specification {
  lazy val geoStore: GeoStore = new MemoryGeoStore

  "Geo Store" should {
    "Associate the GUID and a geo location" in {
      val guid = GUID.create()
      val geoLoc1 = GeoLocation(45.0,1.0)
      val geoLoc2 = GeoLocation(46.0,2.0)
      val geoLoc3 = GeoLocation(47.0,3.0)
      geoStore.add( guid, geoLoc1)
      geoStore.add( guid, geoLoc2)
      geoStore.add( guid, geoLoc3)
      
      /* FIXME -- re-enable geocode test
      geoStore.find( geoLoc1, 2.0,1,50).
      filter(_ == geoLoc2) must_== List(geoLoc2)
      */
    }
  }
}


