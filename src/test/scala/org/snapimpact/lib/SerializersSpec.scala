package org.snapimpact.lib

import org.specs.Specification
import org.snapimpact.lib.Serializers._
import org.snapimpact.etl.model.dto._


object SerializersSpec extends Specification {
  "anyToRss" should {

     "serialize a Location" in { 
       anyToRss(
         Location(No, "name", "streetAddress1", "streetAddress2", "streetAddress3",
           "city", "region", "postalCode", "country", (1.23).asInstanceOf[Float], 
           (3.12).asInstanceOf[Float], "directions")
       ).toString mustMatch("<fp:value>No</fp:value>") 
     }
  }
}
