package com.leebbs.admin.controller;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ServletContextAware;

@Controller("adminCommonController")
@RequestMapping({ "/admin/common" })
public class CommonController implements ServletContextAware {
	@Value("${system.name}")
	private String systemName;
	
	private ServletContext servletContext;

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@RequestMapping(value = { "/main" }, method = { RequestMethod.GET })
	public String main() {
		return "/admin/common/main";
	}

	@RequestMapping(value = { "/overview" }, method = { RequestMethod.GET })
	public String index(ModelMap model) {
		return "/admin/common/overview";
	}
}
