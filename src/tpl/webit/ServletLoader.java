package tpl.webit;

import javax.servlet.ServletContext;

import tpl.common.TplConfig;
import tpl.common.TplUtil;
import webit.script.CFG;
import webit.script.Engine;
import webit.script.loaders.AbstractLoader;
import webit.script.loaders.Resource;

/**
 * 
 */
public class ServletLoader extends AbstractLoader {
	private ServletContext servletContext;
	private TplConfig tplConfig;

	@Override
	public void init(Engine engine) {
		super.init(engine);
		if (this.servletContext == null) {
			setServletContext((ServletContext) engine
					.getConfig(CFG.SERVLET_CONTEXT));
		}
	}

	@Override
	public Resource get(String name) {
		final String[] realpath;
		if ((realpath = TplUtil.checkFilePath(tplConfig, name)) != null) {
			return new FileResource(realpath, encoding, tplConfig);
		} else {
			return new FileResource(realpath, encoding, tplConfig);
		}

		// if ((realpath =
		// servletContext.getRealPath(getRealPath(name))) != null) {
		// return new FileResource(realpath, encoding, tplConfig);
		// } else {
		// throw new
		// ResourceNotFoundException("Not found: ".concat(name));
		// }
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		tplConfig = new TplConfig(servletContext, getRealPath(""));
	}

	public TplConfig getTplConfig() {
		return tplConfig;
	}

}
