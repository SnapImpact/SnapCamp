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
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author Dave Angulo
 */
@Entity
@Table(name = "INTEREST_AREA")
@NamedQueries( {
		@NamedQuery(name = "InterestArea.findAll", query = "SELECT i FROM InterestArea i"),
		@NamedQuery(name = "InterestArea.findById", query = "SELECT i FROM InterestArea i WHERE i.id = :id"),
		@NamedQuery(name = "InterestArea.findByName", query = "SELECT i FROM InterestArea i WHERE i.name = :name") })
public class InterestArea implements Serializable, IdInterface {
	private static final long				serialVersionUID	= 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "id")
	private String							id;
	@Basic(optional = false)
	@Column(name = "name")
	private String							name;
	@ManyToMany(mappedBy = "interestAreaCollection")
	private Collection<Event>				eventCollection;
	@JoinTable(name = "ORGANIZATION_INTEREST_AREA", joinColumns = { @JoinColumn(name = "interest_area_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "organization_id", referencedColumnName = "id") })
	@ManyToMany
	private Collection<Organization>		organizationCollection;
	@ManyToMany(mappedBy = "interestAreaCollection")
	private Collection<Filter>				filterCollection;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "interestAreaId")
	private Collection<SourceInterestMap>	sourceInterestMapCollection;

	public InterestArea() {
	}

	public InterestArea(String id) {
		this.id = id;
	}

	public InterestArea(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Event> getEventCollection() {
		return eventCollection;
	}

	public void setEventCollection(Collection<Event> eventCollection) {
		this.eventCollection = eventCollection;
	}

	public Collection<Organization> getOrganizationCollection() {
		return organizationCollection;
	}

	public void setOrganizationCollection(Collection<Organization> organizationCollection) {
		this.organizationCollection = organizationCollection;
	}

	public Collection<Filter> getFilterCollection() {
		return filterCollection;
	}

	public void setFilterCollection(Collection<Filter> filterCollection) {
		this.filterCollection = filterCollection;
	}

	public Collection<SourceInterestMap> getSourceInterestMapCollection() {
		return sourceInterestMapCollection;
	}

	public void setSourceInterestMapCollection(
			Collection<SourceInterestMap> sourceInterestMapCollection) {
		this.sourceInterestMapCollection = sourceInterestMapCollection;
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
		if (!(object instanceof InterestArea)) {
			return false;
		}
		InterestArea other = (InterestArea) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "persistence.InterestArea[id=" + id + "]";
	}

}
