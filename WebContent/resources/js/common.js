var Browser = {
	IE:     navigator.userAgent.indexOf('MSIE') > 0,
	IE6:    navigator.userAgent.indexOf('MSIE 4.') > 0 || navigator.userAgent.indexOf('MSIE 5.') > 0 || navigator.userAgent.indexOf('MSIE 6.') > 0,
	IE7:    navigator.userAgent.indexOf('MSIE 7.') > 0,
	IE8:    navigator.userAgent.indexOf('MSIE 8.') > 0,
	IE9:    navigator.userAgent.indexOf('MSIE 9.') > 0,
	IE10:    navigator.userAgent.indexOf('MSIE 10.') > 0,
	Opera:  !!window.opera,
	WebKit: navigator.userAgent.indexOf('AppleWebKit/') > -1,
	Gecko:  navigator.userAgent.indexOf('Gecko') > -1 && navigator.userAgent.indexOf('KHTML') == -1,
	Android:  navigator.userAgent.indexOf('Android') > -1,
	iPhone:  navigator.userAgent.indexOf('iPhone') > -1,
	iPad:  navigator.userAgent.indexOf('iPad') > -1,
	Firefox:  navigator.userAgent.indexOf('Firefox') > -1
}
if(Browser.IE){
	$.ajaxSetup({
		cache:false
	});
}

//自定义confirm
function my_confirm(msg, fun_ok, fun_cancel) {
	if (window.frameElement != null && window.top && window.top.my_confirm) {
		parent.my_confirm(msg, fun_ok, fun_cancel);
	} else {
		var conf = {
			dialog: {msg: msg, type: -1, btns: 2, yes: function (index){
				if(fun_ok)fun_ok();
				layer.close(index);
				
			}, no: function (index){
				if(fun_cancel)fun_cancel();
				layer.close(index);
			}}
		};
		return $.layer(conf); 
	}

}

//自定义alert
function my_alert(msg, fun_ok) {
	if (window.frameElement != null && parent && parent.my_alert) {
		parent.my_alert(msg, fun_ok);
	} else {
		if(msg.len() < 40)msg=str_pad_right(msg, 40-msg.len(), "&nbsp;");
		layer.alert(msg, -1, function (index){
			if(fun_ok)fun_ok();
			layer.close(index);
		});
	}

}

//自定义prompt
function my_prompt(msg, fun_ok) {
	if (window.frameElement != null && parent && parent.my_prompt) {
		parent.my_prompt(msg, fun_ok);
	} else {
		layer.prompt({
				title: msg,
				formType: 1 //prompt风格，支持0-2
			}, 
			function(value, index, elem){
				if(fun_ok)fun_ok(value);
			});
	}
}

//自定义notice
function my_notice(msg, fun_ok) {
	if (window.frameElement != null && parent && parent.my_prompt) {
		parent.my_notice(msg, fun_ok);
	} else {
		layer.msg(msg, 3, -1,function(){
			//location.reload(); //自动关闭后可做一些刷新页面等操作
			if(fun_ok)fun_ok();
		});
	}



}
//自定义msg
function my_msg(msg, t, fun_ok) {
	if (window.frameElement != null && parent && parent.my_prompt) {
		parent.my_msg(msg,t, fun_ok);
	} else {
		layer.msg(msg, t, {
			type: -1 
		},function(){
			if(fun_ok)fun_ok();
		});
	}
}



//图片预加载
function preload_images(arr) {
	var d=document; 
	if(d.images){ 
		if(!d.preload_pics) d.preload_pics=new Array();
		var i,j=d.preload_pics.length,a=arr; 
		for(i=0; i<a.length; i++){
			if (a[i].indexOf("#")!=0){ 
				d.preload_pics[j]=new Image; 
				d.preload_pics[j].src=a[i];
				j++;
			}
		}
	}
}


//ajax
/**
 * 
 */
