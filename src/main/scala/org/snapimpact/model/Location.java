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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedNativeQueries;
import javax.persistence.Table;

/**
 * 
 * @author Dave Angulo
 */
@Entity
@Table(name = "LOCATION")
@NamedQueries( {
		@NamedQuery(name = "Location.findAll", query = "SELECT l FROM Location l"),
        @NamedQuery(name = "Location.findByNull", query = "SELECT l FROM Location l where l.street is null and l.city is null and l.state is null and l.zip is null"),
		@NamedQuery(name = "Location.findById", query = "SELECT l FROM Location l WHERE l.id = :id"),
		@NamedQuery(name = "Location.findByStreet", query = "SELECT l FROM Location l WHERE l.street = :street"),
		@NamedQuery(name = "Location.findByCity", query = "SELECT l FROM Location l WHERE l.city = :city"),
		@NamedQuery(name = "Location.findByState", query = "SELECT l FROM Location l WHERE l.state = :state"),
		@NamedQuery(name = "Location.findByZip", query = "SELECT l FROM Location l WHERE l.zip = :zip"),
		@NamedQuery(name = "Location.findByLocation", query = "SELECT l FROM Location l WHERE l.location = :location"),
		@NamedQuery(name = "Location.findByLatitude", query = "SELECT l FROM Location l WHERE l.latitude = :latitude"),
		@NamedQuery(name = "Location.findByLongitude", query = "SELECT l FROM Location l WHERE l.longitude = :longitude"),
		@NamedQuery(name = "Location.findByStreetZip", query = "SELECT l FROM Location l WHERE l.street = :street and l.zip = :zip"),
                @NamedQuery(name = "Location.findByZipNull", query = "SELECT l FROM Location l WHERE l.zip = :zip and l.street is null"),
                @NamedQuery(name = "Location.findNullLatLon", query = "SELECT l FROM Location l WHERE l.latitude is null or l.longitude is null")
                
})

@NamedNativeQueries( {
    @NamedNativeQuery(name = "Location.updateGeom", query = "UPDATE Location SET geom = PointFromText('POINT(' || latitude || ' ' || longitude || ')',4326)"),
    @NamedNativeQuery(name = "Location.dropIndex", query = "DROP INDEX IF EXISTS location_geom_idx"),
    @NamedNativeQuery(name = "Location.createIndex", query = "CREATE INDEX location_geom_idx on location USING GIST ( geom GIST_GEOMETRY_OPS )")
})

public class Location implements Serializable, IdInterface {
	private static final long			serialVersionUID	= 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "id")
	private String						id;
	@Column(name = "street")
	private String						street;
	@Column(name = "city")
	private String						city;
	@Column(name = "state")
	private String						state;
	@Column(name = "zip")
	private String						zip;
        @Column(name = "country")
        private String                                          country;
	@Column(name = "location")
	private String						location;
	@Column(name = "latitude")
	private String						latitude;
	@Column(name = "longitude")
	private String						longitude;
	@JoinTable(name = "ORGANIZATION_LOCATION", joinColumns = { @JoinColumn(name = "location_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "organization_id", referencedColumnName = "id") })
	@ManyToMany
	private Collection<Organization>	organizationCollection;
	@ManyToMany(mappedBy = "locationCollection")
	private Collection<Event>			eventCollection;

	public Location() {
	}

	public Location(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public Collection<Organization> getOrganizationCollection() {
		return organizationCollection;
	}

	public void setOrganizationCollection(Collection<Organization> organizationCollection) {
		this.organizationCollection = organizationCollection;
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
		if (!(object instanceof Location)) {
			return false;
		}
		Location other = (Location) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "persistence.Location[id=" + id + "]";
	}

}
