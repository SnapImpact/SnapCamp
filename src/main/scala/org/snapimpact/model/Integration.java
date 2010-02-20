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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * @author Dave Angulo
 */
@Entity
@Table(name = "INTEGRATION")
@NamedQueries( {
		@NamedQuery(name = "Integration.findAll", query = "SELECT i FROM Integration i"),
		@NamedQuery(name = "Integration.findById", query = "SELECT i FROM Integration i WHERE i.id = :id"),
		@NamedQuery(name = "Integration.findByUserName", query = "SELECT i FROM Integration i WHERE i.userName = :userName"),
		@NamedQuery(name = "Integration.findByPassword", query = "SELECT i FROM Integration i WHERE i.password = :password") })
public class Integration implements Serializable, IdInterface {
	private static final long	serialVersionUID	= 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "id")
	private String				id;
	@Basic(optional = false)
	@Column(name = "user_name")
	private String				userName;
	@Column(name = "password")
	private String				password;
	@JoinColumn(name = "user_id", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	private IvUser				userId;
	@JoinColumn(name = "network_id", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	private Network				networkId;

	public Integration() {
	}

	public Integration(String id) {
		this.id = id;
	}

	public Integration(String id, String userName) {
		this.id = id;
		this.userName = userName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public IvUser getUserId() {
		return userId;
	}

	public void setUserId(IvUser userId) {
		this.userId = userId;
	}

	public Network getNetworkId() {
		return networkId;
	}

	public void setNetworkId(Network networkId) {
		this.networkId = networkId;
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
		if (!(object instanceof Integration)) {
			return false;
		}
		Integration other = (Integration) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "persistence.Integration[id=" + id + "]";
	}

}
