package org.snapimpact.model

import org.specs.Specification
import org.slf4j.LoggerFactory
import helpers.{DbTestDataGenerator, DbTestUtil}

import _root_.org.specs._
import _root_.org.specs.runner._
import _root_.org.specs.Sugar._

import net.liftweb.util._

class TagStoreTest extends Runner(new TagStoreSpec) with JUnit with Console

class TagStoreSpec extends Specification {
  lazy val tagStore: TagStore = new MemoryTagStore // FIXME choose real impl

  "Tag Store" should {
    "Associate add a GUID" in {
      val guid = GUID.create()
      val tl = List(Tag("foo"), Tag("bar"))
      
      tagStore.add(guid, tl)
      tagStore.find(List(Tag("foo"))) must_== List(guid)
    }
  }
}
