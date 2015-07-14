package com.leebbs.admin.service;

import com.leebbs.admin.entity.Role;

public interface RoleService extends BaseService<Role, String>{
	
	public void update(Role role, String... authorities);
	
}
