package com.leebbs.admin.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leebbs.admin.dao.RoleDao;
import com.leebbs.admin.entity.Role;
import com.leebbs.admin.entity.RoleAuthority;
import com.leebbs.admin.service.RoleService;


@Service
public class RoleServiceImpl extends BaseServiceImpl<Role, String> 
	implements RoleService{
		
	@Resource
	private RoleDao roleDao;
	
	@Resource
	public void setBaseDao(RoleDao roleDao) {
		super.setBaseDao(roleDao);
	}

	@Transactional
	public void update(Role role, String... authorities) {
		this.roleDao.update(role);
		
		List<RoleAuthority> pageRoleAuthorities = new ArrayList<RoleAuthority>();
		List<RoleAuthority> dbRoleAuthorities = this.roleDao.findRoleAuthorities(role);
		

		for (String authority : authorities) {
				RoleAuthority ra = new RoleAuthority();
				ra.setRoleId(role.getId());
				ra.setAuthorities(authority);
				pageRoleAuthorities.add(ra);
		}
		
		if (dbRoleAuthorities == null) {
			dbRoleAuthorities = new ArrayList<RoleAuthority>();
		}
		
		Collection<RoleAuthority> subtract = CollectionUtils.subtract(dbRoleAuthorities, pageRoleAuthorities); 
		
		Iterator<RoleAuthority> it = subtract.iterator();
		while (it.hasNext()) {
			this.roleDao.deleteRoleAuthorities(it.next());
		}

		
		subtract = CollectionUtils.subtract(pageRoleAuthorities, dbRoleAuthorities); 
		it = subtract.iterator();
		while (it.hasNext()) {
			this.roleDao.saveRoleAuthorities(it.next());
		}
		
//		Collection<RoleAuthority> subtract = CollectionUtils.subtract(dbRoleAuthorities, pageRoleAuthorities); 	
//		if (!CollectionUtils.isEmpty(dbRoleAuthorities)) {
//			 for (RoleAuthority ra : dbRoleAuthorities) {
//				 System.out.println(ra.getAuthorities());
//			 }
//		}
		 
	}
	
}
