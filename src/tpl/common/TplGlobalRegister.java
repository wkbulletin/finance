package tpl.common;

import java.util.Date;

import com.huacai.web.constant.LotteryInfo;

import libcore.util.ArrayUtil;
import libcore.util.DateSimpleUtil;
import libcore.util.DateUtil;
import libcore.util.StringUtil;
import libcore.util.VarUtil;
import webit.script.Context;
import webit.script.global.GlobalManager;
import webit.script.global.GlobalRegister;
import webit.script.lang.Bag;
import webit.script.lang.MethodDeclare;

public class TplGlobalRegister implements GlobalRegister {

	@Override
	public void regist(GlobalManager manager) {
		// 全局变量
		//SimpleBag globalBag = manager.getGlobalBag();

		// 全局常量
		Bag constBag = manager.getConstBag();

		// 全局Native 函数
		//constBag.set("new_list", this.nativeFactory.createNativeConstructorDeclare(ArrayList.class, null));

		// 全局自定函数
		
		//字符串替换
		//str_replace(String text, String searchString, String replacement)
		constBag.set("str_replace", new MethodDeclare() {
			public Object invoke(Context context, Object[] args) {
				//String text, String searchString, String replacement
				if(args[0]==null)return "";
				return StringUtil.replace(String.valueOf(args[0]), String.valueOf(args[1]), String.valueOf(args[2]));
			}
		});
		
		//字符串截取
		//cutStr(String str, int toCount, String more)
		//cutStr(String str, int toCount)
		constBag.set("cutStr", new MethodDeclare() {
			public Object invoke(Context context, Object[] args) {
				//cutStr(String str, int toCount, String more)
				if(args[0]==null)return "";
				if(args.length == 3){
					return StringUtil.cutStr(String.valueOf(args[0]), Integer.valueOf(String.valueOf(args[1])), String.valueOf(args[2]));
				}else{
					return StringUtil.cutStr(String.valueOf(args[0]), Integer.valueOf(String.valueOf(args[1])), "");
				}
				
			}
		});
		
		//格式化金额:将分格式化为元
		//formatFenMoney(String money, String format)
		//formatFenMoney(String money)
		constBag.set("formatFenMoney", new MethodDeclare() {
			public Object invoke(Context context, Object[] args) {
				//formatFenMoney(String money, String format)
				if(args.length == 2){
					return StringUtil.formatFenMoney(String.valueOf(args[0]), String.valueOf(args[1]));
				}else{
					return StringUtil.formatFenMoney(String.valueOf(args[0]));
				}
				
			}
		});
		//格式化金额
		//formatMoney(String money, String format)
		//formatMoney(String money)
		constBag.set("formatMoney", new MethodDeclare() {
			public Object invoke(Context context, Object[] args) {
				//formatMoney(String money, String format)
				if(args[0]==null)return "";
				if(args.length == 2){
					return StringUtil.formatMoney(String.valueOf(args[0]), String.valueOf(args[1]));
				}else{
					return StringUtil.formatMoney(String.valueOf(args[0]));
				}
				
			}
		});		
		//格式化比率
		//formatRate(String sp, String format)
		//formatRate(String sp)
		constBag.set("formatRate", new MethodDeclare() {
			public Object invoke(Context context, Object[] args) {
				//formatRate(String sp, String format)
				if(args[0]==null)return "";
				if(args.length == 2){
					return StringUtil.formatRate(String.valueOf(args[0]), String.valueOf(args[1]));
				}else{
					return StringUtil.formatRate(String.valueOf(args[0]));
				}
				
			}
		});
		//字符串补齐
		//strPad(String str, int fillLen, String fillStr)
		//strPad(String str, int fillLen)
		constBag.set("strPad", new MethodDeclare() {
			public Object invoke(Context context, Object[] args) {
				//strPad(String str, int fillLen, String fillStr)
				if(args[0]==null)return "";
				if(args.length == 3){
					return StringUtil.strPad(String.valueOf(args[0]), Integer.valueOf(String.valueOf(args[1])), String.valueOf(args[2]));
				}else{
					return StringUtil.strPad(String.valueOf(args[0]), Integer.valueOf(String.valueOf(args[1])), "0");
				}
			}
		});
		//日期格式化
		//time是10位的时间戳, format:Y-m-d H:i:s
		//date(String format, String time)
		//date(String format)
		//date()
		constBag.set("date", new MethodDeclare() {
			public Object invoke(Context context, Object[] args) {
				//date(String format, String time)
				if(args.length == 2){
					if(args[0]==null)return "";
					if (args[1] instanceof Date){
						return DateSimpleUtil.date(String.valueOf(args[0]), (Date)args[1]);
					}else{
						return DateSimpleUtil.date(String.valueOf(args[0]), String.valueOf(args[1]));
					}
				}else if(args.length == 1){
					if(args[0]==null)return "";
					return DateSimpleUtil.date(String.valueOf(args[0]));
				}else{
					return DateSimpleUtil.date();
				}
			}
		});
		//10位时间戳
		//time(String date, String format)
		//time(String date)
		//time()
		constBag.set("time", new MethodDeclare() {
			public Object invoke(Context context, Object[] args) {
				//time(String date, String format)
				if(args.length == 2){
					if(args[0]==null)return "";
					return DateSimpleUtil.time(String.valueOf(args[0]), String.valueOf(args[1]));
				}else if(args.length == 1){
					if(args[0]==null)return "";
					return DateSimpleUtil.time(String.valueOf(args[0]));
				}else{
					return DateSimpleUtil.time();
				}
			}
		});

		
		//日期格式化2--java格式
		//time是13位的时间戳, format:yyyy-MM-dd HH:mm:ss
		//getDate(String time, String format)
		//getDate(String format)
		//getDate()
		constBag.set("getDate", new MethodDeclare() {
			public Object invoke(Context context, Object[] args) {
				//getDate(String format, String time)
				if(args.length == 2){
					if(args[0]==null)return "";
					if (args[0] instanceof Date){
						return DateUtil.getDate((Date)args[0], String.valueOf(args[1]));
					}else{
						return DateUtil.getDate(String.valueOf(args[0]), String.valueOf(args[1]));
					}
				}else if(args.length == 1){
					if(args[0]==null)return "";
					return DateUtil.getDate(String.valueOf(args[0]));
				}else{
					return DateUtil.getDate();
				}
			}
		});
		//13位时间戳--java格式
		//getTime(String date, String format)
		//getTime(String date)
		//getTime()
		constBag.set("getTime", new MethodDeclare() {
			public Object invoke(Context context, Object[] args) {
				//getTime(String date, String format)
				if(args.length == 2){
					if(args[0]==null)return "";
					return DateUtil.getTime(String.valueOf(args[0]), String.valueOf(args[1]));
				}else if(args.length == 1){
					if(args[0]==null)return "";
					return DateUtil.getTime(String.valueOf(args[0]));
				}else{
					return DateUtil.getTime();
				}
			}
		});
		
		
		//日期转换格式--简化format
		//date是日期字符串, format:Y-m-d H:i:s
		//date2date(String date, String format)
		constBag.set("date2date", new MethodDeclare() {
			public Object invoke(Context context, Object[] args) {
				//date2date(String date, String format)
				if(args[0]==null)return "";
				return DateSimpleUtil.date2date(String.valueOf(args[0]), String.valueOf(args[1]));
			}
		});
		


		//日期转换格式--java format
		//date是日期字符串, format:yyyy-MM-dd HH:mm:ss
		//dateToDate(String date, String format)
		constBag.set("dateToDate", new MethodDeclare() {
			public Object invoke(Context context, Object[] args) {
				//dateToDate(String date, String format)
				if(args[0]==null)return "";
				return DateUtil.dateToDate(String.valueOf(args[0]), String.valueOf(args[1]));
			}
		});
		
		//字符串转换为 int
		//intval(String str, int def)
		//intval(String str)
		constBag.set("intval", new MethodDeclare() {
			public Object invoke(Context context, Object[] args) {
				//intval(String str, int def)
				if(args.length == 2){
					return VarUtil.intval(String.valueOf(args[0]), Integer.valueOf(String.valueOf(args[1])));
				}else{
					return VarUtil.intval(String.valueOf(args[0]));
				}
			}
		});

		//字符串转换为 long
		//longval(String str, long def)
		//longval(String str)
		constBag.set("longval", new MethodDeclare() {
			public Object invoke(Context context, Object[] args) {
				//longval(String str, long def)
				if(args.length == 2){
					return VarUtil.longval(String.valueOf(args[0]), Long.valueOf(String.valueOf(args[1])));
				}else{
					return VarUtil.longval(String.valueOf(args[0]));
				}
			}
		});

		//字符串转换为 float
		//floatval(String str, float def)
		//floatval(String str)
		constBag.set("floatval", new MethodDeclare() {
			public Object invoke(Context context, Object[] args) {
				//floatval(String str, float def)
				if(args.length == 2){
					return VarUtil.floatval(String.valueOf(args[0]), Float.valueOf(String.valueOf(args[1])));
				}else{
					return VarUtil.floatval(String.valueOf(args[0]));
				}
			}
		});

		//字符串转换为 double
		//doubleval(String str, float def)
		//doubleval(String str)
		constBag.set("doubleval", new MethodDeclare() {
			public Object invoke(Context context, Object[] args) {
				//doubleval(String str, double def)
				if(args.length == 2){
					return VarUtil.doubleval(String.valueOf(args[0]), Double.valueOf(String.valueOf(args[1])));
				}else{
					return VarUtil.doubleval(String.valueOf(args[0]));
				}
			}
		});
		

		//字符串格式化-判断为null返回长度为0的字符串
		//strval(String str)
		constBag.set("doubleval", new MethodDeclare() {
			public Object invoke(Context context, Object[] args) {
				//strval(String str)
				return VarUtil.strval(String.valueOf(args[0]));
			}
		});
		
		
		//判断字符串是否在数组中
		//strInArray(String str)
		constBag.set("strInArray", new MethodDeclare() {
			public Object invoke(Context context, Object[] args) {
				//strInArray(String str, String[] arr)
				return ArrayUtil.inArray(String.valueOf(args[0]), (String[])args[1]);
			}
		});
		//判断数字是否在数组中
		//intInArray(String str)
		constBag.set("intInArray", new MethodDeclare() {
			public Object invoke(Context context, Object[] args) {
				//intInArray(int str, int[] arr)
				return ArrayUtil.inArray(Integer.valueOf(String.valueOf(args[0])), (int[])args[1]);
			}
		});
		
		/**
		 * id转换采种信息，李祖荣添加彩种id转换彩种名
		 */
		constBag.set("toLottInfo", new MethodDeclare() {
			public Object invoke(Context context, Object[] args) {
				if(args[0]==null)return "";
     				return LotteryInfo.getLotName(Integer.valueOf(String.valueOf(args[0])));
			}
		});

	}

}
