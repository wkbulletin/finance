package com.huacai.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import javax.imageio.ImageIO;
	/**
	 * 对图片的操作
	 * 
	 * @author 
	 * 
	 */
public class ImageUtil {
	    // 添加字体的属性值
	    private Font font = new Font("", Font.PLAIN, 12);
	 
	    private Graphics2D g = null;
	 
	    private int fontsize = 0;
	 
	    private int x = 0;
	 
	    private int y = 0;
	 
	    private String destDir = "F:\\";
	 
	    public void setDestDir(String path) {
	        this.destDir = path;
	        // 目录不存在
	        File file = new File(path);
	        if (!file.exists()) {
	            file.mkdir();
	        }
	    }
	 
	    /**
	     * 导入本地图片到缓冲中
	     * 
	     * @param imgPath
	     * @return
	     */
	    public BufferedImage loadImageLocal(String imgPath) {
	        try {
	            return ImageIO.read(new File(imgPath));
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	            return null;
	        }
	    }
	 
	    /**
	     * 读取网络中的图片到缓冲
	     * 
	     * @param imgPath
	     * @return
	     */
	    public BufferedImage loadImageUrl(String imgPath) {
	        URL url;
	        try {
	            url = new URL(imgPath);
	            return ImageIO.read(url);
	        } catch (MalformedURLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	            return null;
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	            return null;
	        }
	    }
	 
	    /**
	     * 生成新图片到本地
	     * 
	     * @param newImage
	     *            新名字
	     * @param img
	     *            缓冲
	     * @param type
	     *            类别
	     */
	    public void writeImageLocal(BufferedImage img, String type) {
	        if (img != null) {
	            File outputFile = new File(this.destDir + new Date().getTime()
	                    + "." + type);
	            try {
	                ImageIO.write(img, type, outputFile);
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	    }
	 
	    /**
	     * 设置文字字体，大小
	     * 
	     * @param fontStyle
	     * @param fontSize
	     */
	    public void setFont(String fontStyle, int fontSize) {
	        this.fontsize = fontSize;
	        this.font = new Font(fontStyle, Font.PLAIN, fontSize);
	    }
	 
	    /**
	     * 单行文本
	     * 
	     * @param img
	     * @param content
	     * @param x
	     * @param y
	     * @return
	     */
	    public BufferedImage modifyImage(BufferedImage img, Object content) {
	        try {
	            int w = img.getWidth();
	            int h = img.getHeight();
	 
	            g = img.createGraphics();
	            g.setColor(Color.RED);
	            if (this.font != null) {
	                g.setFont(this.font);
	            }
	 
	            // 文字输出位置
	            this.y = h - fontsize - 10;
	            this.x = w - 70;
	 
	            if (content != null) {
	                g.drawString(content.toString(), this.x, this.y);
	            }
	            g.dispose();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	 
	        return img;
	    }
	 
	    /**
	     * 将两张图片合并
	     * 
	     * @param base
	     * @param dest
	     *            目标
	     * @return
	     */
	    public BufferedImage modifyImageTogether(BufferedImage base,
	            BufferedImage dest) {
	        try {
	            int w = base.getWidth();
	            int h = base.getHeight();
	 
	            int dw = dest.getWidth();
	            int dh = dest.getHeight();
	 
	            g = dest.createGraphics();
	            AlphaComposite cp = AlphaComposite.getInstance(
	                    AlphaComposite.SRC_OVER, 1f);
	            g.setComposite(cp);
	            g.drawImage(base, (dw - w)/2,(dh - h)/2, w, h, null);
	            g.dispose();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return dest;
	    }
	 
	    /**
	     * 改变图片大小
	     * 
	     * @param img
	     * @param weight
	     * @param height
	     * @return
	     */
	    public BufferedImage modifySize(BufferedImage img, int width, int height) {
	        try {
	            int w = img.getWidth();
	            int h = img.getHeight();
	 
	            double wRation = (new Integer(width)).doubleValue() / w;
	            double hRation = (new Integer(height)).doubleValue() / h;
	            Image image = img.getScaledInstance(width, height,
	                    Image.SCALE_SMOOTH);
	 
	            AffineTransformOp op = new AffineTransformOp(AffineTransform
	                    .getScaleInstance(wRation, hRation), null);
	 
	            image = op.filter(img, null);
	 
	            img = (BufferedImage) image;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	 
	        return img;
	    }
	 
	    public BufferedImage changeSize(BufferedImage img, int width, int height) {
	        try {
	            BufferedImage distin = new BufferedImage(width, height,
	                    BufferedImage.TYPE_INT_RGB);
	            g = distin.createGraphics();
	            g.drawImage(img, 0, 0, width, height, null);
	            return distin;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	 
	    /**
	     * 截图
	     * 
	     * @param img
	     * @param x
	     * @param y
	     * @param width
	     * @param height
	     * @return
	     */
	    public BufferedImage cutImage(BufferedImage img, int x, int y, int width,
	            int height) {
	        BufferedImage distin = new BufferedImage(width, height,
	                BufferedImage.TYPE_INT_RGB);
	        try {
	            g = distin.createGraphics();
	            // 原图的大小，以及截图的坐标及大小
	            g.drawImage(img, 0, 0, width, height, x, y, width, height, null);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return distin;
	    }
	 
	    /**
	     * 测试
	     * 
	     * @param args
	     */
	    public static void main(String[] args) {
	    	ImageUtil ic = new ImageUtil();
	 
	        // 两张图片合并
	        BufferedImage d = ic
	                .loadImageLocal("C:\\Documents and Settings\\yangyz\\Desktop\\MyAPP\\catergory_list_right.jpg");
	        BufferedImage b = ic
	                .loadImageLocal("C:\\Documents and Settings\\yangyz\\Desktop\\MyAPP\\ico_exPlore_home_sports_bundles_selected.png");
	        ic.setDestDir("f:\\temp\\");
	        ic.writeImageLocal(ic.modifyImageTogether(b, d), "jpg");
	 
	        // 在向生成的图片添加文字
	        ic.modifyImage(d, "Troy Young");
	        // // 根据坐标截图
	        // BufferedImage d = ic
	        // .loadImageLocal("C:\\Documents and Settings\\yangyz\\Desktop\\MyAPP\\catergory_list_right.jpg");
	        // ic.setDestDir("f:\\temp\\");
	        ic.writeImageLocal(ic.cutImage(d, 30, 20, 200, 100), "jpg");
	        // 修改图片大小
	        // BufferedImage d = ic
	        // .loadImageLocal("C:\\Documents and Settings\\yangyz\\Desktop\\MyAPP\\catergory_list_right.jpg");
	        // ic.setDestDir("f:\\temp\\");
	        ic.writeImageLocal(ic.modifySize(d, 210, 175), "jpg");
	 
	        System.out.println("成功");
	    }
	}
