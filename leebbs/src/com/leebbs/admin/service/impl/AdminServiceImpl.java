package com.leebbs.admin.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;  
import org.apache.commons.lang.ArrayUtils;   

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leebbs.admin.dao.AdminDao;
import com.leebbs.admin.entity.Admin;
import com.leebbs.admin.entity.AdminRoleLink;
import com.leebbs.admin.entity.Role;
import com.leebbs.admin.entity.search.SearchAdmin;
import com.leebbs.admin.service.AdminService;
import com.leebbs.core.utils.Page;
import com.leebbs.core.utils.Pageable;

//http://feiyeguohai.iteye.com/blog/1180898/

@Service
public class AdminServiceImpl extends BaseServiceImpl<Admin, String> 
	implements AdminService{
	
	@Resource
	private AdminDao adminDao;
	
	@Resource
	public void setBaseDao(AdminDao adminDao) {
		super.setBaseDao(adminDao);
	}

	@Transactional
	public void save(Admin admin) {
		super.save(admin);

		List<Role> roles = admin.getRoles();
		
		AdminRoleLink adminRoleLink = new AdminRoleLink();
		adminRoleLink.setAdmin(admin);
		if (roles != null) {
			for (Role role : roles) {
				adminRoleLink.setRole(role);
				this.adminDao.saveRelativity(adminRoleLink);
			}
		}
	}
	
	@Transactional
	public void remove(String id) {
		//先删除AdminRoleLink关联关系
		
		Admin admin = new Admin();
		admin.setId(id);
		
		AdminRoleLink adminRoleLink = new AdminRoleLink();
		adminRoleLink.setAdmin(admin);
		this.adminDao.removeRelativity(adminRoleLink);
		//再删除Admin记录
		super.remove(id);
		
/*		int b=12;
		int c;
		for (int i=2;i>=-2;i--){
			c=b/i;
			System.out.println("i="+i);
		}*/
	}
	
	@Transactional(readOnly=true)
	public Page<Admin> findPage(Pageable pageable, SearchAdmin searchAdmin) {
		//分页并计算出总页数 
		List<Admin> admins = adminDao.findPage(pageable, searchAdmin);
		Page<Admin> page = new Page<Admin>(admins, pageable);
		return page;
	}

	@Transactional(readOnly=true)
	public boolean usernameExists(String username) {
		if (username == null) {
			return false;
		}
		long num = adminDao.usernameExists(username);
		return num > 0L;
	}	
	
	@Transactional
	public void update(Admin admin) {
		super.update(admin);
		//先delete  再insert(hiberante)
		Admin adminWithRoles = this.adminDao.findAdminRoles(admin.getId());
		//返回原来用户与角色的所有关系
		List<Role> pageRoles = admin.getRoles();		
		List<Role> dbRoles = adminWithRoles.getRoles();
		if (dbRoles == null) {
			dbRoles = new ArrayList<Role>();
		}
		
		AdminRoleLink adminRoleLink = new AdminRoleLink();
		Collection<Role> subtract = CollectionUtils.subtract(dbRoles, pageRoles);    
		System.out.println("Intersection(dbRoles, pageRoles): " + ArrayUtils.toString(subtract.toArray()));   
		
		Iterator<Role> it = subtract.iterator();
		while (it.hasNext()) {
	            Role role = it.next();
				adminRoleLink.setAdmin(admin);
				adminRoleLink.setRole(role);
				this.adminDao.removeRelativity(adminRoleLink);
		}
	     
		subtract = CollectionUtils.subtract(pageRoles, dbRoles);  
		System.out.println("Intersection(pageRoles, dbRoles): " + ArrayUtils.toString(subtract.toArray())); 
	
		it = subtract.iterator();
		while (it.hasNext()) {
	            Role role = it.next();
				adminRoleLink.setAdmin(admin);
				adminRoleLink.setRole(role);
				this.adminDao.saveRelativity(adminRoleLink);
		}
	
	}
}
