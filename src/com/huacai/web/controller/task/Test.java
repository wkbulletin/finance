package com.huacai.web.controller.task;

public class Test {

	public static void main(String[] args) {
		String f = "/tpl/upload/323234234/m1_3file/Module1-input-投资部_待确认.xlsx";
		System.out.println(f.replaceAll("upload/[0-9]*/", "upload/" + 36 + "/"));
		System.out.println(f.replaceAll("upload\\\\[0-9]*\\\\", "upload\\\\" + 36 + "\\\\"));
	}

}
