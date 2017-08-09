package com.huacai.web.controller.index;

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
public class IndexController extends BaseController {
	protected final static Logger logger = LogManager.getLogger(IndexController.class);

	@RequestMapping("index")
	public String index(Map<String, Object> model) {
		
		//return "redirect:/privilege/privilege_list.do";
		//return "forward:/passport/login.do";
		return "/index/index";
	}
	
 
}
