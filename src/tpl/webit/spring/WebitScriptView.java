package tpl.webit.spring;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import libcore.util.DateSimpleUtil;

import org.springframework.web.servlet.view.AbstractTemplateView;

import com.alibaba.fastjson.JSONObject;
import com.huacai.web.bean.privilege.AdminInfo;
import com.huacai.web.common.LoginCommon;
import com.huacai.web.constant.TplViewConstants;

/**
 * 
 */
public class WebitScriptView extends AbstractTemplateView {

	private WebitScriptViewResolver resolver;

	public void setResolver(WebitScriptViewResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//全局变量
		JSONObject gjson = new JSONObject();
		gjson.put("now_time", System.currentTimeMillis());
		gjson.put("now_date", DateSimpleUtil.date("Y-m-d H:i:s"));
		
		model.put("_GLOBAL", gjson);
		
		model.put("request", request);
		model.put("response", response);
		
		AdminInfo adminInfo = (AdminInfo)request.getSession(true).getAttribute(LoginCommon.SESSION_KEY_LOGIN_INFO);
		model.put("ADMIN_INFO", adminInfo);
		
		
		TplViewConstants.reg(request, response, model);
		
		this.resolver.render(getUrl(), model, request, response);
	}

}
