package com.huacai.util;

import java.util.HashMap;
import java.util.Map;

public interface MerchantContants {
	public static int MOINTOR_ADD = 1;
	public static int MOINTOR_DEL = 2;
	public static int MOINTOR_UPD = 3;
	public static int MOINTOR_SEL = 4;
	public static int MOINTOR_SH = 5;
	public static int MOINTOR_PLATFORMID = 13;
	public static int MOINTOR_STAUTS_SUC = 0;
	public static int MOINTOR_STAUTS_FAIL = 1;
	// 以上接口新增应用来源字段source,值如下：
	// 1.UC2.UCM3.BOSS4.BOSSWeb5.Server6.ServerWeb7.Game8.GameWeb9.Report10.ReportWeb11
	// .BusinessAnalysis12.BusinessAnalysisWeb13.Merchant14.MerchantWeb15.Website
	public static int MOINTOR_SOURCE_WEB = 14;
	public static int MOINTOR_SOURCE_SERVER = 13;

	// 0.关键事件
	// 1.通知事件
	// 2.警告事件
	// 3.告警事件
	public static int MOINTOR_EVENTTYPE_KEYEV = 0;
	public static int MOINTOR_EVENTTYPE_NOT = 1;
	public static int MOINTOR_EVENTTYPE_WARNING = 2;
	public static int MOINTOR_EVENTTYPE_ALARM = 3;
	//佣金结算事件
	public static int MOINTOR_EVENTTYPE_OTHER = 14;

	// 0.关键事件
	// 1.通知事件
	// 2.警告事件
	// 3.告警事件
	public static int MOINTOR_EVENTLEVEL_KEYEV = 0;
	public static int MOINTOR_EVENTLEVEL_NOT = 1;
	public static int MOINTOR_EVENTLEVEL_WARNING = 2;
	public static int MOINTOR_EVENTLEVEL_ALARM = 3;
	
	public static int MOINTOR_ACTIVE_TYPE_APP = 0;
	public static int MOINTOR_ACTIVE_TYPE_THREAD = 1;
	//Merchant14.MerchantWeb15
	public static String MOINTOR_APPNAME_WEB ="MerchantWeb";
	public static String MOINTOR_APPNAME_SERVER = "Merchant";
	
}
