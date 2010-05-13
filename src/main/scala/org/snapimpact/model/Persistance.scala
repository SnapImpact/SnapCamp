package org.snapimpact
package model

import org.snapimpact.etl.model.dto._

import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.http._
import geocode.Earth

object PersistenceFactory extends Factory {
  val opportunityStore = new FactoryMaker[OpportunityStore](() => MemoryOpportunityStore) {}
  val geoStore = new FactoryMaker[GeoStore](() => MemoryGeoStore){}
  val tagStore = new FactoryMaker[TagStore](() => MemoryTagStore){}
  val searchStore = new FactoryMaker[SearchStore](() => MemoryLuceneStore){} 

  val store = new FactoryMaker[Store](DefaultStore){}
}

object Timeperiod extends Enumeration {
  val Today = Value(1, "Today")
  val ThisMonth = Value(2, "This Month")
  val ThisWeekend = Value(3, "This Weekend")
  val ThisWeek = Value(4, "This Week")
}

trait Store {
  private lazy val store = PersistenceFactory.opportunityStore.vend
  private lazy val geo = PersistenceFactory.geoStore.vend
  private lazy val tag = PersistenceFactory.tagStore.vend
  private lazy val search = PersistenceFactory.searchStore.vend

  def add(op: VolunteerOpportunity): GUID = {
    val guid = store.create(op)
    geo.add(guid, op)
    tag.add(guid, op)

    search.add(guid, op)

    guid
  }

  def search(query: Option[String] = None,
             start: Int = 0,
             num: Int = 100,
             provider: Option[String] = None,
             timeperiod: Option[Timeperiod.Value] = None,
             loc: Option[GeoLocation] = None,
             radius: Double = 50): List[(GUID, Double)] 
  = {
    def geoFind(geoLocation: GeoLocation, start: Int, num: Int) = 
      geo.find(location = geoLocation, 
               distance = radius,
               first = start,
               max = num).map
    {
      case (guid, dist) =>
        if (Math.abs(dist) <= 1d) (guid -> 1d)
        else (guid -> (1d / Math.abs(dist)))
    }

    implicit def ltom(in: List[(GUID, Double)]): Map[GUID, Double] =
      Map(in :_*)

    def merge(seed: Map[GUID, Double], other: List[(GUID, Double)]*):
    List[(GUID, Double)] = {
      other.toList match {
        case _ if seed.isEmpty => Nil
        case Nil => seed.toList.sortWith{_._2 > _._2}
        case x :: xs => 
          merge(x.foldLeft[Map[GUID, Double]](Map()){
            case (map, (guid, rank)) =>
              seed.get(guid) match {
                case Some(or) => map + (guid -> (rank * or))
                case None => map
              }
          }, xs :_*)
      }
    }
      
    (query, loc) match {
      case (Some(q), None) => search.find(q, start, num)
      
      case (None, Some(geoLocation)) => {
        geoFind(geoLocation, start, num).sortWith {
          _._2 > _._2
        }
      }


      case (Some(q), Some(geoLocation)) => {
        val qr = search.find(q, 0, (start+ num) * 10)
        
        merge(qr,
              geoFind(geoLocation, 0, (start + num) * 10)).drop(start).take(num)
      }

      case _ => Nil
    }
  }
               

  /**
   * Read a set of GUIDs from the backing store
   */
  def read(guids: List[(GUID, Double)]): List[(GUID, VolunteerOpportunity, Double)] = {
    val ret = store.read(guids)

    /*
    if (ret.length != guids.length) {
      pri ntln("***Failed*** to find "+guids)
    }
    */

    ret
  }
}

private object DefaultStore extends Store


/**
 * This file defines interfaces for the storage mechanism.  It
 * does not specific the actual storage mechansim.
 *
 * @author dpp
 */

final case class GUID(guid: String) extends Ordered[GUID] {
  def compare(that: GUID) = guid compare that.guid
}

object GUID {
  def create(): GUID = new GUID(Helpers.nextFuncName)
}

trait OpportunityStore {
  /**
   * Add a record to the backing store and get a GUID
   * the represents the record
   */
  def create(record: VolunteerOpportunity): GUID

  /**
   * read the GUID from the backing store
   */
  def read(guid: GUID): Option[VolunteerOpportunity]

