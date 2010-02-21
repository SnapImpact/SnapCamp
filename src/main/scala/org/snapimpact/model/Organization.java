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
@Table(name = "ORGANIZATION")
@NamedQueries( {
		@NamedQuery(name = "Organization.findAll", query = "SELECT o FROM Organization o"),
		@NamedQuery(name = "Organization.findById", query = "SELECT o FROM Organization o WHERE o.id = :id"),
		@NamedQuery(name = "Organization.findByName", query = "SELECT o FROM Organization o WHERE o.name = :name"),
		@NamedQuery(name = "Organization.findByDescription", query = "SELECT o FROM Organization o WHERE o.description = :description"),
		@NamedQuery(name = "Organization.findByPhone", query = "SELECT o FROM Organization o WHERE o.phone = :phone"),
		@NamedQuery(name = "Organization.findByEmail", query = "SELECT o FROM Organization o WHERE o.email = :email"),
		@NamedQuery(name = "Organization.findByUrl", query = "SELECT o FROM Organization o WHERE o.url = :url"),
		@NamedQuery(name = "Organization.findBySourceKey", query = "SELECT o FROM Organization o WHERE o.sourceKey = :sourceKey"),
		@NamedQuery(name = "Organization.findBySourceUrl", query = "SELECT o FROM Organization o WHERE o.sourceUrl = :sourceUrl") })
public class Organization implements Serializable, IdInterface {
	private static final long			serialVersionUID	= 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "id")
	private String						id;
	@Basic(optional = false)
	@Column(name = "name")
	private String						name;
	@Column(name = "description")
	private String						description;
	@Column(name = "phone")
	private String						phone;
	@Column(name = "email")
	private String						email;
	@Column(name = "url")
	private String						url;
	@Column(name = "source_key")
	private String						sourceKey;
	@Column(name = "source_url")
	private String						sourceUrl;
	@ManyToMany(mappedBy = "organizationCollection")
	private Collection<Location>		locationCollection;
	@ManyToMany(mappedBy = "organizationCollection")
	private Collection<InterestArea>	interestAreaCollection;
	@ManyToMany(mappedBy = "organizationCollection")
	private Collection<Event>			eventCollection;
	@JoinColumn(name = "organization_type_id", referencedColumnName = "id")
	@ManyToOne
	private OrganizationType			organizationTypeId;
	@JoinColumn(name = "source_id", referencedColumnName = "id")
	@ManyToOne
	private Source						sourceId;

	public Organization() {
	}

	public Organization(String id) {
		this.id = id;
	}

	public Organization(String id, String name) {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSourceKey() {
		return sourceKey;
	}

	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public Collection<Location> getLocationCollection() {
		return locationCollection;
	}

	public void setLocationCollection(Collection<Location> locationCollection) {
		this.locationCollection = locationCollection;
	}

	public Collection<InterestArea> getInterestAreaCollection() {
		return interestAreaCollection;
	}

	public void setInterestAreaCollection(Collection<InterestArea> interestAreaCollection) {
		this.interestAreaCollection = interestAreaCollection;
	}

	public Collection<Event> getEventCollection() {
		return eventCollection;
	}

	public void setEventCollection(Collection<Event> eventCollection) {
		this.eventCollection = eventCollection;
	}

	public OrganizationType getOrganizationTypeId() {
		return organizationTypeId;
	}

	public void setOrganizationTypeId(OrganizationType organizationTypeId) {
		this.organizationTypeId = organizationTypeId;
	}

	public Source getSourceId() {
		return sourceId;
	}

	public void setSourceId(Source sourceId) {
		this.sourceId = sourceId;
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
		if (!(object instanceof Organization)) {
			return false;
		}
		Organization other = (Organization) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "persistence.Organization[id=" + id + "]";
	}

}
