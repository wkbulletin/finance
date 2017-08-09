package com.huacai.web.controller.task;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.itrus.util.FileUtils;

import libcore.util.ExcelParser;

public class UploadUtil {
	/**
	 * 保存投资部两个sheet页 为  json 
	 * 用户在 查看中使用
	 * @param module
	 * @param file
	 */
	public static void saveTouzibuSheet(String module, File file) {
		if(module.endsWith("m1_3file")){ //模块1 的投资部文件
			ExcelParser excelParser = new ExcelParser(file);
			List<List<String>> sheet6 = excelParser.getDatasInSheet(6);
			List<List<String>> sheet7 = excelParser.getDatasInSheet(7);
			String filePath = file.getParent();
			
			try {
				FileUtils.saveBytesToFile(JSON.toJSONBytes(sheet6, SerializerFeature.PrettyFormat), filePath + "/sheet6.json");
				FileUtils.saveBytesToFile(JSON.toJSONBytes(sheet7, SerializerFeature.PrettyFormat), filePath + "/sheet7.json");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}else{
			
		}
	}
}
