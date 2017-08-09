package tpl.webit.spring;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

import com.huacai.web.controller.task.TaskController;

import tpl.common.TplFilter;
import tpl.webit.WebitTplSyntaxFilter;
import webit.script.servlet.WebEngineManager;
import webit.script.servlet.WebEngineManager.ServletContextProvider;

 
public class WebitScriptViewResolver extends AbstractTemplateViewResolver implements InitializingBean {

	protected final WebEngineManager engineManager;

	@Override
	protected Class<?> requiredViewClass() {
		return WebitScriptView.class;
	}

	public void afterPropertiesSet() throws Exception {
		//########## 设置语法替换器 ##########
		TplFilter.setSyntaxFilter(new WebitTplSyntaxFilter());
		if (getSuffix() == null || getSuffix().length() == 0) {
			super.setSuffix(this.engineManager.getEngine().getSuffix());
		}
	}

	public WebitScriptViewResolver() {
		setViewClass(requiredViewClass());

		this.engineManager = new WebEngineManager(new ServletContextProvider() {

			public ServletContext getServletContext() {
				return WebitScriptViewResolver.this.getServletContext();
			}
		});
	}

	public void setConfigPath(String configPath) {
		this.engineManager.setConfigPath(configPath);
	}

	public void resetEngine() {
		this.engineManager.resetEngine();
	}

	@Override
	protected WebitScriptView buildView(String viewName) throws Exception {
		WebitScriptView view = (WebitScriptView) super.buildView(viewName);
		view.setResolver(this);
		return view;
	}

	protected void render(String viewName, Map<String, Object> model, final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		//########## 设置皮肤 ##########
		//viewName = viewName.concat("|default|".concat(request.getParameter("skin")));
		
		//商户与默认模板配置
		//viewName = viewName.concat("|default|".concat((String)request.getAttribute("_WEB_AGENCY_ID_")));
		
		//viewName = viewName.concat("|default|10000");
		model.put(TaskController.Priv_Set, request.getAttribute(TaskController.Priv_Set));
		this.engineManager.renderTemplate(viewName, model, response);
	}
	
	/**
	 * ########## 供注入的bean使用 ###############
	 * @param viewName
	 * @param model
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void print(String viewName, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		model.put("request", request);
		model.put("response", response);
		render(viewName.concat(getSuffix()), model, request, response);
	}
}
