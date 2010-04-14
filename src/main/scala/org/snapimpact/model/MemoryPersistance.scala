package org.snapimpact
package model

import org.snapimpact.etl.model.dto.FootprintFeed

import net.liftweb._
import util.Helpers

/**
 * This file contains an implementation of in-memory persistance
 *
 * @author dpp
 */

object MemoryFeedStore extends MemoryFeedStore

class MemoryFeedStore extends FeedStore {
  private var data: Map[GUID, FootprintFeed] = Map()

  /**
   * Add a record to the backing store and get a GUID
   * the represents the record
   */
  def create(record: FootprintFeed): GUID = synchronized {
    val guid = GUID(Helpers.nextFuncName)

    data += guid -> record

    guid
  }

  /**
   * read the GUID from the backing store
   */
  def read(guid: GUID): Option[FootprintFeed] = synchronized {
    data.get(guid)
  }

  /**
   * Read a set of GUIDs from the backing store
   */
  def read(guids: List[GUID]): List[(GUID, FootprintFeed)] = synchronized {
    guids.flatMap(g => data.get(g).map(f => (g, f)))
  }

  /**
   * Update the record
   */
  def update(guid: GUID, record: FootprintFeed) = synchronized {
    data += guid -> record
  }


  /**
   * Remove the record from the system
   */
  def delete(guid: GUID): Unit = synchronized {
    data -= guid
  }
}

/**
 * The interface to the Geocoded search
 */
object MemoryGeoStore extends MemoryGeoStore

class MemoryGeoStore extends GeoStore {
  import scala.collection.immutable.{TreeMap, TreeSet}
  private var locs: TreeMap[GUID, GeoLocation] = TreeMap()
  private var nonLocSet: TreeSet[GUID] = TreeSet()

  /**
   * Assocate the GUID and a geo location
   */
  def add(guid: GUID, location: GeoLocation): Unit = synchronized {
    remove(guid)
    locs += guid -> location
    if (!location.hasLocation) {
      nonLocSet += guid
    }
  }

  /**
   * Unassocate the GUID and the geo location
   */
  def remove(guid: GUID) = synchronized {
    locs -= guid
    nonLocSet -= guid
  }

  /**
   * Update the location of a given GUID
   */
  def update(guid: GUID, location: GeoLocation): Unit = add(guid, location)

  /**
   * Find a series of locations that are within a range of the
   * specified location
   * @param location location around which we are looking
   * @param radius radius, in miles
   */
  def find(location: GeoLocation,
           radius: Double,
           first: Int,
           max: Int): List[GUID] =
  {
    val set = synchronized{locs}
    set.view.filter{ case (_, loc) => loc.withinMiles(radius, location)}.
    drop(first).take(max).map(_._1).toList
  }

  /**
   * Find the GUIDs that do not have a location
   */
  def findNonLocated(first: Int = 0, max: Int = 200): List[GUID] =
  {
    val set = synchronized{nonLocSet}
    set.view.drop(first).take(max).toList
  }
}

/**
 * The in-memory implementation of the tag store
 */
object MemoryTagStore extends MemoryTagStore 

class MemoryTagStore extends TagStore {
  import scala.collection.immutable.{TreeMap, TreeSet, SortedSet}

  private var tags: TreeMap[Tag, TreeSet[GUID]] = TreeMap()
  private var guids: TreeMap[GUID, Set[Tag]] = TreeMap()

  /**
   * Assocate the GUID and a set of tags
   */
  def add(guid: GUID, tagsToAdd: List[Tag]): Unit = synchronized {
    remove(guid)
    val realTags = TreeSet(tagsToAdd :_*)
    guids += guid -> realTags
    for {
      tag <- realTags
    } tags += tag -> (tags.getOrElse(tag, TreeSet(guid)) + guid)
  }

  /**
   * Unassocate the GUID and the tags
   */
  def remove(guid: GUID): Unit = synchronized {
    guids.get(guid) match {
      case Some(toRemove) =>
        for {
          tag <- toRemove
        } tags += tag -> (tags(tag) - guid)
      case _ =>
    }
  }

  /**
   * Update the tags associated with a given GUID
   */
  def update(guid: GUID, tags: List[Tag]): Unit = add(guid, tags)

  /**
   *Find a set of GUIDs assocaiated with a set of tags
   */
  def find(tagsToFind: List[Tag],
           first: Int = 0, max: Int = 200): List[GUID] =
  {
    val toFind = TreeSet(tagsToFind :_*)
    val tg = synchronized {tags}

    val ret: SortedSet[GUID] = for {
        t <- toFind
        lst <- tg.get(t).toList
        item <- lst
      } yield item

    ret.drop(first).take(max).toList
  }
}
