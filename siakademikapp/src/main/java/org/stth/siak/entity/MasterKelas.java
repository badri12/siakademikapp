package org.stth.siak.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class MasterKelas extends BasicEntity{
	@Id @GeneratedValue
	private int id;
	private int angkatan;
	private String kode;
	private String keterangan;
	@ManyToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
	private ProgramStudi prodi;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAngkatan() {
		return angkatan;
	}
	public void setAngkatan(int angkatan) {
		this.angkatan = angkatan;
	}
	public String getKode() {
		return kode;
	}
	public void setKode(String kode) {
		this.kode = kode;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public ProgramStudi getProdi() {
		return prodi;
	}
	public void setProdi(ProgramStudi prodi) {
		this.prodi = prodi;
	}
	public String toString(){
		return kode+" angkatan "+angkatan;
	}

}
