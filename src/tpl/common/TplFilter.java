package tpl.common;

import java.io.File;
import java.util.regex.Pattern;

import tpl.webit.WebitTplSyntaxFilter;


public class TplFilter {
	private static TplSyntaxFilter syntaxFilter;
	 
	private static Pattern p_replacePath = Pattern.compile("images/");
	
	private static Pattern skinPattern = Pattern.compile("<!--\\s*#include\\s*(file|virtual)\\s*=\\s*\"([^\"]+)\"\\s*-->", Pattern.DOTALL);
	
	
	/**
	 * 语法替换，模板规则统一
	 * @param fname
	 * @param str
	 * @return
	 */
	public static String filter(TplConfig tplConfig, String[] fname,File file, String encode) {
		return filter(tplConfig, fname,TplUtil.fileRead(file, encode));
	}
	public static String filter(TplConfig tplConfig, String[] fname, String str) {
		if(str == null)return str;
		
		str = replacePath(tplConfig, fname[0], str);
		
		// include路径
		if(fname.length > 1 && fname[1].length()>0){
			//替换include 为皮肤路径 <!-- #include file="/test2.header.html" -->
			String skinPatternRep = "<!-- #include $1=\"$2|"+fname[1]+"|"+fname[2]+"\" -->";
			str = skinPattern.matcher(str).replaceAll(skinPatternRep);
		}
		
		
		// 替换语法
		if(syntaxFilter != null){
			str = syntaxFilter.filter(tplConfig, str);
		}
		
		
		
		
		return str;
	}

	
	/**
	 * 替换图片路径
	 * @param absRoot
	 * @param root
	 * @param file
	 */
	public static String replacePath(TplConfig tplConfig, String fname, String str){
		if(tplConfig == null)return str;
		String fullpathFile = TplUtil.clearPath(fname);
		String srcTplPath = tplConfig.getRoot();
		String file = fullpathFile.substring(tplConfig.getAbsRoot().length());
		
		String srcPath = fullpathFile.substring(0, fullpathFile.lastIndexOf("/") + 1);
		
		String srcWebFilename = TplUtil.clearPath(srcTplPath.concat(file));
		String srcWebPath = srcWebFilename.substring(0, srcWebFilename.lastIndexOf("/") + 1);

		String srcWebFilenameDef = TplUtil.clearPath(srcTplPath);
		String srcWebPathDef = srcWebFilenameDef.substring(0, srcWebFilenameDef.lastIndexOf("/") + 1);

		// 判断图片文件夹是否存在
		File imgFile = new File(srcPath.concat("images"));
		
		if (imgFile.isDirectory()) {
			str = p_replacePath.matcher(str).replaceAll(srcWebPath.concat("images/"));
		} else {
			str = p_replacePath.matcher(str).replaceAll(srcWebPathDef.concat("images/"));
		}
		return str;
			
	}

 	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String file = "Z:/workspace/jee/agile_pm/agile_pm/WebContent/tpl/default/webit.html";
		String str = TplUtil.fileRead(file, "UTF-8");
		
		syntaxFilter = new WebitTplSyntaxFilter();
		System.out.println(filter(null, new String[]{""},str));
//		
//		long t1 = System.currentTimeMillis();
//		int len = 1000000;
//		String str1 = "afasfsssasdfaasdfffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
//		for(int i = 0; i < len; i++){
//			filter(null, "",str1);
//		}
//		System.out.println(System.currentTimeMillis()-t1);
	}
	public static TplSyntaxFilter getSyntaxFilter() {
		return syntaxFilter;
	}
	public static void setSyntaxFilter(TplSyntaxFilter syntaxFilter) {
		TplFilter.syntaxFilter = syntaxFilter;
	}
	
}
