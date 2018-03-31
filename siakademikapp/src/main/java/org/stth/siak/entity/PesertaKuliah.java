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
public class PesertaKuliah implements Comparable<PesertaKuliah>, Comparator <PesertaKuliah> {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private Mahasiswa mahasiswa;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private KelasPerkuliahan kelasPerkuliahan;
	private String nilai;
	private Double nilaiAngka;
	private String copiedKodeMatkul;
	private String copiedNamaMatkul;
	private int copiedSKSMatkul;
	
	public PesertaKuliah(){
		
	}
	public PesertaKuliah(Mahasiswa mhs, KelasPerkuliahan kelas){
		mahasiswa = mhs;
		setKelasPerkuliahan(kelas);
	}
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
	public KelasPerkuliahan getKelasPerkuliahan() {
		return kelasPerkuliahan;
	}
	public void setKelasPerkuliahan(KelasPerkuliahan kelasPerkuliahan) {
		this.kelasPerkuliahan = kelasPerkuliahan;
		if (kelasPerkuliahan.getMataKuliah()!=null) {
			copiedKodeMatkul = kelasPerkuliahan.getMataKuliah().getKode();
			copiedNamaMatkul = kelasPerkuliahan.getMataKuliah().getNama();
			copiedSKSMatkul = kelasPerkuliahan.getMataKuliah().getSks();
		}
	}
	public String getNilai() {
		return nilai;
	}
	public void setNilai(String nilai) {
		this.nilai = nilai;
	}
	public Double getNilaiAngka() {
		return nilaiAngka;
	}
	public void setNilaiAngka(Double nilaiAngka) {
		this.nilaiAngka = nilaiAngka;
		if(nilaiAngka>=85){
			setNilai("A");
		}else if(nilaiAngka>=70){
			setNilai("B");
		}else if(nilaiAngka>=55){
			setNilai("C");
		}else if(nilaiAngka>=40){
			setNilai("D");
		}else{ 
			setNilai("E");
		}
	}
	public String getCopiedKodeMatkul() {
		return copiedKodeMatkul;
	}
	public void setCopiedKodeMatkul(String copiedKodeMatkul) {
		this.copiedKodeMatkul = copiedKodeMatkul;
	}
	public String getCopiedNamaMatkul() {
		return copiedNamaMatkul;
	}
	public void setCopiedNamaMatkul(String copiedNamaMatkul) {
		this.copiedNamaMatkul = copiedNamaMatkul;
	}
	public int getCopiedSKSMatkul() {
		return copiedSKSMatkul;
	}
	public void setCopiedSKSMatkul(int copiedSKSMatkul) {
		this.copiedSKSMatkul = copiedSKSMatkul;
	}
	public boolean isTuntas(){
		if (nilai.equals("A")||nilai.equals("B")||nilai.equals("C")||nilai.equals("D")){
			return true;
		}
		return false;
	}
	public void copyMatKulString(){
		copiedKodeMatkul = kelasPerkuliahan.getMataKuliah().getKode();
		copiedNamaMatkul = kelasPerkuliahan.getMataKuliah().getNama();
		copiedSKSMatkul = kelasPerkuliahan.getMataKuliah().getSks();
	}
	public String toString(){
		return getMahasiswa().toString()+getKelasPerkuliahan().toString()+" "+nilai;
	}
	@Override
	public int compareTo(PesertaKuliah arg0) {
		String s1,s2;
		s1 = arg0.getKelasPerkuliahan().getTahunAjaran();
		s2 = arg0.getKelasPerkuliahan().getTahunAjaran();
		if (s1.equals(s2)){
			return getKelasPerkuliahan().compareTo(arg0.getKelasPerkuliahan());
		}
		return s1.compareTo(s2);
	}
	@Override
	public int compare(PesertaKuliah arg0, PesertaKuliah arg1) {
		return arg0.compareTo(arg1);
	}
	

}
