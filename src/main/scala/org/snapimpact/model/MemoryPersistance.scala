package org.snapimpact
package model

import org.snapimpact.etl.model.dto.VolunteerOpportunity

import net.liftweb._
import util.Helpers
import common._

/**
 * This file contains an implementation of in-memory persistance
 *
 * @author dpp
 */

private object MemoryOpportunityStore extends MemoryOpportunityStore

private class MemoryOpportunityStore extends OpportunityStore {
  private var data: Map[GUID, VolunteerOpportunity] = Map()

  /**
   * Add a record to the backing store and get a GUID
   * the represents the record
   */
  def create(record: VolunteerOpportunity): GUID = synchronized {
    val guid = GUID(Helpers.nextFuncName)

    data += guid -> record

    guid
  }

  /**
   * read the GUID from the backing store
   */
  def read(guid: GUID): Option[VolunteerOpportunity] = synchronized {
    data.get(guid)
  }

  /**
   * Read a set of GUIDs from the backing store
   */
  def read(guids: List[(GUID, Double)]): 
  List[(GUID, VolunteerOpportunity, Double)] = synchronized {
    guids.flatMap{ case (g, rank) => data.get(g).map(f => (g, f, rank))}
  }

  /**
   * Update the record
   */
  def update(guid: GUID, record: VolunteerOpportunity) = synchronized {
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
private object MemoryGeoStore extends MemoryGeoStore

private class MemoryGeoStore extends GeoStore {
  import scala.collection.immutable.{TreeMap, TreeSet}
  private var locs: TreeMap[GUID, List[GeoLocation]] = TreeMap()
  private var nonLocSet: TreeSet[GUID] = TreeSet()

  /**
   * Assocate the GUID and a geo location
   */
  def add(guid: GUID, location: List[GeoLocation]): Unit = synchronized {
    remove(guid)
    locs += guid -> location
    if (location.find(!_.hasLocation).isDefined) {
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
  def update(guid: GUID, location: List[GeoLocation]): Unit = 
    add(guid, location)

  /**
   * Find a series of locations that are within a range of the
   * specified location
   * @param location location around which we are looking
   * @param radius radius, in miles
   */
  def find(location: GeoLocation,
           radius: Double,
           first: Int,
           max: Int): List[(GUID, Double)] =
	     {
	       val set = synchronized{locs}
               
               val view: List[(GUID, Double)] = 
                 for {
                   (guid, locs) <- set.toList
                   loc <- locs
                   distance <- loc.distanceFrom(location) if distance <= radius
                 } yield guid -> distance

               view.sortWith(_._2 < _._2).drop(first).take(max)
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
private object MemoryTagStore extends MemoryTagStore

private class MemoryTagStore extends TagStore {
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

private object MemoryLuceneStore extends MemoryLuceneStore

/**
 * THe interface for storing stuff in a search engine
 */
private class MemoryLuceneStore extends SearchStore {
  private val guid = GUID.create

  override def toString = "MemoryLuceneStore "+guid

  import java.io.IOException;
  import java.io.StringReader;

  import org.apache.lucene.search._
  import org.apache.lucene.document._
  import org.apache.lucene.search._
  import org.apache.lucene.store._
  import org.apache.lucene.index._
  import org.apache.lucene.analysis.standard._
  import org.apache.lucene.util.Version
  import org.apache.lucene.queryParser.QueryParser


  /*
   import org.apache.lucene.queryParser.QueryParser;
   import org.apache.lucene.queryParser.ParseException;
   import org.apache.lucene.analysis.standard.StandardAnalyzer;
   */

  private val idx = {
    val rd = new RAMDirectory()
    val writer = new IndexWriter(rd, new StandardAnalyzer(Version.LUCENE_30),
                                 true, IndexWriter.MaxFieldLength.UNLIMITED)
    writer.optimize
    writer.commit
    writer.close

    rd
  }

  private lazy val writer = new IndexWriter(idx,
                                            new StandardAnalyzer(Version.LUCENE_30),
                                            false, IndexWriter.MaxFieldLength.UNLIMITED)
  
  private var writeCnt = 0L



  private def write[T](f: IndexWriter => T): T = synchronized {
    val ret = f(writer)
    writeCnt += 1
    if (writeCnt % 1000L == 0) {
      writer.optimize
    }

    writer.commit

    ret
  }

  /**
   * Assocate the GUID and an item.
   * @param guid the GUID to associate
   * @param item the item to associate with the GUID
   * @splitter a function that returns the Strings and types of String (e.g., description,
   */
  def add[T](guid: GUID, item: T)(implicit splitter: T => Seq[(String, Option[String])]): Unit = {
    val doc = new Document()

    doc.add(new Field("guid", guid.guid, Field.Store.YES, Field.Index.ANALYZED))

    splitter(item).foreach {
      case (value, name) => doc.add(new Field(name getOrElse "body", value,
					      Field.Store.YES, Field.Index.ANALYZED))
    }

    write(_.addDocument(doc))
  }

  /**
   * Unassocate the GUID and words
   */
  def remove(guid: GUID): Unit = {
    val reader = IndexReader.open(idx)
    reader.deleteDocuments(new Term("guid", guid.guid))
    reader.close()
  }

  /**
   * Assocate the GUID and a set of words
   */
  def update[T](guid: GUID, item: T)(implicit splitter: T => Seq[(String, Option[String])]): Unit = {
    remove(guid)
    add(guid, item)(splitter)
  }


  /**
   * Find a set of GUIDs assocaiated with a search string, optionally
   * specifing the subset of GUIDs to search and the number of results to
   * return
   */
  def find(search: String,
           first: Int = 0, max: Int = 200,
           inSet: Option[Seq[GUID]] = None): List[(GUID, Double)]
  = {
    try {
      val qp = new QueryParser(Version.LUCENE_CURRENT, "body",
			       new StandardAnalyzer(Version.LUCENE_30))

      qp.setAllowLeadingWildcard(true)
      val q = qp.parse(search)
      
      val collector = TopScoreDocCollector.create(max + first, true)
      
      val searcher = new IndexSearcher(idx, true)
      
      searcher.search(q, collector)
      
      val hits = collector.topDocs().scoreDocs.drop(first)

      val ret = for {
	h <- hits.toList
	doc <- Box !! searcher.doc(h.doc, new MapFieldSelector("guid"))
	guid <- Box !! doc.getField("guid")
	value <- Box !! guid.stringValue
      } yield GUID(value) -> h.score.toDouble

      ret
    } catch {
      case pe: org.apache.lucene.queryParser.ParseException => Nil
    }
  }
}

