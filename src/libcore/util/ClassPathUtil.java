package libcore.util;

import java.io.File;

public class ClassPathUtil {
	
	public static String basePath() {
		String basePath;
		/*try {
			basePath = URLDecoder.decode(ClassPathUtil.class.getProtectionDomain()
					.getCodeSource().getLocation().getFile(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			basePath = "";
		}
		basePath = basePath.replaceAll("[\\\\/]+[\\w\\.\\-]*\\.jar", "")+File.separator;
		*/
		//Thread.currentThread().getContextClassLoader().getResource("")
		
		//System.out.println((new ClassPathUtil()).getClass().getResource("/libcore.properties"));
		
		basePath = ClassPathUtil.class.getResource("/libcore.properties").getPath()+"/";
		
		String classPackage = ClassPathUtil.class.getPackage().getName();
		classPackage = classPackage.replaceAll("\\.", "[\\\\/]+")+"[\\/]+";
		basePath = basePath.replaceAll(classPackage,"");
		if(basePath.indexOf(".jar") > 0){
			basePath = basePath.replaceAll("[\\\\/]+[\\w\\.\\-]*\\.jar[!\\/]*", "")+File.separator;
		}
		if(basePath.indexOf(":") > 0 && basePath.substring(0,1).equals("/")){
			basePath = basePath.substring(1);
		}
		basePath = basePath.replaceAll("file:", "");
		basePath = basePath.replaceAll("libcore.properties", "");
		
		basePath = basePath.replaceAll("[\\\\/]+", "/");
		
		return "/"+basePath;
	}
	
	public static String webPath() {
		String basePath = basePath();
		
		basePath = basePath.replaceAll("(lib|classes|WEB-INF|WEB-INF/lib|WEB-INF/classes)/$","");
		
		return basePath;
	}
	
	
	public static void main(String[] args) {
		System.out.println("basePath="+basePath());
		
		//String str = "E:/workspace_jee/huacai/huacai_v4/huacai_v4/WebContent/WEB-INF/lib/";
		// str =  str.replaceAll("(lib|classes|WEB-INF|WEB-INF/lib|WEB-INF/classes)/$","");
		//System.out.println(str);
	}
}
