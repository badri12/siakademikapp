package org.stth.siak.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PembayaranMahasiswa {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer id;
	int angkatan;
	

}
