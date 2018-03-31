package org.stth.jee.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.siak.entity.BerkasFotoDosen;
import org.stth.siak.entity.DosenKaryawan;

public class DosenKaryawanPersistence {
	
	public static List<DosenKaryawan> getDosen(){
		Map<String, Object> lc = new HashMap<>();
		lc.put("dosen", true);
		List<DosenKaryawan> l = GenericPersistence.findList(DosenKaryawan.class, lc);
		return l;
	}
	public static List<DosenKaryawan> getKaryawanByExample(DosenKaryawan kr){
		return getDosenByExample(kr, false);
	}
	public static List<DosenKaryawan> getDosenByExample(DosenKaryawan dosen){
		return getDosenByExample(dosen, true);
	}
	public static List<DosenKaryawan> getDosenByExample(DosenKaryawan dosen, boolean isDosen){
		Map<String, Object> lc = new HashMap<>();
		Map<String, String> like = new HashMap<>();
		lc.put("dosen", isDosen);
		if (dosen.getProdi()!=null){
			lc.put("prodi", dosen.getProdi());
		}
		if (dosen.getAlias()!=null){
			lc.put("alias", dosen.getAlias());
		}
		if(dosen.getNama()!=null){
			if (!dosen.getNama().isEmpty()) {
				like.put("nama", dosen.getNama());
			}
		}
		List<DosenKaryawan> l = GenericPersistence.findList(DosenKaryawan.class, lc,like);
		return l;
	}
	public static void updatePicture(DosenKaryawan d, byte[] image){
		Map<String, Object> lc = new HashMap<>();
		lc.put("dosen", d);
		List<BerkasFotoDosen> lg = GenericPersistence.findList(BerkasFotoDosen.class, lc);
		BerkasFotoDosen bfd;
		if (lg.size()>0){
			bfd = lg.get(0);
		} else {
			bfd = new BerkasFotoDosen();
			bfd.setDosen(d);
		}
		bfd.setFile(image);
		GenericPersistence.merge(bfd);
	}
	public static BerkasFotoDosen getFotoDosen(DosenKaryawan d){
		Map<String, Object> lc = new HashMap<>();
		lc.put("dosen", d);
		List<BerkasFotoDosen> lg = GenericPersistence.findList(BerkasFotoDosen.class, lc);
		BerkasFotoDosen bfd = null;
		if (lg.size()>0){
			bfd = lg.get(0);
		}
		return bfd;
	}
	

}
