package org.stth.siak.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class UserGroup {
	@Id @GeneratedValue
	private int id;
	private String groupName;
	private boolean adminMasterData;
	private boolean adminAkademik;
	private boolean operatorAkademik;
	private boolean adminKeuangan;
	private boolean operatorKeuangan;
	private boolean adminInventaris;
	private boolean operatorInventaris;
	private boolean adminPenelitian;
	private boolean operatorPenelitian;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public boolean isAdminMasterData() {
		return adminMasterData;
	}
	public void setAdminMasterData(boolean adminMasterData) {
		this.adminMasterData = adminMasterData;
	}
	public boolean isOperatorAkademik() {
		return operatorAkademik;
	}
	public void setOperatorAkademik(boolean operatorAkademik) {
		if (operatorAkademik){
			this.adminAkademik = false;
		}
		this.operatorAkademik = operatorAkademik;
	}
	public boolean isOperatorKeuangan() {
		return operatorKeuangan;
	}
	public void setOperatorKeuangan(boolean operatorKeuangan) {
		if (operatorKeuangan){
			adminKeuangan = false;
		}
		this.operatorKeuangan = operatorKeuangan;
	}
	public boolean isOperatorInventaris() {
		return operatorInventaris;
	}
	public void setOperatorInventaris(boolean operatorInventaris) {
		if (operatorInventaris){
			adminInventaris = false;
		}
		this.operatorInventaris = operatorInventaris;
	}
	public boolean isOperatorPenelitian() {
		
		return operatorPenelitian;
	}
	public void setOperatorPenelitian(boolean operatorPenelitian) {
		if (operatorPenelitian){
			setAdminPenelitian(false);
		}
		this.operatorPenelitian = operatorPenelitian;
	}
	public boolean isAdminAkademik() {
		return adminAkademik;
	}
	public void setAdminAkademik(boolean adminAkademik) {
		if (adminAkademik){
			operatorAkademik = false;
		}
		this.adminAkademik = adminAkademik;
	}
	public boolean isAdminKeuangan() {
		return adminKeuangan;
	}
	public void setAdminKeuangan(boolean adminKeuangan) {
		if (adminKeuangan){
			operatorKeuangan = false;
		}
		this.adminKeuangan = adminKeuangan;
	}
	public boolean isAdminInventaris() {
		return adminInventaris;
	}
	public void setAdminInventaris(boolean adminInventaris) {
		if (adminInventaris){
			operatorInventaris = false;
		}
		this.adminInventaris = adminInventaris;
	}
	public boolean isAdminPenelitian() {
		return adminPenelitian;
	}
	public void setAdminPenelitian(boolean adminPenelitian) {
		if (adminPenelitian){
			setOperatorPenelitian(false);
		}
		this.adminPenelitian = adminPenelitian;
	}
	public String toString(){
		return groupName;
	}

}
