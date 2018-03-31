package org.stth.siak.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Entity
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS )
public class ProgramStudi extends BasicEntity{
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String jenjang;
	private String kode;
	@NotNull
	@Pattern(regexp = "[A-Za-z -]*")
	private String nama;
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	private DosenKaryawan kaprodi;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getJenjang() {
		return jenjang;
	}
	public void setJenjang(String jenjang) {
		this.jenjang = jenjang;
	}
	public String getKode() {
		return kode;
	}
	public void setKode(String kode) {
		this.kode = kode;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public DosenKaryawan getKaprodi() {
		return kaprodi;
	}
	public void setKaprodi(DosenKaryawan kaprodi) {
		this.kaprodi = kaprodi;
	}
	public String toString(){
		return jenjang+" "+nama;
	}

}
