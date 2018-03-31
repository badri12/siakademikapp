package org.stth.siak.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class Konfigurasi {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String namaKonfigurasi;
	private String nilaiKonfigurasi;
	public static final String SKS_MAX = "max sks";
	public static final String KRS_SEM = "krs sem";
	public static final String KRS_OPEN = "krs open";
	public static final String KRS_TA = "krs ta";
	public static final String CUR_SEM = "cur sem";
	public static final String CUR_TA = "cur ta";
	public static final String UPLOAD_DIR = "upl dir";
	
	public Konfigurasi(){
		
	}
	public Konfigurasi(String nama, String nilai) {
		namaKonfigurasi = nama;
		nilaiKonfigurasi = nilai;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNamaKonfigurasi() {
		return namaKonfigurasi;
	}
	public void setNamaKonfigurasi(String namaKonfigurasi) {
		this.namaKonfigurasi = namaKonfigurasi;
	}
	public String getNilaiKonfigurasi() {
		return nilaiKonfigurasi;
	}
	public void setNilaiKonfigurasi(String nilaiKonfigurasi) {
		this.nilaiKonfigurasi = nilaiKonfigurasi;
	}
	
	
}
