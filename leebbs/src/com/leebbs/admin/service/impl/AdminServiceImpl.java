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
		if (roles != null && !roles.isEmpty()) {
			for (Role role : roles) {
				adminRoleLink.setRole(role);
				this.adminDao.saveRelativity(adminRoleLink);
			}
		}
	}
	
	@Transactional
	public void remove(String id) {
		Admin admin = new Admin();
		admin.setId(id);
		AdminRoleLink adminRoleLink = new AdminRoleLink();
		adminRoleLink.setAdmin(admin);
		//先删除AdminRoleLink关联关系
		this.adminDao.removeRelativity(adminRoleLink);
		//再删除Admin记录
		super.remove(id);
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
	
	
	/***
	 * 
	 * 更新用户(包含用户角色)
	 * 
	 * 首先返回当前admin数据库角色列表
	 * 其次确定新增和被删除的角色信息
	 * 
	 */
	@Transactional
	public void update(Admin admin) {
		super.update(admin);
		
		Admin adminWithRoles = this.adminDao.findAdminRoles(admin.getId());
		//返回原来用户与角色的所有关系
		List<Role> pageRoles = admin.getRoles();		
		List<Role> dbRoles = adminWithRoles.getRoles();
		if (dbRoles == null) {
			dbRoles = new ArrayList<Role>();
		}
		
		//先delete  再insert(hiberante)
		AdminRoleLink adminRoleLink = new AdminRoleLink();
		Collection<Role> subtract = CollectionUtils.subtract(dbRoles, pageRoles);    
		Iterator<Role> it = subtract.iterator();
		while (it.hasNext()) {
	            Role role = it.next();
				adminRoleLink.setAdmin(admin);
				adminRoleLink.setRole(role);
				this.adminDao.removeRelativity(adminRoleLink);
		}
	     
		subtract = CollectionUtils.subtract(pageRoles, dbRoles);  
		it = subtract.iterator();
		while (it.hasNext()) {
	            Role role = it.next();
				adminRoleLink.setAdmin(admin);
				adminRoleLink.setRole(role);
				this.adminDao.saveRelativity(adminRoleLink);
		}
	
	}
}
