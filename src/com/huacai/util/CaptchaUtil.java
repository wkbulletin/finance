package com.huacai.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import libcore.util.FileUtil;

import com.huacai.web.filters.InitConfig;

public class CaptchaUtil {
	static byte[] vcodeStr;
	static Vector<Font> vcodeFonts;
	static{
		vcodeStr = "ABCDE2345678GFHIJKMNPRSUVWXY".getBytes();
		String basePath = InitConfig.basePath+"media/fonts/";
		
		vcodeFonts = new Vector<Font>();
		ArrayList<File> files = FileUtil.getFileList(basePath);

		try{
			for(File file : files){
				if(file.getName().endsWith(".ttf")){
					//System.out.println(">."+file.getName());
					vcodeFonts.add(Font.createFont(Font.TRUETYPE_FONT,file));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void write(String sessionName, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		
		//设置页面不缓存
		request.setAttribute("decorator", "none");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); 
		
		// 在内存中创建图象
		int width = 100, height = 30;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		
		// 获取图形上下文
		Graphics2D g = (Graphics2D)image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//生成随机类
		Random random = new Random();

		// 设定背景色
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);

		//设定字体
		Font font = null;

		//画边框
		//g.setColor(new Color());
		//g.drawRect(0,0,width-1,height-1);

		// 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		// 取随机产生的认证码(4位数字)
		StringBuffer sRand = new StringBuffer();
		String rand;
		for (int i = 0; i < 4; i++) {
			font = vcodeFonts.get(random.nextInt(vcodeFonts.size())).deriveFont(Font.BOLD, 20+random.nextInt(4));
			g.setFont(font);
			rand = String.valueOf((char)vcodeStr[random.nextInt(vcodeStr.length-1)]);
			// 将认证码显示到图象中
			//调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
			g.setColor(new Color(120 + random.nextInt(50), 120 + random
					.nextInt(50), 170 + random.nextInt(50)));
			g.drawString(rand, (font.getSize()-2) * i + 15, font.getSize()/2+6+random.nextInt(10));
		}

		for (int i = 0; i < 4; i++) {
			font = vcodeFonts.get(random.nextInt(vcodeFonts.size())).deriveFont(Font.BOLD, 24+random.nextInt(4));
			g.setFont(font);
			rand = String.valueOf((char)vcodeStr[random.nextInt(vcodeStr.length-1)]);
			sRand.append(rand);
			// 将认证码显示到图象中
			//调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
			g.setColor(new Color(10 + random.nextInt(80), 10 + random
					.nextInt(80), 10 + random.nextInt(200)));
			
			g.drawString(rand, (font.getSize()-4) * i + 8, font.getSize()/2+6+random.nextInt(10));
		}

		// 将认证码存入SESSION
		//session.setAttribute(sessionName, sRand);
	 
		// 图象生效
		g.dispose();

		// 输出图象到页面
		try {
			ImageIO.write(image, "JPEG", response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	Color getRandColor(int fc, int bc) {//给定范围获得随机颜色
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
}
