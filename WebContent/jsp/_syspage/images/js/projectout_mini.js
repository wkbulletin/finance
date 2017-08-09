var Browser = {
	IE:     !!(window.attachEvent && !window.opera),
	Opera:  !!window.opera,
	WebKit: navigator.userAgent.indexOf('AppleWebKit/') > -1,
	iPanel: navigator.userAgent.indexOf('iPanel') > -1,
	Gecko:  navigator.userAgent.indexOf('Gecko') > -1 && navigator.userAgent.indexOf('KHTML') == -1
}

var go_page_url="/uniptv_index.do";
function go_page(){
	if(go_page_url.length<1){
		window.history.back();
	}else{
		window.location=go_page_url;
	}
	return false;
}
function set_go_page(url){
	go_page_url = url;
}
function set_active_link(id){
	if(getID2(id))getID2(id).focus();
}

function go_login(url){
	if(url.indexOf("backurl=http")<=0){
		url += get_top_location();
	}
	window.top.location=url;
}
function go_register(url){
	if(url.indexOf("backurl=http")<=0){
		url += get_top_location();
	}
	window.top.location=url;
}
function get_top_location(){
	var url = window.top.location + "";
	url = url.replace(/forwardUrl=.*/,"");
	url = url.replace(/mobileno=[^&]*&/,"");
	url = url.replace(/realmobile=[^&]*&/,"");
	url = url.replace(/mobileno=.*$/,"");
	url = url.replace(/realmobile=.*$/,"");
	url = url.replace(/\?$/,"");
	url = url.replace(/#$/,"");

	return encodeURIComponent(url);
}

//页面总高
function get_body_height(t){
	var h = 0;
	if (!document.all) {
		h = document.documentElement.offsetHeight > 0 ? document.documentElement.offsetHeight : document.body.clientHeight;
		h = h > 0 ? h : document.documentElement.clientHeight;
	}
	else{
		h = document.body.clientHeight == 0 ? document.documentElement.scrollHeight : document.body.clientHeight;
	}
	var h2 = document.all ? document.documentElement.offsetHeight : window.innerHeight;
	return t && h2 && h2 > h ? h2 : h;
}


//提示信息
var my_msg_call_fun = null;
function my_msg(title, msg, call){
	my_msg_call_fun = call;
	getID("msg_box").style.display="block";
	getID("msg_box_title").innerHTML=title;
	getID("msg_box_msg").innerHTML=msg;
	
	getID("msg_box_btn_ok").blur();
	getID("msg_box_btn_ok").focus();

	key_event_vars.IsDefKeyDown=true;
	key_event_vars.OnDefKeyDown=my_msg_document_onkeypress;
}
function my_msg_close(){
	getID('msg_box').style.display='none';
	if(typeof(my_msg_call_fun) == "function"){
		my_msg_call_fun();
	}
	key_event_vars.IsDefKeyDown=false;
	key_event_vars.OnDefKeyDown=new Function();
}
function my_msg_document_onkeypress(){
	getID("msg_box_btn_ok").focus();
}
function my_alert(msg, call){
	my_msg("提示信息",msg, call);
}

function print_r_obj(obj){
	for(var i in obj){
		alert(i + "=" + obj[i]);
	}
}

//解决IE6下默认不缓存背景图片
try {
if(Browser.IE)
          document.execCommand('BackgroundImageCache', false, true);
} catch(e) {}

function preload_images() {
	var d=document; 
	if(d.images){ 
		if(!d.im_MM_p) d.im_MM_p=new Array();
		var i,j=d.im_MM_p.length,a=preload_images.arguments; 
		for(i=0; i<a.length; i++)
		if (a[i].indexOf("#")!=0){ 
			d.im_MM_p[j]=new Image; 
			d.im_MM_p[j++].src=a[i];
		}
	}
}
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
function toInt(str){
	var result = parseInt(str, 10);
	return isNaN(result) ? 0 : result;
}
function get_timestamp(){
	var d = new Date();
	return Math.round(d.getTime()/1000);
}
function get_date(type, t){
	if(t){ 
		var o = new Date(parseFloat(t, 10) * 1000); 
	}else{
		var o = new Date();
	}
	var dd = new Array();
	dd['y'] = o.getYear();
	dd['Y'] = o.getFullYear();
	dd['n'] = (parseInt(o.getMonth(), 10)+1);
	dd['m'] = strPad(dd['n'], 2);
	dd['j'] = o.getDate();
	dd['d'] = strPad(dd['j'], 2);
	
	dd['g'] = o.getHours();//小时，12 小时格式，没有前导零 1 到 12 
	if(dd['g']>12)dd['g'] = dd['g'] - 12;
	dd['G'] = o.getHours() ;//小时，24 小时格式，没有前导零 0 到 23 
	dd['h'] = strPad(dd['g'], 2);//小时，12 小时格式，有前导零 01 到 12 
	dd['H'] = strPad(dd['G'], 2);//小时，24 小时格式，有前导零 00 到 23 

	dd['i'] = strPad(o.getMinutes(), 2);
	dd['s'] = strPad(o.getSeconds(), 2);
	
	
	var date = dd['Y'] + '-' + dd['m'] + '-' + dd['d'];
	var time = dd['H'] + ':' + dd['i'] + ':' + dd['s'];
	switch(type){
		case 'date':
			return date;
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
			for(var i in dd){
				str = str_replace (str, i, dd[i]);
			}
			return str;
		break;
	}
}
function str_replace (str,replace_what,replace_with)   
{   
	var   ndx=str.indexOf(replace_what);   
	var   delta=replace_with.length - replace_what.length;   
	while(ndx >= 0)   
	{   
		str=str.substring(0,ndx)+replace_with+str.substring(ndx+replace_what.length);   
		ndx=str.indexOf(replace_what,ndx+delta+1);   
	}   
	return str;   
}
function strPad(v, l){
	if(typeof(l) == 'undefined')var l = 2;
	var s = v + '';
	if(s.length < l){
		for(var i = s.length; i < l;i++){
			s = '0' + s;
		}
	}
	return s;
}
//排序：t=asc/desc
function sortArray(arr, t){
	if(!arr && arr.length == 0 )return new Array();
	return t == 'asc' ? arr.sort(function(a, b){return toInt(a)-toInt(b)}) : arr.sort(function(a, b){return toInt(b)-toInt(a)});
}
function inArray(v, arr){
	try{
		for(var i = 0; i < arr.length;i++){
			if(v == arr[i])return true;
		}
	}catch(e){}
	return false;
}
function searchArray(v, arr){
	try{
		for(var i = 0; i < arr.length;i++){
			var str = arr[i] + "";
			if(str.indexOf(v) != -1)return new Array(i, arr[i]);
		}
	}catch(e){}
	return false;
}
function arrayDeleteItem(v, arr){
	var new_arr = new Array();
	try{
		for(var i = 0; i < arr.length;i++){
			if(v != arr[i]){new_arr[new_arr.length] = arr[i]}
		}
	}catch(e){}
	return new_arr;
}
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
function isNum(num){
	var rule = /^\d+$/; 
	if(rule.test(num))return true;
	return false;
}
//是否为数字
function isNumber(str){
	var rule = /^\d+$/;
	return rule.test(str+"");
}
function trim(value) {
   var temp = value;
   var obj = /^(\s*)([\W\w]*)(\b\s*$)/;
   if (obj.test(temp)) { 
     temp = temp.replace(obj, '$2'); 
   }
   return temp;
}
function load_script(url) {
	try{
		var script = document.createElement("script");
		script.type = "text/javascript";
		script.src = url;
		document.getElementsByTagName("head")[0].appendChild(script);
	}catch(e){
		//alert(e);
	}

}
function load_script2(url, callback) {
	var f = arguments.callee;
	if (!("queue" in f))
		f.queue = {};
	var queue =  f.queue;
	if (url in queue) { // script is already in the document
		if (callback) {
			if (queue[url]) // still loading
				queue[url].push(callback);
			else // loaded
				callback();
		}
		return;
	}
	queue[url] = callback ? [callback] : [];
	var script = document.createElement("script");
	script.type = "text/javascript";
	script.onload = script.onreadystatechange = function() {
		if (script.readyState && script.readyState != "loaded" && script.readyState != "complete")
			return;
		script.onreadystatechange = script.onload = null;
		while (queue[url].length)
			queue[url].shift()();
		queue[url] = null;
	};
	
	script.src = url;
	document.getElementsByTagName("head")[0].appendChild(script);
}
var _ELEMENT_ID_CACHES = new Array();
function getID2_clearAll(){ 
	_ELEMENT_ID_CACHES = new Array();
}
function getID2(obj){ 
	return _ELEMENT_ID_CACHES[obj] ? _ELEMENT_ID_CACHES[obj] : _ELEMENT_ID_CACHES[obj] = getID(obj);
}
function getID(obj){
	var element = null;
	if(document.getElementById){
		element = document.getElementById(obj);
	}
	else if(document.all){
		element = document.all[obj];
	}
	else if(document.layers){
		element = document.layers[obj];
	} 
	return element;

} 
if(typeof($) == 'undefined')$=getID;
function fix_event(e){
	if (typeof e == 'undefined') e = window.event;
	if (typeof e.layerX == 'undefined') e.layerX = e.offsetX;
	if (typeof e.layerY == 'undefined') e.layerY = e.offsetY;
	if (typeof e.srcElement == 'undefined') e.srcElement = e.target;
	return e;
}


function format_number(num, dot, dec_point){
	try{
		num = parseFloat(num);
		if(typeof(dot) == 'undefined')var dot=2;
		if(typeof(dec_point) == 'undefined')var dec_point='';
		num = num.toFixed(dot);
		if(dec_point.length>0){
			var   re=/(\d+)(\d{3})/,s=num.toString();  
			while(re.test(s))s=s.replace(re,"$1" + dec_point + "$2");  
			return s;  
		}
	}catch(e){
		
	}
	if(isNaN(num))num = "";
	return num;  
}


function removeEvent(obj, name, func) {
	name = name.toLowerCase();
	// Add the hookup for the event.
	if(typeof(obj.addEventListener) != "undefined") {
		if(name.length > 2 && name.indexOf("on") == 0) name = name.substring(2, name.length);
			obj.removeEventListener(name, func, true);
	} else if(typeof(obj.attachEvent) != "undefined"){
		obj.detachEvent(name, func);
	} else {
		eval("obj." + name) = null;
	}
}

function addEvent(obj, name, func) {
	name = name.toLowerCase();
	// Add the hookup for the event.
	if(typeof(obj.addEventListener) != "undefined") {
		if(name.length > 2 && name.indexOf("on") == 0) name = name.substring(2, name.length);
			obj.addEventListener(name, func, false);
	} else if(typeof(obj.attachEvent) != "undefined"){
		obj.attachEvent(name, func);
	} else {
		if(eval("obj." + name) != null){
			// Save whatever defined in the event
			var oldOnEvents = eval("obj." + name);
			eval("obj." + name) = function(e) {
				try{
					func(e);
					eval(oldOnEvents);
				} catch(e){}
			};
		} else {
			eval("obj." + name) = func;
		}
	}
}


function print_r_obj(obj){
	var popup = window.open("/js/x_open/loading.htm", "_print_r_obj_", 'width=700,height=400,scrollbars=yes,resizable=yes,status=yes');
	
	popup.document.open();
	popup.document.write("<pre>");
	var k = 0;
	for(var i in obj){
		k++;
		if(k > 1000)break;
		if(typeof(obj[i]) == "function")continue;//typeof(obj[i]) == "object" || 
		if(typeof(obj[i]) == "string"){
			if(obj[i].indexOf("src=") >= 0)continue;
		}
		popup.document.write(k + ">>>>" + i + "="  + obj[i] + "\r\n");
	}
	popup.document.write("</pre>");
	popup.document.close();
}

function copyText(obj) {
	ie = (document.all)? true:false;
	try{
		if(typeof(obj) == "string")var obj = getID(obj);
	}catch(e){}
	if (ie){
		var rng = document.body.createTextRange();
		rng.moveToElementText(obj);
		rng.scrollIntoView();
		rng.select();
		rng.execCommand("Copy");
		rng.execCommand("Unselect");
		rng.collapse(false);
		return true;
	}else{
		try{prompt("请复制以下内容:",obj.innerHTML); }catch(e){}
	}
	return false;
}