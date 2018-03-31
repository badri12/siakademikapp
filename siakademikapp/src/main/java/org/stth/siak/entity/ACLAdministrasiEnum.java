package org.stth.siak.entity;

import java.util.List;

public enum ACLAdministrasiEnum {
	KONFIGURASI,
	LOG_PERKULIAHAN,LOG_PERKULIAHAN_EDIT,LOG_PERKULIAHAN_DELETE,
	JADWAL_KULIAH,
	ABSENSI_KULIAH_PRINT,
	KRS_PRINT,
	TRANSKRIP_PRINT,TRANSKRIP_AKADEMIK,
	MATAKULIAH,
	VIEW_MAHASISWA,EDIT_MAHASISWA,
	EDIT_DOSEN,
	JUDUL,
	ADD_KELASPERKULIAHAN,EDIT_KELASPERKULIAHAN,ADD_NILAIPERKULIAHAN,TOLAK_KRS,
	HAKKRS,SUPERU;
	
	public static boolean isEligibleTo(List<UserAccessRightsAdministrasi> lacl, ACLAdministrasiEnum acl){
		boolean b=false;
		for (UserAccessRightsAdministrasi userAccessRightsAdministrasi : lacl) {
			ACLAdministrasiEnum aclu = userAccessRightsAdministrasi.getAcl();
			if (aclu.equals(acl)){
				b = true;
				break;
			}
		}
		return b;
	}
}
