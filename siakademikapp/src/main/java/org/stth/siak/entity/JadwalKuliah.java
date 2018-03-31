package org.stth.siak.entity;

import java.util.Comparator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class JadwalKuliah implements Comparable<JadwalKuliah>,Comparator<JadwalKuliah>{
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private KelasPerkuliahan kelasPerkuliahan;
	private Hari hari;
	private int waktuMulai;
	private int waktuBerakhir;
	private String ruangan;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private DosenKaryawan entryOleh;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public KelasPerkuliahan getKelasPerkuliahan() {
		return kelasPerkuliahan;
	}
	public void setKelasPerkuliahan(KelasPerkuliahan kelasPerkuliahan) {
		this.kelasPerkuliahan = kelasPerkuliahan;
	}
	public Hari getHari() {
		return hari;
	}
	public void setHari(Hari hari) {
		this.hari = hari;
	}
	public int getWaktuMulai() {
		return waktuMulai;
	}
	public void setWaktuMulai(int waktuMulai) {
		this.waktuMulai = waktuMulai;
	}
	public int getWaktuBerakhir() {
		return waktuBerakhir;
	}
	public void setWaktuBerakhir(int waktuBerakhir) {
		this.waktuBerakhir = waktuBerakhir;
	}
	public String getRuangan() {
		return ruangan;
	}
	public void setRuangan(String ruangan) {
		this.ruangan = ruangan;
	}
	public DosenKaryawan getEntryOleh() {
		return entryOleh;
	}
	public void setEntryOleh(DosenKaryawan entryOleh) {
		this.entryOleh = entryOleh;
	}
	public String getHariJam(){
		return hari+" "+waktuMulai+" s/d "+waktuBerakhir;
	}
	
	@Override
	public int compare(JadwalKuliah o1, JadwalKuliah o2) {
		return o1.compareTo(o2);
	}
	@Override
	public int compareTo(JadwalKuliah o) {
		if (hari!=o.hari){
			return this.hari.compareTo(o.hari);
		}
		if (waktuMulai!=o.waktuMulai){
			return this.waktuMulai - o.waktuMulai;
		}
		return kelasPerkuliahan.compareTo(o.kelasPerkuliahan);
	}
	
	

}
