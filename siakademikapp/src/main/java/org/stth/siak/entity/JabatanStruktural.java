package org.stth.siak.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.ibm.icu.util.Calendar;

@Entity
public class JabatanStruktural {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String jabatan;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private JabatanStruktural atasan;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private DosenKaryawan penjabat;
	private Calendar tmt;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getJabatan() {
		return jabatan;
	}
	public void setJabatan(String jabatan) {
		this.jabatan = jabatan;
	}
	public JabatanStruktural getAtasan() {
		return atasan;
	}
	public void setAtasan(JabatanStruktural atasan) {
		this.atasan = atasan;
	}
	public DosenKaryawan getPenjabat() {
		return penjabat;
	}
	public void setPenjabat(DosenKaryawan penjabat) {
		this.penjabat = penjabat;
	}
	public Calendar getTmt() {
		return tmt;
	}
	public void setTmt(Calendar tmt) {
		this.tmt = tmt;
	}
	
	

}
