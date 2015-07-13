package com.leebbs.admin.controller;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.leebbs.admin.entity.search.SearchAdmin;
import com.leebbs.admin.service.AdminService;
import com.leebbs.core.utils.Pageable;


@Controller("adminAdminController")
@RequestMapping({ "/admin/admin" })
public class AdminController extends BaseAdminController{

	@Resource
	private AdminService adminService;
	
	@RequestMapping(value = {"/check_username"}, method = {RequestMethod.GET})
	@ResponseBody
	public boolean checkUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return false;
		}
		return !this.adminService.usernameExists(username);
	}
	
	
	@RequestMapping(value = {"/list"}, method = {RequestMethod.GET})
	public String list(Pageable pageable, SearchAdmin searchAdmin, ModelMap model) {
		model.addAttribute("page", adminService.findPage(pageable, searchAdmin));
		model.addAttribute("search", searchAdmin);
		return "/admin/system_set/admin/admin_view";
	}	
	
}
