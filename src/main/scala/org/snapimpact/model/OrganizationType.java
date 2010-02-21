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
@Table(name = "ORGANIZATION_TYPE")
@NamedQueries( {
		@NamedQuery(name = "OrganizationType.findAll", query = "SELECT o FROM OrganizationType o"),
		@NamedQuery(name = "OrganizationType.findById", query = "SELECT o FROM OrganizationType o WHERE o.id = :id"),
		@NamedQuery(name = "OrganizationType.findByName", query = "SELECT o FROM OrganizationType o WHERE o.name = :name") })
public class OrganizationType implements Serializable, IdInterface {
	private static final long				serialVersionUID	= 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "id")
	private String							id;
	@Column(name = "name")
	private String							name;
	@ManyToMany(mappedBy = "organizationTypeCollection")
	private Collection<Filter>				filterCollection;
	@OneToMany(mappedBy = "organizationTypeId")
	private Collection<Organization>		organizationCollection;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "organizationTypeId")
	private Collection<SourceOrgTypeMap>	sourceOrgTypeMapCollection;

	public OrganizationType() {
	}

	public OrganizationType(String id) {
		this.id = id;
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

	public Collection<Filter> getFilterCollection() {
		return filterCollection;
	}

	public void setFilterCollection(Collection<Filter> filterCollection) {
		this.filterCollection = filterCollection;
	}

	public Collection<Organization> getOrganizationCollection() {
		return organizationCollection;
	}

	public void setOrganizationCollection(Collection<Organization> organizationCollection) {
		this.organizationCollection = organizationCollection;
	}

	public Collection<SourceOrgTypeMap> getSourceOrgTypeMapCollection() {
		return sourceOrgTypeMapCollection;
	}

	public void setSourceOrgTypeMapCollection(
			Collection<SourceOrgTypeMap> sourceOrgTypeMapCollection) {
		this.sourceOrgTypeMapCollection = sourceOrgTypeMapCollection;
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
		if (!(object instanceof OrganizationType)) {
			return false;
		}
		OrganizationType other = (OrganizationType) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "persistence.OrganizationType[id=" + id + "]";
	}

}
