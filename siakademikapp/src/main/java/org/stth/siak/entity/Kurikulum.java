package org.stth.siak.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class Kurikulum {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
    private ProgramStudi prodi;
	private String nama;
	private int tahunMulai;
	private int sksLulus;
    private boolean aktif;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ProgramStudi getProdi() {
		return prodi;
	}
	public void setProdi(ProgramStudi prodi) {
		this.prodi = prodi;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public int getSksLulus() {
		return sksLulus;
	}
	public void setSksLulus(int sksLulus) {
		this.sksLulus = sksLulus;
	}
	public int getTahunMulai() {
		return tahunMulai;
	}
	public void setTahunMulai(int tahunMulai) {
		this.tahunMulai = tahunMulai;
	}
	public boolean isAktif() {
		return aktif;
	}
	public void setAktif(boolean aktif) {
		this.aktif = aktif;
	}
	public String toString(){
		return "Kurikulum prodi "+prodi.getNama()+" "+tahunMulai;
	}

}
