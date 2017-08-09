package com.huacai.web.controller.cp;

import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huacai.web.controller.BaseController;

@Controller
@RequestMapping
@Scope("prototype")
public class NoAccessController extends BaseController {
	protected final static Logger logger = LogManager.getLogger(NoAccessController.class);
	
	/**
	 * 没有权限页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/no_access")
	public String no_access(Map<String, Object> model) {
		return "/common/no_access";
	}
	 
}
