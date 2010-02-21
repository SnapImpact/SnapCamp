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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * @author Dave Angulo
 */
@Entity
@Table(name = "FILTER")
@NamedQueries( {
		@NamedQuery(name = "Filter.findAll", query = "SELECT f FROM Filter f"),
		@NamedQuery(name = "Filter.findById", query = "SELECT f FROM Filter f WHERE f.id = :id"),
		@NamedQuery(name = "Filter.findByLatitude", query = "SELECT f FROM Filter f WHERE f.latitude = :latitude"),
		@NamedQuery(name = "Filter.findByLongitude", query = "SELECT f FROM Filter f WHERE f.longitude = :longitude") })
public class Filter implements Serializable, IdInterface {
	private static final long				serialVersionUID	= 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "id")
	private String							id;
	@Column(name = "latitude")
	private String							latitude;
	@Column(name = "longitude")
	private String							longitude;
	@JoinTable(name = "FILTER_ORGANIZATION_TYPE", joinColumns = { @JoinColumn(name = "filter_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "organization_type_id", referencedColumnName = "id") })
	@ManyToMany
	private Collection<OrganizationType>	organizationTypeCollection;
	@JoinTable(name = "FILTER_INTEREST_AREA", joinColumns = { @JoinColumn(name = "filter_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "interest_area_id", referencedColumnName = "id") })
	@ManyToMany
	private Collection<InterestArea>		interestAreaCollection;
	@JoinColumn(name = "distance_id", referencedColumnName = "id")
	@ManyToOne
	private Distance						distanceId;
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private IvUser							userId;
	@JoinColumn(name = "timeframe_id", referencedColumnName = "id")
	@ManyToOne
	private Timeframe						timeframeId;

	public Filter() {
	}

	public Filter(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Collection<OrganizationType> getOrganizationTypeCollection() {
		return organizationTypeCollection;
	}

	public void setOrganizationTypeCollection(
			Collection<OrganizationType> organizationTypeCollection) {
		this.organizationTypeCollection = organizationTypeCollection;
	}

	public Collection<InterestArea> getInterestAreaCollection() {
		return interestAreaCollection;
	}

	public void setInterestAreaCollection(Collection<InterestArea> interestAreaCollection) {
		this.interestAreaCollection = interestAreaCollection;
	}

	public Distance getDistanceId() {
		return distanceId;
	}

	public void setDistanceId(Distance distanceId) {
		this.distanceId = distanceId;
	}

	public IvUser getUserId() {
		return userId;
	}

	public void setUserId(IvUser userId) {
		this.userId = userId;
	}

	public Timeframe getTimeframeId() {
		return timeframeId;
	}

	public void setTimeframeId(Timeframe timeframeId) {
		this.timeframeId = timeframeId;
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
		if (!(object instanceof Filter)) {
			return false;
		}
		Filter other = (Filter) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "persistence.Filter[id=" + id + "]";
	}

}
