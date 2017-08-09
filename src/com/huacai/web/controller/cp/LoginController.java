package com.huacai.web.controller.cp;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import libcore.util.NetUtil;
import libcore.util.VarUtil;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.huacai.web.bean.privilege.AdminInfo;
import com.huacai.web.common.LoginCommon;
import com.huacai.web.common.SysLogConstants;
import com.huacai.web.controller.BaseController;
import com.huacai.web.dao.cp.CpDao;
import com.huacai.web.dao.syslog.LogLoginDao;

@Controller
@RequestMapping
@Scope("prototype")
public class LoginController extends BaseController {
	protected final static Logger logger = LogManager.getLogger(LoginController.class);
	@Autowired
	private CpDao cpDao;
	
	@Autowired
	private LogLoginDao logLoginDao;
	
	/**
	 * 登录
	 * @param model
	 * @return
	 */
	@RequestMapping("/login")
	public String login(Map<String, Object> model) {
		String act = VarUtil.RequestStr(request, "act");
		if(act.equals("check")){
			//检测验证码
			
			int ret=VarUtil.RequestInt(request, "ret");
			
			String vcode = VarUtil.RequestStr(request, "vcode");
			String sessVcode = VarUtil.strval((String)request.getSession(true).getAttribute(LoginCommon.SESSION_KEY_LOGIN_VCODE));
			if(ret==1 && (vcode.length() == 0 || !vcode.equals(sessVcode))){
				logger.error("验证码错误:vcode="+vcode+", sessVcode="+sessVcode);
				LoginCommon.clearLoginSession(request);
				return result(model, getAjaxMessage(0, "验证码错误"));
			}
			
			String account = VarUtil.RequestStr(request, "account");
			String password = VarUtil.RequestStr(request, "password");
			
			JSONObject resp = cpDao.checkLogin(request, account, password);
			String ip = NetUtil.getIpAddr(request);
			if(resp.getIntValue("result") == 1){
				AdminInfo adminInfo = LoginCommon.regAdminInfoSession(request, cpDao, resp);
				if(adminInfo.getPrivilegeList().size() == 0){
					LoginCommon.clearLoginSession(request);
					logger.error("登录失败-权限错误:account="+account);
					logLoginDao.add(0, account, "",SysLogConstants.LOGIN_TYPE_LOGIN, SysLogConstants.LOGIN_STATUS_FAILED, "登录失败-权限错误", ip);
					
					return result(model, getAjaxMessage(0, "权限错误，请联系管理员"));
				}
//				int BOSS_APP_ID = VarUtil.intval(ReadResource.get("BOSS_APP_ID"));
				String typePrivList = "0,1,11";
				//com.huacai.web.	.privilege.AdminController.adminTypes
//				switch(BOSS_APP_ID){
//				case SysLogConstants.BOSS_APP_WEBBOSS://业务管理应用
//					typePrivList = "0,1,6,7,8";
//					break;
//				case SysLogConstants.BOSS_APP_WEBDJLJ://大奖领奖应用
//					typePrivList = "1,2,3,4,8";
//					break;
//				case SysLogConstants.BOSS_APP_WEBZHBB://综合报表应用
//					typePrivList = "1,2,3,4,6,7,8";
//					break;
//				case SysLogConstants.BOSS_APP_WEBZHJK://综合监控应用
//					typePrivList = "1,8";
//					break;
//				}
				if(typePrivList.indexOf(adminInfo.getAdminType()+"") == -1){
					LoginCommon.clearLoginSession(request);
					logger.error("登录失败-用户类型不匹配:account="+account+",AdminType="+adminInfo.getAdminType()+",允许登录type="+typePrivList);
					logLoginDao.add(0, account, "",SysLogConstants.LOGIN_TYPE_LOGIN, SysLogConstants.LOGIN_STATUS_FAILED, "登录失败-用户类型不匹配", ip);
					return result(model, getAjaxMessage(0, "用户类型不匹配，请联系管理员"));
				}
				
				LoginCommon.addUserLoginStatus(request, adminInfo);
				
				logLoginDao.add(adminInfo.getAdminId(),adminInfo.getAdminLoginname(),adminInfo.getAdminRealname(), SysLogConstants.LOGIN_TYPE_LOGIN,SysLogConstants.LOGIN_STATUS_SUCCESS, "登录成功", ip);
				//System.out.println(resp);
			}else{
				LoginCommon.clearLoginSession(request);
				logger.error("登录失败-"+resp.getString("message")+":account="+account);
				logLoginDao.add(0, account, "", SysLogConstants.LOGIN_TYPE_LOGIN,SysLogConstants.LOGIN_STATUS_FAILED, "登录失败-"+resp.getString("message"), ip);
			}
			
			
			return result(model, resp.toJSONString());
		}
		
		return "/cp/login";
	}
	
	/**
	 * 验证码
	 * @param model
	 * @throws IOException
	 */
	@RequestMapping("vcode")
	public void vcode(Map<String, Object> model) throws IOException {
		
		//设置页面不缓存
		request.setAttribute("decorator", "none");
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); 
		
		// 在内存中创建图象
		int width = 80, height = 30;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		// 获取图形上下文
		Graphics g = image.getGraphics();

		//生成随机类
		Random random = new Random();

		// 设定背景色
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);

		//设定字体
		g.setFont(new Font("Times New Roman", Font.PLAIN, 32));

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
		String sRand = "";
		for (int i = 0; i < 4; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			// 将认证码显示到图象中
			g.setColor(new Color(20 + random.nextInt(110), 20 + random
					.nextInt(110), 20 + random.nextInt(110)));//调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
			g.drawString(rand, 18 * i + 6, 26);
		}

		// 将认证码存入SESSION
		session.setAttribute(LoginCommon.SESSION_KEY_LOGIN_VCODE, sRand);

		// 图象生效
		g.dispose();

		// 输出图象到页面
		ImageIO.write(image, "JPEG", response.getOutputStream());
		
		
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
	
	/**
	 * 登录信息更新
	 * @param model
	 * @return
	 */
	@RequestMapping("/login_update")
	public String login_update(Map<String, Object> model) {
		if(!LoginCommon.isLogin(request)){
			return result(model, "error");
		}
		AdminInfo adminInfo = LoginCommon.getAdminInfo(request);
		if(adminInfo!=null&&adminInfo.getAdminId()>0){
			JSONObject json = cpDao.getAdminInfo(adminInfo.getAdminLoginname());
			LoginCommon.regAdminInfoSession(request, cpDao, json);
			return result(model, "ok");
		}
		return result(model, "error");
	}
	@RequestMapping("/login_status")
	public String login_status(Map<String, Object> model) {
		AdminInfo adminInfo = LoginCommon.updateUserLoginStatus(request);
		if(adminInfo!=null){
			request.getSession(true).setAttribute(LoginCommon.SESSION_KEY_LOGIN_INFO, adminInfo);
		}
		JSONObject json = new JSONObject();
		json.put("time", System.currentTimeMillis());
		return result(model, json.toJSONString());
	}
	@RequestMapping("/logout")
	public String logout(Map<String, Object> model) {
		AdminInfo adminInfo = LoginCommon.updateUserLoginStatus(request);
		if(adminInfo!=null){
			String ip = NetUtil.getIpAddr(request);
			logLoginDao.add(adminInfo.getAdminId(), adminInfo.getAdminLoginname(), adminInfo.getAdminRealname(),SysLogConstants.LOGIN_TYPE_LOGOUT, SysLogConstants.LOGIN_STATUS_SUCCESS, "退出成功", ip);
		}
		
		LoginCommon.clearLoginSession(request);
		return "redirect:/";
	}
}
