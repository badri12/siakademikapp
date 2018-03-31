package org.stth.siak.entity;

import java.util.Comparator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.Id;

import org.stth.siak.enumtype.JenisMataKuliah;
import org.stth.siak.enumtype.Semester;
@Entity
public class MataKuliahRencanaStudi implements Comparable<MataKuliahRencanaStudi>, Comparator<MataKuliahRencanaStudi>{
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne (cascade= CascadeType.REFRESH)
	private MataKuliah mataKuliah;
	@ManyToOne (cascade= CascadeType.REFRESH)
	private ProgramStudi prodi;
	@Enumerated (EnumType.STRING)
	private Semester semester;
	private int semesterBuka;
	private String tahunAjaran;
	private JenisMataKuliah jenis;
	public MataKuliahRencanaStudi(){
		
	}
	public MataKuliahRencanaStudi(MataKuliahKurikulum mataKuliahKurikulum,
			Semester semester, String tahunAjaran) {
		super();
		this.mataKuliah = mataKuliahKurikulum.getMataKuliah();
		this.prodi = mataKuliahKurikulum.getKurikulum().getProdi();
		this.semester = semester;
		this.setSemesterBuka(mataKuliahKurikulum.getSemesterBuka());
		this.tahunAjaran = tahunAjaran;
		this.setJenis(mataKuliahKurikulum.getJenis());
	}
	public MataKuliahRencanaStudi(MataKuliah mataKuliah,ProgramStudi prodi,int semesterBuka,
			Semester semester, String tahunAjaran, JenisMataKuliah jenis) {
		super();
		this.mataKuliah = mataKuliah;
		this.prodi = prodi;
		this.semester = semester;
		this.semesterBuka = semesterBuka;
		this.tahunAjaran = tahunAjaran;
		this.jenis = jenis;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getSemesterBuka() {
		return semesterBuka;
	}
	public JenisMataKuliah getJenis() {
		return jenis;
	}
	public void setJenis(JenisMataKuliah jenis) {
		this.jenis = jenis;
	}
	public void setSemesterBuka(int semesterBuka) {
		this.semesterBuka = semesterBuka;
	}
	public MataKuliah getMataKuliah() {
		return mataKuliah;
	}
	public void setMataKuliah(MataKuliah mataKuliah) {
		this.mataKuliah = mataKuliah;
	}
	public ProgramStudi getProdi() {
		return prodi;
	}
	public void setProdi(ProgramStudi prodi) {
		this.prodi = prodi;
	}
	public Semester getSemester() {
		return semester;
	}
	public void setSemester(Semester semester) {
		this.semester = semester;
	}
	public String getTahunAjaran() {
		return tahunAjaran;
	}
	public void setTahunAjaran(String tahunAjaran) {
		this.tahunAjaran = tahunAjaran;
	}
	@Override
	public int compare(MataKuliahRencanaStudi o1, MataKuliahRencanaStudi o2) {
		return o1.compareTo(o2);
	}
	@Override
	public int compareTo(MataKuliahRencanaStudi o) {
		if (semesterBuka!=o.semesterBuka){
			return semesterBuka - o.semesterBuka;
		} else {
			return mataKuliah.compareTo(o.mataKuliah);
		}
	}
}
