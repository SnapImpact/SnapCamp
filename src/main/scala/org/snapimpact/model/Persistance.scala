package org.snapimpact
package model

import org.snapimpact.etl.model.dto.FootprintFeed

/**
 * This file defines interfaces for the storage mechanism.  It
 * does not specific the actual storage mechansim.
 */

final case class GUID(guid: String)

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
