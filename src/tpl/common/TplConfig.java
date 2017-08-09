package tpl.common;

import javax.servlet.ServletContext;


public class TplConfig {
	
	private String absRoot = "";
	private String root = "";
	private String webPath = "";
	
	private ServletContext servletContext;
	
	public TplConfig(ServletContext sc, String root) {
		servletContext = sc;
		webPath = TplUtil.clearPath(sc.getRealPath("/"));
		this.root = TplUtil.clearPath(root.concat("/"));
		absRoot = webPath + root;
		
	}
	
	public String getWebPath() {
		return webPath;
	}

	public void setWebPath(String webPath) {
		this.webPath = webPath;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}



	public String getAbsRoot() {
		return absRoot;
	}

	public void setAbsRoot(String absRoot) {
		this.absRoot = absRoot;
	}


	public ServletContext getServletContext() {
		return servletContext;
	}


	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	
	
	
}