function my_ajax_json(url, data, success_fun, error_fun) {
	$.ajax({
		type : "POST",
		url : url,
		data : data,
		dataType : "json",
		success : success_fun ? success_fun : function(msg) {},
		error : error_fun ? error_fun : function(msg) {}
	});
}

function trim(value) {
	var temp = value;
	var obj = /^(\s*)([\W\w]*)(\b\s*$)/;
	if (obj.test(temp)) {
		temp = temp.replace(obj, '$2'); 
	}
	obj = /^(\s*)$/;
	if (obj.test(temp)) {
		temp = ''; 
	}

	return temp;
}

 
//字符串填充
function str_pad(t, len, fill){
	t = t+"";
	for(var i = t.length; i < len; i++){
		t = fill+t;
	}
	return t;
}
function str_pad_right(t, len, fill){
	t = t+"";
	for(var i = t.length; i < len; i++){
		t = t+fill;
	}
	return t;
}

//转换为整数
function toInt(str){
	var result = parseInt(str, 10);
	return isNaN(result) ? 0 : result;
}



//是否为数字
function isNum(num){
	var rule = /^\d+$/; 
	if(rule.test(num))return true;
	return false;
}


//数组随机排序
function arrayRand(Arr){
	Arr.sort(function(){return Math.random()>0.5?-1:1;});
	return Arr;
}
//取min-max区间的随机数
function rand(min, max){
	var Range = max - min; 
	var Rand = Math.random(); 
	return(min + Math.round(Rand * Range)); 
}
//判断是字符串是否在数组中存在
function inArray(v, arr){
	try{
		for(var i = 0; i < arr.length;i++){
			if(v == arr[i])return true;
		}
	}catch(e){}
	return false;
}
//判断是字符串是否在数组中存在
function searchArray(v, arr){
	try{
		for(var i = 0; i < arr.length;i++){
			var str = arr[i] + "";
			if(str.indexOf(v) != -1)return new Array(i, arr[i]);
		}
	}catch(e){}
	return false;
}

//删除数组中的一个元素
function arrayDeleteItem(v, arr){
	var new_arr = new Array();
	try{
		for(var i = 0; i < arr.length;i++){
			if(v != arr[i]){new_arr[new_arr.length] = arr[i]}
		}
	}catch(e){}
	return new_arr;
}
//取数组最大数
function arrayMaxNum(a){
	var arr = new Array();
	arr = arr.concat(a);
	arr = arr.sort(function(a, b){
		a = parseInt(a, 10);b = parseInt(b, 10);
		if(a<b)return -1;
		if(a==b)return 0;
		if(a>b)return 1;
	});
	arr = arr.reverse();
	return arr[0];
}

/**
 * 统计字符串长度，中文2的长度
 * @returns
 */
String.prototype.len = function() {
    return this.replace(/[^\x00-\xff]/g, "**").length;
};
/**
 * 字符串截取
 * @param len
 * @returns
 */
String.prototype.cutStr = function(n) {
	var r = /[^\x00-\xff]/g;
	
	if (this.replace(r, "**").length <= n) return this;
	var new_str = "";
	var str = "";
	for (var i=0,j = 0; i<this.length && j < n ; i++) {
		str = this.substr(i, 1);
		new_str+=str;
		j += (str.replace(r,"**").length > 1) ? 2 : 1;
	}
	return new_str + " ..";
};

/**
 * 替换字符串中的字符
 * @param str
 * @param replace_what
 * @param replace_with
 * @returns
 */
function str_replace(str, replace_what, replace_with) {
	var ndx = str.indexOf(replace_what);
	var delta = replace_with.length - replace_what.length;
	while (ndx >= 0) {
		str = str.substring(0, ndx) + replace_with + str.substring(ndx + replace_what.length);
		ndx = str.indexOf(replace_what, ndx + delta + 1);
	}
	return str;
}



//取get变量的值 var info = getParameter("info");
function getParameter( sProp ) {
	try {
		var str = document.location.search;
		str = unescape(str);
		var re = new RegExp( sProp + "=([^\&]*)", "i" );
		var a = re.exec( str );
		if ( a == null )
			return "";
		return a[1];
	} catch(e) {
		return "";
	}
};

