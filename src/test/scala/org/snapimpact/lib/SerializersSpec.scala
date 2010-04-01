package org.snapimpact.lib

import org.specs.Specification
import org.snapimpact.lib.Serializers._
import org.snapimpact.etl.model.dto._

import org.specs._
import org.specs.runner._

class SerializationTest extends Runner(SerializersSpec) with JUnit with Console

object SerializersSpec extends Specification {
  "anyToRss" should {

     "serialize a Location" in { 
       anyToRss(
         Location(Some(No), Some("name"), Some("streetAddress1"), Some("streetAddress2"),
           Some("streetAddress3"), Some("city"), Some("region"), Some("postalCode"),
           Some("country"), Some((1.23).asInstanceOf[Float]),
           Some((3.12).asInstanceOf[Float]), Some("directions"))
       ).toString mustMatch("<fp:value>No</fp:value>") 
     }
  }
}
