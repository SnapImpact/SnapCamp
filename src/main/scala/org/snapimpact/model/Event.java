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
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

/**
 * 
 * @author Dave Angulo
 */
@Entity
@Table(name = "EVENT")
@NamedQueries( {
		@NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"),
		@NamedQuery(name = "Event.findById", query = "SELECT e FROM Event e WHERE e.id = :id"),
		@NamedQuery(name = "Event.findByTitle", query = "SELECT e FROM Event e WHERE e.title = :title"),
		@NamedQuery(name = "Event.findByDescription", query = "SELECT e FROM Event e WHERE e.description = :description"),
		@NamedQuery(name = "Event.findByDuration", query = "SELECT e FROM Event e WHERE e.duration = :duration"),
		@NamedQuery(name = "Event.findByContact", query = "SELECT e FROM Event e WHERE e.contact = :contact"),
		@NamedQuery(name = "Event.findByUrl", query = "SELECT e FROM Event e WHERE e.url = :url"),
		@NamedQuery(name = "Event.findByPhone", query = "SELECT e FROM Event e WHERE e.phone = :phone"),
		@NamedQuery(name = "Event.findByEmail", query = "SELECT e FROM Event e WHERE e.email = :email"),
		@NamedQuery(name = "Event.findBySourceKey", query = "SELECT e FROM Event e WHERE e.sourceKey = :sourceKey"),
		@NamedQuery(name = "Event.findBySourceUrl", query = "SELECT e FROM Event e WHERE e.sourceUrl = :sourceUrl") })
@NamedNativeQuery(
        name = "Event.findNearLocation",
        query = "SELECT e.id, e.title, e.description, e.duration, e.contact, e.url, " +
                "e.phone, e.email, e.source_key, e.source_url, e.source_id FROM Event e " +
                "JOIN Event_Location el on e.id = el.event_id " +
                "JOIN Location l on el.location_id = l.id " +
                "WHERE e.duration < 604800 AND " +
                "e.id IN ( SELECT et.event_id FROM Timestamp t, " +
                "Event_Timestamp et where t.timestamp >= 'today' and t.id = et.timestamp_id ) AND " +
                "l.geom && EXPAND(GeometryFromText(?, 4326), ?) AND " +
                "Distance(GeometryFromText(?, 4326), l.geom) < ? " +
                "ORDER BY ST_Distance_Sphere(GeometryFromText(?, 4326), l.geom) ",
        resultClass = Event.class
        )

public class Event implements Serializable, IdInterface {
	private static final long			serialVersionUID	= 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "id")
	private String						id;
	@Basic(optional = false)
	@Column(name = "title")
	private String						title;
	@Basic(optional = false)
	@Column(name = "description")
	private String						description;
	@Column(name = "duration")
	private Long						duration;
	@Column(name = "contact")
	private String						contact;
	@Column(name = "url")
	private String						url;
	@Column(name = "phone")
	private String						phone;
	@Column(name = "email")
	private String						email;
	@Column(name = "source_Key")
	private String						sourceKey;
	@Column(name = "source_url")
	private String						sourceUrl;
	@JoinTable(name = "EVENT_INTEREST_AREA", joinColumns = { @JoinColumn(name = "EVENT_ID", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "INTEREST_AREA_ID", referencedColumnName = "id") })
	@ManyToMany
	private Collection<InterestArea>	interestAreaCollection;
	@JoinTable(name = "EVENT_TIMESTAMP", joinColumns = { @JoinColumn(name = "EVENT_ID", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "TIMESTAMP_ID", referencedColumnName = "id") })
	@ManyToMany
	private Collection<Timestamp>		timestampCollection;
	@JoinTable(name = "EVENT_LOCATION", joinColumns = { @JoinColumn(name = "EVENT_ID", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "LOCATION_ID", referencedColumnName = "id") })
	@ManyToMany
	private Collection<Location>		locationCollection;
	@JoinTable(name = "EVENT_ORGANIZATION", joinColumns = { @JoinColumn(name = "EVENT_ID", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "ORGANIZATION_ID", referencedColumnName = "id") })
	@ManyToMany
	private Collection<Organization>	organizationCollection;
	@JoinColumn(name = "source_id", referencedColumnName = "id")
	@ManyToOne
	private Source						sourceId;

	public Event() {
	}

	public Event(String id) {
		this.id = id;
	}

	public Event(String id, String title, String description) {
		this.id = id;
		this.title = title;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public Collection<InterestArea> getInterestAreaCollection() {
		return interestAreaCollection;
	}

	public void setInterestAreaCollection(Collection<InterestArea> interestAreaCollection) {
		this.interestAreaCollection = interestAreaCollection;
	}

	public Collection<Timestamp> getTimestampCollection() {
		return timestampCollection;
	}

	public void setTimestampCollection(Collection<Timestamp> timestampCollection) {
		this.timestampCollection = timestampCollection;
	}

	public Collection<Location> getLocationCollection() {
		return locationCollection;
	}

	public void setLocationCollection(Collection<Location> locationCollection) {
		this.locationCollection = locationCollection;
	}

	public Collection<Organization> getOrganizationCollection() {
		return organizationCollection;
	}

	public void setOrganizationCollection(Collection<Organization> organizationCollection) {
		this.organizationCollection = organizationCollection;
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
		if (!(object instanceof Event)) {
			return false;
		}
		Event other = (Event) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "persistence.Event[id=" + id + "]";
	}

}
