package org.stth.siak.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.stth.siak.jee.ui.eis.AccessControlList;

@Entity
public class DosenAccessRights {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private DosenKaryawan dosenKaryawan;
	@Enumerated(EnumType.STRING)
	private AccessControlList acl;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public DosenKaryawan getDosenKaryawan() {
		return dosenKaryawan;
	}
	public void setDosenKaryawan(DosenKaryawan dosenKaryawan) {
		this.dosenKaryawan = dosenKaryawan;
	}
	public AccessControlList getAcl() {
		return acl;
	}
	public void setAcl(AccessControlList acl) {
		this.acl = acl;
	}

	
}
