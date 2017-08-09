package tpl.webit;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import tpl.common.TplConfig;
import tpl.common.TplFilter;
import webit.script.loaders.Resource;

/**
 * 
 */
public class FileResource implements Resource {
	private final String encoding;
	private File file;
	private long lastModified;
	
	
	private File file2[] = new File[2];
	
	
	private String[] path;
	
	private TplConfig tplConfig;

	public FileResource(String[] path, String encoding, TplConfig tplConfig) {
		this.tplConfig = tplConfig;
		this.encoding = encoding;
		
		this.path = path;
		
		if(path.length > 1){
			file2[0] = new File(path[0].replace("/".concat(path[2]).concat("/"), "/".concat(path[1]).concat("/")));
			file2[1] = new File(path[0].replace("/".concat(path[1]).concat("/"), "/".concat(path[2]).concat("/")));
		}else{
			file2[0] = new File(path[0]);
			file2[1] = null;
		}
		
		

		
		//System.out.println(this.file);
	}
	public void checkFile(){
		if(file2[1]!=null&&file2[1].exists()){
			file = file2[1];
		}else{
			file = file2[0];
		}
	}

	public boolean isModified() {
		checkFile();
		return lastModified != file.lastModified();
	}

	public Reader openReader() throws IOException {
		checkFile();
		lastModified = file.lastModified();
		return new StringReader(TplFilter.filter(tplConfig, path, file, encoding));
	}

	@Override
	public boolean exists() {
		checkFile();
		return file.exists();
	}
}
