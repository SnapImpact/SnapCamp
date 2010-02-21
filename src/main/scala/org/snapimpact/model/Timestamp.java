/*
 *  Copyright (c) 2008 Boulder Community Foundation - iVolunteer
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.snapimpact.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @author Dave Angulo
 */
@Entity
@Table(name = "TIMESTAMP")
@NamedQueries( {
		@NamedQuery(name = "Timestamp.findAll", query = "SELECT t FROM Timestamp t"),
		@NamedQuery(name = "Timestamp.findById", query = "SELECT t FROM Timestamp t WHERE t.id = :id"),
		@NamedQuery(name = "Timestamp.findByTimestamp", query = "SELECT t FROM Timestamp t WHERE t.timestamp = :timestamp") })
@NamedNativeQueries( {
    @NamedNativeQuery(name = "Timestamp.deleteOld", query = "DELETE FROM Timestamp where timestamp < now()")
})                       
public class Timestamp implements Serializable, IdInterface {
	private static final long	serialVersionUID	= 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "id")
	private String				id;
	@Basic(optional = false)
	@Column(name = "timestamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				timestamp;
	@ManyToMany(mappedBy = "timestampCollection")
	private Collection<Event>	eventCollection;

	public Timestamp() {
	}

	public Timestamp(String id) {
		this.id = id;
	}

	public Timestamp(String id, Date timestamp) {
		this.id = id;
		this.timestamp = timestamp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Collection<Event> getEventCollection() {
		return eventCollection;
	}

	public void setEventCollection(Collection<Event> eventCollection) {
		this.eventCollection = eventCollection;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Timestamp)) {
			return false;
		}
		Timestamp other = (Timestamp) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "persistence.Timestamp[id=" + id + "]";
	}

}
