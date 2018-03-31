package org.stth.jee.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stth.siak.entity.UserAccessRightsAdministrasi;
import org.stth.siak.entity.DosenKaryawan;

public class AdministrasiAccessControlListPersistence {

	public static List<UserAccessRightsAdministrasi> getListByUser(DosenKaryawan user){
		Map<String, Object> lc= new HashMap<>();
		lc.put("user", user);
		List<UserAccessRightsAdministrasi> rslt = GenericPersistence.findList(UserAccessRightsAdministrasi.class,lc);
		return rslt;
	}

}
