package org.stth.siak.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class LogKehadiranPesertaKuliah {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private LogPerkuliahan logPerkuliahan;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private Mahasiswa mahasiswa;
	private boolean isHadir;
	public LogKehadiranPesertaKuliah() {
		
	}
	public LogKehadiranPesertaKuliah(LogPerkuliahan log, PesertaKuliah pk){
		this.logPerkuliahan = log;
		this.mahasiswa = pk.getMahasiswa();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LogPerkuliahan getLogPerkuliahan() {
		return logPerkuliahan;
	}
	public void setLogPerkuliahan(LogPerkuliahan logPerkuliahan) {
		this.logPerkuliahan = logPerkuliahan;
	}
	public Mahasiswa getMahasiswa() {
		return mahasiswa;
	}
	public void setMahasiswa(Mahasiswa mahasiswa) {
		this.mahasiswa = mahasiswa;
	}
	public boolean isHadir() {
		return isHadir;
	}
	public void setHadir(boolean isHadir) {
		this.isHadir = isHadir;
	}
	
	public String toString(){
		return mahasiswa.getNama()+" "+isHadir;
	}

}
