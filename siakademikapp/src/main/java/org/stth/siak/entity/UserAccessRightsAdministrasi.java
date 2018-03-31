package org.stth.siak.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class UserAccessRightsAdministrasi {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private DosenKaryawan user;
	@Enumerated(EnumType.STRING)
	private ACLAdministrasiEnum acl;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private DosenKaryawan addedBy;
	private Date created;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public DosenKaryawan getUser() {
		return user;
	}
	public void setUser(DosenKaryawan user) {
		this.user = user;
	}
	public ACLAdministrasiEnum getAcl() {
		return acl;
	}
	public void setAcl(ACLAdministrasiEnum acl) {
		this.acl = acl;
	}
	public DosenKaryawan getAddedBy() {
		return addedBy;
	}
	public void setAddedBy(DosenKaryawan addedBy) {
		this.addedBy = addedBy;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	
	

}
