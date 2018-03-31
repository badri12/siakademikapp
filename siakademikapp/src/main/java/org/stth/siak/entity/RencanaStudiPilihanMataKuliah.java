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

import org.stth.siak.enumtype.RencanaStudiMatkulAdditionMethod;
import org.stth.siak.enumtype.RencanaStudiMatkulKeterangan;
@Entity
public class RencanaStudiPilihanMataKuliah implements Comparable<RencanaStudiPilihanMataKuliah>, Comparator<RencanaStudiPilihanMataKuliah>{
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private RencanaStudiMahasiswa rencanaStudi;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private MataKuliah mataKuliah;
	@Enumerated(EnumType.STRING)
	private RencanaStudiMatkulKeterangan keterangan;
	@Enumerated(EnumType.STRING)
	private RencanaStudiMatkulAdditionMethod addMethod;
	private String submittedBy;
	public RencanaStudiPilihanMataKuliah(){
		
	}
	public RencanaStudiPilihanMataKuliah(RencanaStudiMahasiswa rsm, MataKuliahKurikulum mk){
		this.rencanaStudi = rsm;
		this.mataKuliah = mk.getMataKuliah();
	}
	public RencanaStudiPilihanMataKuliah(RencanaStudiMahasiswa rsm,
			MataKuliah mk) {
		this.rencanaStudi = rsm;
		this.mataKuliah = mk;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public RencanaStudiMahasiswa getRencanaStudi() {
		return rencanaStudi;
	}
	public RencanaStudiMatkulKeterangan getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(RencanaStudiMatkulKeterangan keterangan) {
		this.keterangan = keterangan;
	}
	public void setRencanaStudi(RencanaStudiMahasiswa rencanaStudi) {
		this.rencanaStudi = rencanaStudi;
	}
	public MataKuliah getMataKuliah() {
		return mataKuliah;
	}
	public void setMataKuliah(MataKuliah mataKuliah) {
		this.mataKuliah = mataKuliah;
	}
	public RencanaStudiMatkulAdditionMethod getAddMethod() {
		return addMethod;
	}
	public void setAddMethod(RencanaStudiMatkulAdditionMethod addMethod) {
		this.addMethod = addMethod;
	}
	public String getSubmittedBy() {
		return submittedBy;
	}
	public void setSubmittedBy(String submittedBy) {
		this.submittedBy = submittedBy;
	}
	@Override
	public int compare(RencanaStudiPilihanMataKuliah arg0,
			RencanaStudiPilihanMataKuliah arg1) {
		return arg0.compareTo(arg1);
	}
	@Override
	public int compareTo(RencanaStudiPilihanMataKuliah arg0) {
		if (mataKuliah.getId()!=arg0.mataKuliah.getId()){
			return mataKuliah.compareTo(arg0.mataKuliah);
		}
		return rencanaStudi.getMahasiswa().compareTo(arg0.rencanaStudi.getMahasiswa());
	}
	
	
}
