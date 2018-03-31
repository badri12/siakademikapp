package org.stth.siak.util;

import java.util.List;

import org.stth.jee.persistence.DosenKaryawanPersistence;
import org.stth.jee.persistence.GenericPersistence;
import org.stth.jee.persistence.KurikulumPersistence;
import org.stth.jee.persistence.MahasiswaPersistence;
import org.stth.siak.entity.ACLAdministrasiEnum;
import org.stth.siak.entity.DosenKaryawan;
import org.stth.siak.entity.Kurikulum;
import org.stth.siak.entity.Mahasiswa;
import org.stth.siak.entity.ProgramStudi;
import org.stth.siak.entity.UserAccessRightsAdministrasi;

public class OffTest {

	public static void main(String[] args) {
		//mahasiswa();
		dosen();
	}

	private static void dosen() {
		DosenKaryawan kr = new DosenKaryawan();
		kr.setAlias("badri12");
		List<DosenKaryawan> ldk = DosenKaryawanPersistence.getKaryawanByExample(kr);
		DosenKaryawan adm =ldk.get(0);
		UserAccessRightsAdministrasi uara = new UserAccessRightsAdministrasi();
		uara.setUser(adm);
		uara.setAcl(ACLAdministrasiEnum.MATAKULIAH);
		//GenericPersistence.merge(uara);
	}

	private static void mahasiswa() {
		List<ProgramStudi> lps = GenericPersistence.findList(ProgramStudi.class);
		for (ProgramStudi prodi : lps) {
			List<Kurikulum> lkur = KurikulumPersistence.getListByProdi(prodi);
			for (Kurikulum k : lkur) {
				if (k.getTahunMulai()==2013) {
					System.out.println(k);
					List<Mahasiswa> lm= MahasiswaPersistence.getByPaProdiAngkatan(null, prodi, 2014);
					for (Mahasiswa m : lm) {
						m.setK(k);
						GenericPersistence.merge(m);
					}
				}
			}
			System.out.println();

		}
	}
}
