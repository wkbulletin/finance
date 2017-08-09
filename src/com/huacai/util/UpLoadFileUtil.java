package com.huacai.util;

import java.io.File;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import libcore.util.DateUtil;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class UpLoadFileUtil {
	private final static Logger logger = LogManager.getLogger(UpLoadFileUtil.class);

	public static String uploadFile(HttpServletRequest request, String pathfile) {
		return uploadFile(request, pathfile, "imageFile");
	}

	public static String uploadFile(HttpServletRequest request, String pathfile, String parmName) {
		String fileName = "";
		try {
			String basePath = request.getSession().getServletContext().getRealPath("/"); // 存放文件地址
			if(basePath!=null&&!basePath.endsWith("/")){
				basePath=basePath+"/";
			}
			File filePath = new File(basePath + pathfile); // 拼写成完整的路径
			if (!filePath.exists()) {
				filePath.mkdirs(); // 不存在创建
				logger.info("创建文件目录：" + filePath);
			}
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile multipartFile = multipartRequest.getFile(parmName);
			String orig_file=null;
			if (multipartFile != null) {
				orig_file=multipartFile.getOriginalFilename();
				if(orig_file!=null&&orig_file.length()>0){
					//fileName = DateUtil.getDate("yyyyMMddHHmmss") + "_" +orig_file ;// 取得文件名
					
					String tString = orig_file.substring(orig_file.indexOf("."));
					fileName = DateUtil.getDate("yyyyMMddHHmmss") + "_" + getRand() + tString;
					
					logger.info("文件名称：" + fileName);
					try {
						FileCopyUtils.copy(multipartFile.getBytes(), new File(filePath, File.separator + fileName));// 拷贝文件到服务器
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}

	public static Boolean moveFile(HttpServletRequest request, String fromFilePath, String toFilePath, String fileName) {
		String basePath = request.getSession().getServletContext().getRealPath("/"); // 存放文件地址
		File file = new File(basePath + fromFilePath, fileName);
		File newFilePath = new File(basePath + toFilePath);
		if (!newFilePath.exists()) {
			newFilePath.mkdirs(); // 不存在创建
			logger.info("创建文件目录：" + newFilePath);
		}
		boolean success = file.renameTo(new File(newFilePath, fileName));
		file.delete();// 删除
		return success;
	}
	public static String getRand() {
		int max = 10000000;
		int min = 10;
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return String.valueOf(s);
	}
}
