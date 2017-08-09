package com.huacai.web.controller.merchant;

import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.huacai.web.bean.privilege.AdminInfo;
import com.huacai.web.common.LoginCommon;
import com.huacai.web.controller.BaseController;
import com.huacai.web.dao.merchant.QRCodeDao;

/**
 * 二维码管理
 * @author 
 *
 */
@Controller
@RequestMapping("/qrCode")
@Scope("prototype")
public class QRCodeController extends BaseController {
	protected final static Logger logger = LogManager.getLogger(QRCodeController.class);

	@Autowired
	private QRCodeDao qRCodeDao;
	/**
 	 * action 下载
 	 * @param model
 	 * @return
 	 */
 	@RequestMapping("show_qr_code")
	public String showQrCode(Map<String, Object> model) {
 		
 		AdminInfo agencyInfo = LoginCommon.getAdminInfo(request);
 		logger.info("二维码展现==AgencyInfo="+agencyInfo.getAdminId());
 		if(agencyInfo!=null){
 			JSONObject jsonObject=qRCodeDao.getJstco2oQrcodeOriurls(-1);
 			if(jsonObject!=null){
 				model.put("shorturl",jsonObject.get("clinkurl"));

 				model.putAll(jsonObject);
 	 			model.put("noshow",1);
 			}
 		}

		return "qrcode/qr_code";
	}
	
}
