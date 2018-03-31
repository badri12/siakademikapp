package org.stth.siak.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class RiwayatPekerjaan {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne(cascade=CascadeType.REFRESH)
	@PrimaryKeyJoinColumn
	private Mahasiswa mahasiswa;
	private String instansi;
	private String alamatInstansi;
	private String lamaMenungguKerja;
	private String sumberPekerjaan;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Mahasiswa getMahasiswa() {
		return mahasiswa;
	}
	public void setMahasiswa(Mahasiswa mahasiswa) {
		this.mahasiswa = mahasiswa;
	}
	public String getInstansi() {
		return instansi;
	}
	public void setInstansi(String instansi) {
		this.instansi = instansi;
	}
	public String getAlamatInstansi() {
		return alamatInstansi;
	}
	public void setAlamatInstansi(String alamatInstansi) {
		this.alamatInstansi = alamatInstansi;
	}
	public String getLamaMenungguKerja() {
		return lamaMenungguKerja;
	}
	public void setLamaMenungguKerja(String lamaMenungguKerja) {
		this.lamaMenungguKerja = lamaMenungguKerja;
	}
	public String getSumberPekerjaan() {
		return sumberPekerjaan;
	}
	public void setSumberPekerjaan(String sumberPekerjaan) {
		this.sumberPekerjaan = sumberPekerjaan;
	}
	
	
}