// alert( readCookie("myCookie") );
function readCookie(name)
{
	var cookieValue = "";
	var search = name + "=";
	if(document.cookie.length > 0)
	{ 
		offset = document.cookie.indexOf(search);
		if (offset != -1)
		{ 
			offset += search.length;
			end = document.cookie.indexOf(";", offset);
			if (end == -1) end = document.cookie.length;
				cookieValue = unescape(document.cookie.substring(offset, end))
		}
	}
	return cookieValue;
}
//time=秒
function writeCookie(name, value, time)
{
	var expire = "";
	if(time != null)
	{//domain=.baidu.com
		expire = new Date((new Date()).getTime() + time * 1000);
		expire = "; path=/; expires=" + expire.toGMTString();
	}
	document.cookie = name + "=" + escape(value) + expire;
}

function getTime(){
	return (new Date()).getTime();
}

/**
 * 格式化时间, t=时间戳
 * @param type
 * @param t
 * @returns
 */
function get_date(type, t){
	if(t){ 
		var o = new Date(parseFloat(t) * 1000); 
	}else{
		var o = new Date();
	}
	var dd = new Array();
	dd['y'] = o.getYear();
	dd['Y'] = o.getFullYear();
	dd['n'] = (parseInt(o.getMonth())+1);
	dd['m'] = str_pad(dd['n'], 2, "0");
	dd['j'] = o.getDate();
	dd['d'] = str_pad(dd['j'], 2, "0");
	
	dd['g'] = o.getHours();//小时，12 小时格式，没有前导零 1 到 12 
	if(dd['g']>12)dd['g'] = dd['g'] - 12;
	dd['G'] = o.getHours() ;//小时，24 小时格式，没有前导零 0 到 23 
	dd['h'] = str_pad(dd['g'], 2, "0");//小时，12 小时格式，有前导零 01 到 12 
	dd['H'] = str_pad(dd['G'], 2, "0");//小时，24 小时格式，有前导零 00 到 23 

	dd['i'] = str_pad(o.getMinutes(), 2, "0");
	dd['s'] = str_pad(o.getSeconds(), 2, "0");
	dd['w'] = o.getDay();
	var x = new Array("星期日", "星期一", "星期二","星期三","星期四", "星期五","星期六");
	var w = new Array("周日", "周一", "周二","周三","周四", "周五","周六");

	dd['W'] = x[dd['w']];

	
	var date = dd['Y'] + '-' + dd['m'] + '-' + dd['d'];
	var time = dd['H'] + ':' + dd['i'] + ':' + dd['s'];
	var md = dd['m'] + '-' + dd['d'];
	switch(type){
		case 'date':
			return date;
		break;
		case 'm-d w':
			return  dd['m'] + '-' + dd['d'] +' '+ w[dd['w']];
		break;
		case 'w':
			return dd['W'];
		break;
		case 'time':
			return time;
		break;
		case '':
		case 'datetime':
			return date + ' ' + time;
		break;
		default:
			var str = "Y-m-d H:i:s";
			if(typeof(type) == "string")str = type;
			var arr = str.split("");
			str = "";
			for(var i in arr){
				str += dd[arr[i]] ? dd[arr[i]] : arr[i];
			}
			return str;
		break;
	}
}
//获取指定天数的日期，
/*
format是返回格式，如:Y-m-d
type=30day/7day/3day/yesterday/week_u/week/month/today
*/
function get_x_date(type, format){
	var d = Math.ceil((new Date()).getTime()/1000);
	if(!format || format.length == 0)format="Y-m-d";
	switch(type){
		case '30day'://30天
			d -= 29*86400;
		break;
		case '7day'://7天
			d -= 6*86400;
		break;
		case '3day'://3天
			d -= 2*86400;
		break;
		case 'yesterday'://昨天
			d -= 86400;
		break;

		case 'week_u'://当周第一天(周日为首)
			var date = new Date();
			var day = date.getDay();
			date.setDate(date.getDate() - day);
			d = Math.ceil(date.getTime()/1000);
		break;
		case 'week'://当周第一天(周一为首)
			var date = new Date();
			var day = date.getDay();
			if(day == 0)day=7;
			date.setDate(date.getDate() - (day - 1));
			d = Math.ceil(date.getTime()/1000);
		break;
		case 'month'://当月第一天
			var date = new Date();
			date.setDate(1);
			d = Math.ceil(date.getTime()/1000);
		break;
		case 'today'://今天
		default://如果type为数字可以加或减天数
			if(typeof(type) == "number"){
				if(type > 0){
					d += (type-1)*86400;
				}else{
					d += (type+1)*86400;
				}
			}
		break;
	}
	return get_date(format, d);
}
//获取指定天数的日期
/*
date 是开始时间
format是返回格式，如:Y-m-d
type=30day/7day/3day/yesterday/week_u/week/month/today
*/
function get_x_date2(date, type, format){
	var d = Math.ceil(date.getTime()/1000);
	if(!format || format.length == 0)format="Y-m-d";
	switch(type){
		case '30day'://30天
			d -= 29*86400;
		break;
		case '7day'://7天
			d -= 6*86400;
		break;
		case '3day'://3天
			d -= 2*86400;
		break;
		case 'yesterday'://昨天
			d -= 86400;
		break;

		case 'week_u'://当周第一天(周日为首)
			var date = new Date();
			var day = date.getDay();
			date.setDate(date.getDate() - day);
			d = Math.ceil(date.getTime()/1000);
		break;
		case 'week'://当周第一天(周一为首)
			var date = new Date();
			var day = date.getDay();
			if(day == 0)day=7;
			date.setDate(date.getDate() - (day - 1));
			d = Math.ceil(date.getTime()/1000);
		break;
		case 'month'://当月第一天
			var date = new Date();
			date.setDate(1);
			d = Math.ceil(date.getTime()/1000);
		break;
		case 'today'://今天
		default://如果type为数字可以加或减天数
			if(typeof(type) == "number"){
				if(type > 0){
					d += (type-1)*86400;
				}else{
					d += (type+1)*86400;
				}
			}
		break;
	}
	return get_date(format, d);
}


