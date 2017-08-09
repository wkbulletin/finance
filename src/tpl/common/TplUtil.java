package tpl.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TplUtil {
	protected transient final static Log logger = LogFactory.getLog(TplUtil.class);
	
	
	
	/**
	 * 清除多余路径
	 * @param str
	 * @return
	 */
	private static Pattern p_clearPath = Pattern.compile("[\\\\/]+");
	public static String clearPath(String str) {
		if(str == null)return str;
		return p_clearPath.matcher(str).replaceAll("/");
	}
	/**
	 * 判断文件是否存在 
	 * @param str
	 * @return
	 */
	public static String[] checkFilePath(TplConfig tplConfig, String filename) {
		int pos = filename.indexOf('|');
		String root = tplConfig.getAbsRoot().concat("/");
		if(pos>0){
			String[] arr = filename.split("\\|");
			String skinFile = TplUtil.clearPath(root.concat(arr[2].concat(arr[0])));
			File file = new File(skinFile);
			if(file.exists()){
				return new String[]{skinFile,arr[1],arr[2]};
			}else{
				skinFile = TplUtil.clearPath(root.concat(arr[1].concat(arr[0])));
				file = new File(skinFile);
				if(file.exists()){
					return new String[]{skinFile,arr[1],arr[2]};
				}
			}
		}else{
			String skinFile = TplUtil.clearPath(root.concat(filename));
			File file = new File(skinFile);
			if(file.exists()){
				return new String[]{skinFile};
			}
		}
		return null;
	}
	
	
	/**
	 * 读取文件
	 * @param filename
	 * @param encode
	 * @return
	 */
	public static String fileRead(String filename, String encode) {
		return fileRead(new File(filename), encode);
	}
	public static String fileRead(File _file, String encode) {
		FileInputStream file = null;
		BufferedReader fw = null;
		try {
			file = new FileInputStream(_file);
			int ch;
			StringBuffer strb = new StringBuffer();
			fw = new BufferedReader(new InputStreamReader(file, encode));
			while ((ch = fw.read()) > -1) {
				strb.append((char) ch);
			}
			fw.close();
			file.close();
			return strb.toString();
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}finally{
			if(fw!=null){
				try {
					fw.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
			if(file!=null){
				try {
					file.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
