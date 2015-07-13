package com.leebbs.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.leebbs.admin.dao.RoleDao;
import com.leebbs.admin.entity.Role;
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

}
