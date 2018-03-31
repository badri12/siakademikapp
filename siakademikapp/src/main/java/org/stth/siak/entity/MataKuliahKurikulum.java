package org.stth.siak.entity;

import java.util.Comparator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.stth.siak.enumtype.JenisMataKuliah;
import org.stth.siak.enumtype.Semester;
@Entity
public class MataKuliahKurikulum implements Comparator<MataKuliahKurikulum>,Comparable<MataKuliahKurikulum>{
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private Kurikulum kurikulum;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private MataKuliah mataKuliah;
	private int semesterBuka;
	@Enumerated(EnumType.STRING)
	private JenisMataKuliah jenis;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Kurikulum getKurikulum() {
		return kurikulum;
	}

	public void setKurikulum(Kurikulum kurikulum) {
		this.kurikulum = kurikulum;
	}

	public MataKuliah getMataKuliah() {
		return mataKuliah;
	}

	public void setMataKuliah(MataKuliah mataKuliah) {
		this.mataKuliah = mataKuliah;
	}

	public int getSemesterBuka() {
		return semesterBuka;
	}

	public void setSemesterBuka(int semesterBuka) {
		this.semesterBuka = semesterBuka;
	}

	public JenisMataKuliah getJenis() {
		return jenis;
	}

	public void setJenis(JenisMataKuliah jenis) {
		this.jenis = jenis;
	}

	public String toString(){
		return mataKuliah.getKode()+" "+mataKuliah.getNama();
	}
	public Semester getSemesterTipe(){
		int i = semesterBuka % 2;
		if (i==0){
			return Semester.GENAP;
		}
		return Semester.GANJIL;
	}

	@Override
	public int compareTo(MataKuliahKurikulum o) {
		if (this.semesterBuka!=o.semesterBuka){
			return Integer.compare(this.semesterBuka, o.semesterBuka);
		} else {
			return this.getMataKuliah().compareTo(o.getMataKuliah());
		}
	}

	@Override
	public int compare(MataKuliahKurikulum o1, MataKuliahKurikulum o2) {
		return o1.compareTo(o2);
	}
	
	

}