//给URL增加参数，如:url_add_parm(url, "menu_id="+id)
function url_add_parm(url, parm){
	var arr = url.split("#");
	if(arr[0].indexOf("?")>-1){
		arr[0] += ("&"+parm);
	}else{
		arr[0] += ("?"+parm);
	}
	return arr.join("#");
}

function parse_str(str) {
  //        example 1: varresult = parse_str('first=foo&second=bar');
  //        returns 1: { first: 'foo', second: 'bar' }

	var strArr = String(str)
			.replace(/^\?/, '')
			.replace(/^&/, '')
			.replace(/&$/, '')
			.split('&'),
		sal = strArr.length,
		i, j, ct, p, lastObj, obj, lastIter, undef, chr, tmp, key, value,
		postLeftBracketPos, keys, keysLen,
		fixStr = function(str) {
			return decodeURIComponent(str.replace(/\+/g, '%20'));
		};
	array = {};

	for (i = 0; i < sal; i++) {
		tmp = strArr[i].split('=');
		key = fixStr(tmp[0]);
		value = (tmp.length < 2) ? '' : fixStr(tmp[1]);

		while (key.charAt(0) === ' ') {
			key = key.slice(1);
		}
		if (key.indexOf('\x00') > -1) {
			key = key.slice(0, key.indexOf('\x00'));
		}
		if (key && key.charAt(0) !== '[') {
			keys = [];
			postLeftBracketPos = 0;
			for (j = 0; j < key.length; j++) {
				if (key.charAt(j) === '[' && !postLeftBracketPos) {
					postLeftBracketPos = j + 1;
				} else if (key.charAt(j) === ']') {
					if (postLeftBracketPos) {
						if (!keys.length) {
							keys.push(key.slice(0, postLeftBracketPos - 1));
						}
						keys.push(key.substr(postLeftBracketPos, j - postLeftBracketPos));
						postLeftBracketPos = 0;
						if (key.charAt(j + 1) !== '[') {
							break;
						}
					}
				}
			}
			if (!keys.length) {
				keys = [key];
			}
			for (j = 0; j < keys[0].length; j++) {
				chr = keys[0].charAt(j);
				if (chr === ' ' || chr === '.' || chr === '[') {
					keys[0] = keys[0].substr(0, j) + '_' + keys[0].substr(j + 1);
				}
				if (chr === '[') {
					break;
				}
			}

			obj = array;
			for (j = 0, keysLen = keys.length; j < keysLen; j++) {
				key = keys[j].replace(/^['"]/, '').replace(/['"]$/, '');
				lastIter = j !== keys.length - 1;
				lastObj = obj;
				if ((key !== '' && key !== ' ') || j === 0) {
					if (obj[key] === undef) {
						obj[key] = {};
					}
					obj = obj[key];
				} else { // To insert new dimension
					ct = -1;
					for (p in obj) {
						if (obj.hasOwnProperty(p)) {
							if (+p > ct && p.match(/^\d+$/g)) {
								ct = +p;
							}
						}
					}
					key = ct + 1;
				}
			}
			lastObj[key] = value;
		}
	}
	return array;
}

// 本函数实现了更人性化的时间提示 
// @param date_str 传递过来的时间，时间格式如:2010-12-14 18:36:09
function date_parser_diff_return(date_str){
	var  date=new Date();
	if(typeof(date_str)!='string')return date;
	var date_arr=date_str.split(new RegExp("[:| |-]","ig"));
	var date_obj = new Date(date_arr[0],date_arr[1]-1,date_arr[2],date_arr[3],date_arr[4],date_arr[5]);
	var date_seconddiff=( new Date().getTime()-date_obj.getTime() ) /1000 ;
	date_str_w='';
	if(date_seconddiff <60*30)date_str_w= Math.ceil(date_seconddiff/60)+"分钟前 ";
	if(!date_str_w && date_seconddiff <3600)date_str_w= "1小时前 ";
	if(!date_str_w && date_seconddiff <3600*2)date_str_w= "2小时前 ";
	if(!date_str_w && date_seconddiff <3600*3)date_str_w= "3小时前 ";
	if(!date_str_w && date.getFullYear()==date_arr[0] && date.getMonth()==date_arr[1]-1 && date.getDate()==date_arr[2])
		date_str_w= "今天 "+date_arr[3]+':'+date_arr[4];
	if(!date_str_w && date.getFullYear()==date_arr[0] && date.getMonth()==date_arr[1]-1 && date.getDate()-1==date_arr[2])
		date_str_w= "昨天 "+date_arr[3]+':'+date_arr[4];
	if(!date_str_w && date.getFullYear()==date_arr[0] && date.getMonth()==date_arr[1]-1 && date.getDate()-2==date_arr[2])
		date_str_w= "前天 "+date_arr[3]+':'+date_arr[4];
	if(!date_str_w && date.getFullYear()==date_arr[0] && date.getMonth()==date_arr[1]-1 ) 
		date_str_w= (date.getMonth()+1)+"月"+  date_arr[2]+"号 "+date_arr[3]+':'+date_arr[4];
	if(!date_str_w && date.getFullYear()==date_arr[0]) 
		date_str_w= "今年 " + date_arr[1]+"月"+  date_arr[2]+"号 "+date_arr[3]+':'+date_arr[4];
	if(!date_str_w && date.getFullYear()-1==date_arr[0]) 
		date_str_w= "去年 " + date_arr[1]+"月"+  date_arr[2]+"号 "+date_arr[3]+':'+date_arr[4];
	return date_str_w
};
function log(str){
	window.console && console.log('log: '+str);
}
