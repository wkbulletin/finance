package tpl.webit;

import java.util.Vector;
import java.util.regex.Pattern;

import tpl.common.TplConfig;
import tpl.common.TplSyntaxFilter;


public class WebitTplSyntaxFilter implements TplSyntaxFilter{
	private static Vector<Object[]> syntaxPatterns = new Vector<Object[]>();

	static{
		/*
DelimiterStatementStart     = "<%"
DelimiterStatementEnd       = "%>"
int length = yylength()-2;
		 */
		Object[] pt;
		
		//<!-- #include file="/test2.header.html" -->  to  <% include "/test2.header.html"; %>
		pt = new Object[]{
				Pattern.compile("<!--\\s*#include\\s*file\\s*=\\s*\"([^\"]+)\"\\s*-->", Pattern.DOTALL), 
				"<% include \"$1\"; %>"
				};
		syntaxPatterns.add(pt);
		
		//<!-- for(book : books) --> to <% for(book : books){ %>
		pt = new Object[]{
				Pattern.compile("<!--\\s*(for\\s*[\\(](.+?)[\\)])\\s*-->", Pattern.DOTALL), 
				"<% $1{ %>"
				};
		syntaxPatterns.add(pt);
		
		//<!-- if( ... ) --> to <% if( ... ){ %>
		pt = new Object[]{
				Pattern.compile("<!--\\s*(if\\s*[\\(](.+?)[\\)])\\s*-->", Pattern.DOTALL), 
				"<% $1{ %>"
				};
		syntaxPatterns.add(pt);
		
		//<!-- else if( ... ) --> to <% }else if( ... ){ %>
		//"<!--\\s*(else\\s*(if|)(\\s*[\\(](.+?)[\\)]|))\\s*-->"
		pt = new Object[]{
				Pattern.compile("<!--\\s*(else\\s*if\\s*[\\(](.+?)[\\)])\\s*-->", Pattern.DOTALL), 
				"<% }$1{ %>"
				};
		syntaxPatterns.add(pt);
				
		
		//<!-- else --> to <% }else{ %>
		pt = new Object[]{
				Pattern.compile("<!--\\s*(else)\\s*-->", Pattern.DOTALL), 
				"<% }$1{ %>"
				};
		syntaxPatterns.add(pt);	
	
		
		
		//<!-- end --> to <% } %>
		pt = new Object[]{
				Pattern.compile("<!--\\s*end\\s*-->", Pattern.DOTALL), 
				"<% } %>"
				};
		syntaxPatterns.add(pt);
		
		
		//<!-- {echo 32;} --> to <% echo 32; %>
		pt = new Object[]{
				Pattern.compile("<!--\\s*\\{(.+?)\\}\\s*-->", Pattern.DOTALL), 
				"<% $1 %>"
				};
		syntaxPatterns.add(pt);
		
		
		//${data['index01']} to ${data["index01"]}
		pt = new Object[]{
				Pattern.compile("\\$\\{([\\w\\d_\\-]+)\\[[']*([\\w\\d_\\-]+)[']*]}", Pattern.DOTALL), 
				"\\${$1[\"$2\"]}"
				};
		syntaxPatterns.add(pt);
	}
	
	
	public String filter(TplConfig tplConfig, String str) {
		if(str == null)return str;
		// 替换语法
		for(Object[] pt : syntaxPatterns){
			str = ((Pattern)pt[0]).matcher(str).replaceAll(((String)pt[1]));
		}

		return str;
	}

}
