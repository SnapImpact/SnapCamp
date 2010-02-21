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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author Dave Angulo
 */
@Entity
@Table(name = "SOURCE")
@NamedQueries({
    @NamedQuery(name = "Source.findAll", query = "SELECT s FROM Source s"),
    @NamedQuery(name = "Source.findById", query = "SELECT s FROM Source s WHERE s.id = :id"),
    @NamedQuery(name = "Source.findByName", query = "SELECT s FROM Source s WHERE s.name = :name"),
    @NamedQuery(name = "Source.findByEtlClass", query = "SELECT s FROM Source s WHERE s.etlClass = :etlClass")})
public class Source implements Serializable, IdInterface {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "etl_class")
    private String etlClass;
    @Column(name = "url")
    private String url;
    @Column(name = "api_url")
    private String apiUrl;
    @OneToMany(mappedBy = "sourceId")
    private Collection<Organization> organizationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sourceId")
    private Collection<SourceInterestMap> sourceInterestMapCollection;
    @OneToMany(mappedBy = "sourceId")
    private Collection<Event> eventCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sourceId")
    private Collection<SourceOrgTypeMap> sourceOrgTypeMapCollection;
    @OneToMany(mappedBy = "sourceId")
    private Collection<Api> apiCollection;

    public Source() {
    }

    public Source(String id) {
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

    public String getEtlClass() {
        return etlClass;
    }

    public void setEtlClass(String etlClass) {
        this.etlClass = etlClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public Collection<Organization> getOrganizationCollection() {
        return organizationCollection;
    }

    public void setOrganizationCollection(Collection<Organization> organizationCollection) {
        this.organizationCollection = organizationCollection;
    }

    public Collection<SourceInterestMap> getSourceInterestMapCollection() {
        return sourceInterestMapCollection;
    }

    public void setSourceInterestMapCollection(
            Collection<SourceInterestMap> sourceInterestMapCollection) {
        this.sourceInterestMapCollection = sourceInterestMapCollection;
    }

    public Collection<Event> getEventCollection() {
        return eventCollection;
    }

    public void setEventCollection(Collection<Event> eventCollection) {
        this.eventCollection = eventCollection;
    }

    public Collection<SourceOrgTypeMap> getSourceOrgTypeMapCollection() {
        return sourceOrgTypeMapCollection;
    }

    public void setSourceOrgTypeMapCollection(
            Collection<SourceOrgTypeMap> sourceOrgTypeMapCollection) {
        this.sourceOrgTypeMapCollection = sourceOrgTypeMapCollection;
    }

    public Collection<Api> getApiCollection() {
        return apiCollection;
    }

    public void setApiCollection(Collection<Api> apiCollection) {
        this.apiCollection = apiCollection;
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
        if (!(object instanceof Source)) {
            return false;
        }
        Source other = (Source) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistence.Source[id=" + id + "]";
    }
}
