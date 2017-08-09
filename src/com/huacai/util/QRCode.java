package com.huacai.util;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QRCode {
	/** 
     * 生成二维码(QRCode)图片 
     * @param content 二维码图片的内容
     * @param imgPath 生成二维码图片完整的路径
     * @param ccbpath  二维码图片中间的logo路径
     */  
    public static int createQRCode(String content, String imgPath,String ccbPath,int width,int high) {  
        try {  
        Qrcode qrcodeHandler = new Qrcode();  
            //设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小  
            qrcodeHandler.setQrcodeErrorCorrect('H');  
            //N代表数字,A代表字符a-Z,B代表其他字符
            qrcodeHandler.setQrcodeEncodeMode('B'); 
            // 设置设置二维码尺寸，取值范围1-40，值越大尺寸越大，可存储的信息越大
          //，也象征着二维码的信息容量；二维码可以看成一个黑白方格矩阵，版本不同，矩阵长宽 //方向方格的总数量分别不同，
            //版本1为21*21矩阵，版本每增
          //  1，二维码的两个边长都增4；所以版本//7为45*45的矩阵；最高版本为是40，是177*177的矩阵；
            qrcodeHandler.setQrcodeVersion(4); 
            // 图片尺寸

            byte[] contentBytes = content.getBytes("utf-8");  
            BufferedImage bufImg = new BufferedImage(width,high, BufferedImage.TYPE_INT_RGB);  
            Graphics2D gs = bufImg.createGraphics();  
  
            gs.setBackground(Color.WHITE);  
            gs.clearRect(0, 0, width, high);  
  
            // 设定图像颜色 > BLACK  
            gs.setColor(Color.BLACK);  
  
            // 设置偏移量 不设置可能导致解析出错  
            int pixoff = 2;  
            // 输出内容 > 二维码  
            if (contentBytes.length > 0 && contentBytes.length <150) {  
                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);  
                for (int i = 0; i < codeOut.length; i++) {  
                    for (int j = 0; j < codeOut.length; j++) {  
                        if (codeOut[j][i]) {  
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);  
                        }  
                    }  
                }  
            } else {  
                System.err.println("QRCode content bytes length = "  
                        + contentBytes.length + " not in [ 0,125]. ");  
                return -1;
            }  
            Image img = ImageIO.read(new File(ccbPath));//实例化一个Image对象。
            
            int logo_width = img.getWidth(null);
            int logo_height = img.getHeight(null);
            System.out.println("logo_width=="+logo_width);
            System.out.println("logo_height=="+logo_height);
            
            gs.drawImage(img,(width-logo_width)/2,(high-logo_height)/2, null);
            gs.dispose();  
            bufImg.flush();  
  
            // 生成二维码QRCode图片  
            File imgFile = new File(imgPath);  
            ImageIO.write(bufImg, "png", imgFile);  
  
        } catch (Exception e) {  
            e.printStackTrace();  
            return -100;
        }  
        
        return 0;
    }  

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	    String imgPath = "D://work//jsWorkRoom//O2OYXPT//xf.png"; 
	    String imgPath1 = "D://work//jsWorkRoom//O2OYXPT//WebContent//tpl//default//images//30.png";
	                       //D:\work\jsWorkRoom\O2OYXPT\WebContent\tpl\default\images
	    String encoderContent = "http://www.163.com";
	    QRCode logo_Two_Code = new QRCode();
	    logo_Two_Code.createQRCode(encoderContent, imgPath, imgPath1,103,103);
	    zoomInImage("D://work//jsWorkRoom//O2OYXPT//xf.png","D://work//jsWorkRoom//O2OYXPT//mxf.png",10);
	    
    	ImageUtil ic = new ImageUtil();
        // 两张图片合并
        BufferedImage d = ic
                .loadImageLocal("D://work//jsWorkRoom//O2OYXPT//mxf.png");
        BufferedImage b = ic
                .loadImageLocal("D://work//jsWorkRoom//O2OYXPT//WebContent//tpl//default//images//1000.png");
        ic.setDestDir("D:\\temp\\");
        ic.writeImageLocal(ic.modifyImageTogether(b, d), "png");

	}

	/**

     * 对图片进行放大

     * @param originalImage 原始图片

     * @param times 放大倍数

     * @return

     */

    public static BufferedImage  zoomInImage(BufferedImage  originalImage, Integer times){

        int width = originalImage.getWidth()*times;

        int height = originalImage.getHeight()*times;

        BufferedImage newImage = new BufferedImage(width,height,originalImage.getType());

        Graphics g = newImage.getGraphics();

        g.drawImage(originalImage, 0,0,width,height,null);

        g.dispose();

        return newImage;

    }

    /**

     * 对图片进行放大

     * @param srcPath 原始图片路径(绝对路径)

     * @param newPath 放大后图片路径（绝对路径）

     * @param times 放大倍数

     * @return 是否放大成功

     */

    public static boolean zoomInImage(String srcPath,String newPath,Integer times){

        BufferedImage bufferedImage = null;

        try {

            File of = new File(srcPath);

            if(of.canRead()){

                bufferedImage =  ImageIO.read(of);

            }

        } catch (IOException e) {

            //TODO: 打印日志

            return false;

        }

        if(bufferedImage != null){

            bufferedImage = zoomInImage(bufferedImage,times);

            try {

                //TODO: 这个保存路径需要配置下子好一点

                ImageIO.write(bufferedImage, "PNG", new File(newPath)); //保存修改后的图像,全部保存为JPG格式

            } catch (IOException e) {

                // TODO 打印错误信息

                return false;

            }

        }

        return true;

    }

    /**

     * 对图片进行缩小

     * @param originalImage 原始图片

     * @param times 缩小倍数

     * @return 缩小后的Image

     */

    public static BufferedImage  zoomOutImage(BufferedImage  originalImage, Integer times){

        int width = originalImage.getWidth()/times;

        int height = originalImage.getHeight()/times;

        BufferedImage newImage = new BufferedImage(width,height,originalImage.getType());

        Graphics g = newImage.getGraphics();

        g.drawImage(originalImage, 0,0,width,height,null);

        g.dispose();

        return newImage;

    }

    /**

     * 对图片进行缩小

     * @param srcPath 源图片路径（绝对路径）

     * @param newPath 新图片路径（绝对路径）

     * @param times 缩小倍数

     * @return 保存是否成功

     */

    public static boolean zoomOutImage(String srcPath,String newPath,Integer times){

        BufferedImage bufferedImage = null;

        try {

            File of = new File(srcPath);

            if(of.canRead()){

                bufferedImage =  ImageIO.read(of);

            }

        } catch (IOException e) {

            //TODO: 打印日志

            return false;

        }

        if(bufferedImage != null){

            bufferedImage = zoomOutImage(bufferedImage,times);

            try {

                //TODO: 这个保存路径需要配置下子好一点

                ImageIO.write(bufferedImage, "JPG", new File(newPath)); //保存修改后的图像,全部保存为JPG格式

            } catch (IOException e) {

                // TODO 打印错误信息

                return false;

            }

        }

        return true;

    }

//    public static void main(String[] args) {
//
//        boolean testIn = zoomInImage("E:/11.jpg","E:\\in.jpg",4);
//
//        if(testIn){
//
//            System.out.println("in ok");
//
//        }
//
//        boolean testOut = zoomOutImage("E:/11.jpg","E:\\out.jpg",4);
//
//        if(testOut){
//
//            System.out.println("out ok");
//
//        }
//
//    }
//}

	
}
