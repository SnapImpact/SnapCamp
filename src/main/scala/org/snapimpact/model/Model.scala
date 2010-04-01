package org.snapimpact.model

import org.scala_libs.jpa.LocalEMF
import net.liftweb.jpa.RequestVarEM

object Model extends LocalEMF("pgunit") with RequestVarEM {
    
  implicit def symbolAnyTuple2StringAnyTuple(tup: (Symbol, Any)): 
  (String, Any) = (tup._1.name, tup._2)

    object Event {
        
        def findAll = Model.findAll("Event.findAll")

        def findById(id: String) = Model.findAll("Event.findById", 'id -> id).headOption
        /*match {
            case list if list.size > 0 => Some(list.first)
            case _ => None
        }*/

        def findByTitle(title: String) = Model.findAll("Event.findByTitle", 'title -> title)

        def findByDescription(description: String) = Model.findAll("Event.findByDescription", 'description -> description)

        def findByDuration(duration: java.lang.Long) = Model.findAll("Event.findByDuration", 'duration -> duration)

        def findByContact(contact: String) = Model.findAll("Event.findByContact", 'contact -> contact)

        def findByUrl(url: String) = Model.findAll("Event.findByUrl", 'url -> url)

        def findByPhone(phone: String) = Model.findAll("Event.findByPhone", 'phone -> phone)

        def findByEmail(email: String) = Model.findAll("Event.findByEmail", 'email -> email)

        def findBySourceKey(sourceKey: String) = Model.findAll("Event.findBySourceKey", 'sourceKey -> sourceKey)

        def findBySourceUrl(sourceUrl: String) = Model.findAll("Event.findBySourceUrl", 'sourceUrl -> sourceUrl)

        //TODO: add findNearLocation here once you've figured out what the hell the syntax is
    }


    object InterestArea {
        def findAll = Model.findAll("InterestArea.findAll")

        def findById(id: String) = Model.findAll("InterestArea.findById", 'id -> id).headOption
        /*match {
            case list if list.size > 0 => Some(list.first)
            case _ => None
        }*/

        def findByName(name: String) = Model.findAll("InterestArea.findByName", 'name -> name)
    }
}
