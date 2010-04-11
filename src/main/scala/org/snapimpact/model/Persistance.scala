package org.snapimpact
package model

import org.snapimpact.etl.model.dto.FootprintFeed

import net.liftweb.util.Helpers
import geocode.Earth

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

trait FeedStore {
  /**
   * Add a record to the backing store and get a GUID
   * the represents the record
   */
  def create(record: FootprintFeed): GUID

  /**
   * read the GUID from the backing store
   */
  def read(guid: GUID): Option[FootprintFeed]

  /**
   * Read a set of GUIDs from the backing store
   */
  def read(guids: List[GUID]): List[(GUID, FootprintFeed)]

  /**
   * Update the record
   */
  def update(guid: GUID, record: FootprintFeed)


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
}

/**
 * The interface to the Geocoded search
 */
trait GeoStore {
  /**
   * Assocate the GUID and a geo location
   */
  def add(guid: GUID, location: GeoLocation): Unit

  /**
   * Unassocate the GUID and the geo location
   */
  def remove(guid: GUID): Unit

  /**
   * Update the location of a given GUID
   */
  def update(guid: GUID, location: GeoLocation): Unit

  /**
   * Find a series of locations that are within a range of the
   * specified location
   */
  def find(location: GeoLocation, distance: Double,
           first: Int = 0, max: Int = 200): List[GUID]

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
           inSet: Option[Seq[GUID]] = None): List[GUID]
}