  /**
   * Read a set of GUIDs from the backing store
   */
  def read(guids: List[(GUID, Double)]): List[(GUID, VolunteerOpportunity, Double)]

  /**
   * Update the record
   */
  def update(guid: GUID, record: VolunteerOpportunity)


  /**
   * Remove the record from the system
   */
  def delete(guid: GUID): Unit
}

/**
 * (Optional) location data
 * @param longitude location longitude in degrees, -180.0..180.0
 * @param latitude location latitude in degrees, -90.0..90.0
 * @param hasLocation if false, there's no latitude or longitude here
 */
final case class GeoLocation(longitude: Double,
                             latitude: Double,
                             hasLocation: Boolean = true)
{
  import org.snapimpact.geocode.Earth

  /**
   * Checks if another point is within given number of miles from this point
   * @param distance max distance (miles)
   * @param of the point relative to which the distance is checked; if the point
   *        does not have the location, the answer is 'false'
   */
  def withinMiles(distance: Double, of: GeoLocation): Boolean = {
    hasLocation &&
    of.hasLocation &&
    Earth.distanceInMiles((latitude,       longitude),
                          (of.latitude, of.longitude)) <= distance
  }

  def distanceFrom(other: GeoLocation): Option[Double] = {
    if (!hasLocation || !other.hasLocation) None
    else Some(Earth.distanceInMiles((latitude,       longitude),
                                    (other.latitude, other.longitude))
)
  }
}

object GeoLocation {
  val Nums = """^(0|(?:[1-9]\d*(?:\.\d*)?)|(?:-\d+(?:\.\d*)?))\,(0|(?:[1-9]\d*(?:\.\d*)?)|(?:-\d+(?:\.\d*)?))$""".r
  def fromString(str: String): Option[GeoLocation] = str match {
    case null => None
    case Nums(lat, long) => Some(GeoLocation(lat.toDouble, long.toDouble))
    case _ => None // FIXME look up city
  }
}

/**
 * The interface to the Geocoded search
 */
trait GeoStore {
  /**
   * Assocate the GUID and a geo location
   */
  def add(guid: GUID, location: List[GeoLocation]): Unit

  /**
   * Unassocate the GUID and the geo location
   */
  def remove(guid: GUID): Unit

  /**
   * Update the location of a given GUID
   */
  def update(guid: GUID, location: List[GeoLocation]): Unit

  /**
   * Find a series of locations that are within a range of the
   * specified location
   */
  def find(location: GeoLocation, distance: Double,
           first: Int = 0, max: Int = 200): List[(GUID, Double)]

  /**
   * Find the GUIDs that do not have a location
   */
  def findNonLocated(first: Int = 0, max: Int = 200): List[GUID]
}

case class Tag(tag: String) extends Ordered[Tag] {
  def compare(other: Tag) = tag compare other.tag
}

/**
 * The store that associates tags and GUIDs
 */
trait TagStore {
  /**
   * Assocate the GUID and a set of tags
   */
  def add(guid: GUID, tags: List[Tag]): Unit

  /**
   * Unassocate the GUID and the tags
   */
  def remove(guid: GUID): Unit

  /**
   * Update the tags associated with a given GUID
   */
  def update(guid: GUID, tags: List[Tag]): Unit

  /**
   *Find a set of GUIDs assocaiated with a set of tags
   */
  def find(tags: List[Tag],
           first: Int = 0, max: Int = 200): List[GUID]
}

/**
 * THe interface for storing stuff in a search engine
 */
trait SearchStore {
  /**
   * Assocate the GUID and an item.
   * @param guid the GUID to associate
   * @param item the item to associate with the GUID
   * @splitter a function that returns the Strings and types of String (e.g., description, 
   */
  def add[T](guid: GUID, item: T)(implicit splitter: T => Seq[(String, Option[String])]): Unit

  /**
   * Unassocate the GUID and words
   */
  def remove(guid: GUID): Unit

  /**
   * Assocate the GUID and a set of words
   */
  def update[T](guid: GUID, item: T)(implicit splitter: T => Seq[(String, Option[String])]): Unit


  /**
   * Find a set of GUIDs assocaiated with a search string, optionally
   * specifing the subset of GUIDs to search and the number of results to
   * return
   */
  def find(search: String,
           first: Int = 0, max: Int = 200,
           inSet: Option[Seq[GUID]] = None): List[(GUID, Double)]
}
