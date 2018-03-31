package org.stth.siak.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BerkasFotoDosen extends Berkas{
	@ManyToOne(cascade = CascadeType.REFRESH)
    @PrimaryKeyJoinColumn
	DosenKaryawan dosen;

	public DosenKaryawan getDosen() {
		return dosen;
	}

	public void setDosen(DosenKaryawan dosen) {
		this.dosen = dosen;
	}
	
	

}
