package com.leebbs.admin.dao;

import java.util.List;

import com.leebbs.admin.entity.Role;
import com.leebbs.core.dao.BaseDao;


public interface RoleDao extends BaseDao<Role, String>{
	/**
	 * 返回所有角色
	 * @return
	 */
	public List<Role> findAll();
	
	public List<Role> findRoleAdmin(Role role);
}
