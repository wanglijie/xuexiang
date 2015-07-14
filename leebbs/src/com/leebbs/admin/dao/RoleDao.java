package com.leebbs.admin.dao;

import java.util.List;

import com.leebbs.admin.entity.Role;
import com.leebbs.admin.entity.RoleAuthority;
import com.leebbs.core.dao.BaseDao;


public interface RoleDao extends BaseDao<Role, String>{
	/**
	 * 返回所有角色
	 * @return
	 */
	public List<Role> findAll();
	
	public List<Role> findRoleAdmin(Role role);

	public List<RoleAuthority> findRoleAuthorities(Role role);
	
	public void saveRoleAuthorities(RoleAuthority roleAuthority);
	
	public void deleteRoleAuthorities(RoleAuthority roleAuthority);
}
