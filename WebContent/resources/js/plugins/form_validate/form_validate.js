/*
自动检测表单js

*/
function FormValidate(_id_pfx){
	this.id_pfx = _id_pfx ? _id_pfx + "_form_v_" : "form_v_";
	this.count = 0;
	this.check_rule = new Array();
	this.check_rule['email']		=	/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;    
	this.check_rule['number']  		=	/^\d+$/;
	this.check_rule['num']  		=	this.check_rule['number'];
	this.check_rule['url']			=	/^(http:|https:)\/\/[\w]+\.[\w]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/;
	this.check_rule['tel']			=	 /^((\(\d{2,3}\))|(\d{3}[\-]{0,1}))?(\(0\d{2,3}\)|0\d{2,3}[\-]{0,1})?[1-9]\d{6,7}([\-]{0,1}\d{1,4})?$/;
	this.check_rule['mobile']		=	/^((\(\d{3}\))|(\d{3}\-))?(13|15|18)\d{9}$/;
	this.check_rule['domain']		=	/^[\w]+\.[\w]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/;
	
	this.check_rule['money']		=	/^\d+(\.\d+)?$/;
	this.check_rule['zip']			=	/^[0-9]{5,6}$/;
	this.check_rule['oicq']			=	/^[0-9]{4,12}$/;
	this.check_rule['int']			=	/^[-\+]?\d+$/;
	this.check_rule['double']		=	/^[-\+]?\d+(\.\d+)?$/;
	this.check_rule['english']		=	/^[\w\. ]+$/;
	this.check_rule['letter']		=	/^[A-Za-z]+$/;
	this.check_rule['upper_letter']		=	/^[A-Z]+$/;
	this.check_rule['lower_letter']		=	/^[a-z]+$/;

	this.check_rule['chinese']		=	/^[\u4E00-\u9FA5\uF900-\uFA2D]+$/;
	this.check_rule['username']		=	/^[a-z]\w{2,32}$/i;
	this.check_rule['username2']		=	/^[\w]{2,32}$/i;
	this.check_rule['nickname']		=	/^[\u4E00-\u9FA5\uF900-\uFA2D\w]{2,40}$/i;    
	this.check_rule['password']		=	/^[\w]+$/;
	this.check_rule['password2']		=	/^[\w\.;,'~!#@$%^&*()<>\-=:\?+|]+$/;
	this.check_rule['char']			=	/^[\w\-]+$/;
	this.check_rule['text_cn_en']		=	/^[\u4E00-\u9FA5\uF900-\uFA2D\w]*$/i;


	this.check_rule['idcard']		=	/^\d{15}(\d{2}[\w])?$/;
	this.check_rule['bankcard']  		=	/^\d+$/;
	
	this.check_rule['ip']  		=	/^((?:(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d))))$/;



	this.form = null;
	this.isOverflow = false;
	this.overflowDiv = null;

	//保存表单默认值 
	this.form_item_vals = new Array();

	this.msg_type = "div";

	this.submitFun = null;
}
FormValidate.prototype.init = function(fm, submitFun, overflowDiv, msgType){
	var _this = this;
	
	var fm_obj = $(fm);
	this.form = fm_obj;
	fm = fm_obj[0];
	
	this.submitFun = submitFun;
	fm_obj.submit(function () {
		if(!_this.check(this)){
			return false;
		}
		if(typeof(_this.submitFun) == "function"){
			var r = _this.submitFun.call(this);
			if(typeof(r) == "undefined"){
				return false;
			}
			return r;
		}
		return true;
	});

	
	if(msgType){
		this.msg_type = msgType;
	}
	if(!this.isOverflow && overflowDiv && $(overflowDiv).length > 0){
		this.overflowDiv = $(overflowDiv);
		this.isOverflow = true;
	}	
	if(!this.isOverflow){
		this.form.parents().each(function (){
			if(!_this.isOverflow && parseInt($(this).css("height"), 10) > 0 && $(this).css("overflow") == "hidden"){
				_this.overflowDiv = $(this);
				_this.isOverflow = true;
			}
		
		});
	}

	if(this.isOverflow){
		this.overflowDiv.css("position","relative");
	}
	this.form.bind('reset', function (){
		$(".form_v_msg").hide();	
		
	});
	 
	fm_obj.find("input[type=checkbox],input[type=radio]").bind("change", function (){
		var check_label = document.getElementsByName(this.name);
		for(var i = 0; i < check_label.length; i++){
			var v_rule = _this.getString(check_label[i].getAttribute('v_rule'));
			try{
				v_rule = $.parseJSON(v_rule);
			}catch(e){
				continue;
			}
			if(v_rule == null){
				continue;
			}
			var msg_fail = _this.getString(v_rule.msg_fail);
			if(msg_fail){
				//_this.hideError(check_label[i]);


				_this.check_item(_this.form, check_label[i]);
			}
		}

	});
	this.form_item_vals = new Array();
	//if(this.msg_type == "div")
	for(var i = 0; i < fm.elements.length; i++){
		var item = fm.elements[i];
		var item_obj = $(item);

		var v_rule = this.getString(item.getAttribute('v_rule'));
		try{
			v_rule = $.parseJSON(v_rule);
		}catch(e){
			continue;
		}
		if(v_rule == null){
			continue;
		}


		var msg = this.getString(v_rule.msg);
		item_obj.bind("keypress", function (){
			_this.hideError(this);
		});
		if(item.type == 'checkbox' || item.type == 'radio'){
				
		}else if(item.tagName == 'SELECT'){
			
			item_obj.bind("change", function (){
				if(_this.msg_type == "div"){
					_this.check_item(this.form, this);
				}
				
			});
		}else{

			this.form_item_vals[item_obj.attr("name")] = item_obj.val();

			if(this.msg_type == "div"){
				item_obj.bind("change", function (){
					_this.hideError(this);

					
				});
			}


			

			item_obj.bind("focus", function (){
				var v_rule = _this.getString(this.getAttribute('v_rule'));
				try{
					v_rule = $.parseJSON(v_rule);
				}catch(e){
					v_rule = null;
				}
				if(v_rule != null){
					var msg = _this.getString(v_rule.msg);
					if(msg.length>0){
						var msg_offset = _this.getString(v_rule.msg_offset).split(",");
						msg_offset[0] = msg_offset[0] ? _this.toInt(msg_offset[0]) : 0;
						msg_offset[1] = msg_offset[1] ? _this.toInt(msg_offset[1]) : 0;

						_this.showTip(this, msg, msg_offset);
					}
				}
			});
			item_obj.bind("blur", function (){
				_this.hideTip(this);
				if(_this.msg_type == "div"){
					_this.check_item(this.form, this);
				}
			});
		}

		
			
	}

	//删除无用msg信息

	this.getParentID().find(".form_v_msg").each(function (){
		var id = $(this).attr("id");
		var msg = $(this);

		var inputs = _this.getParentID().find("input[v_msg_fail_id="+id+"],textarea[v_msg_fail_id="+id+"],select[v_msg_fail_id="+id+"]");
		if(inputs.length == 0){
			$(this).remove();
		}
		inputs.each(function (){
			if($(this).parents("*[form_v_disabled=1]").length > 0){
				msg.hide();
			}		
			
		});


	});
}
//清除所有创建的信息
FormValidate.prototype.destroy = function(){
	this.getParentID().find(".form_v_msg").empty();
}
//隐藏错误信息
FormValidate.prototype.hideAll = function(fm){
	for(var i = 0; i < fm.elements.length; i++){
		var item = fm.elements[i];
		this.hideError(item);
	}
}
FormValidate.prototype.check = function(fm){
	var firstItem = null;
	var is_valid = true;
	for(var i = 0; i < fm.elements.length; i++){
		var item = fm.elements[i];
		if(!this.check_item(fm, item, true)){
			if(firstItem==null)firstItem=item;
			is_valid= false;
			if(this.msg_type == "dialog"){
				return is_valid;
			}
		}
		//this.showError(item, " asdfds");
		//return false;
	}
	if(firstItem!=null){
		firstItem.focus();
	}
	return is_valid;
}
FormValidate.prototype.check_item = function(fm, item, is_submit){
	var _this = this;
	this.hideError(item);

	if($(item).attr("form_v_disabled") == "1" || $(item).parents("*[form_v_disabled=1]").length > 0){
		return true;
	}
	var tagName = item.tagName.toLowerCase();
	var type = $(item).attr("type");
	if(type=="file" && !$(item).parent().parent().is(":visible")){
		return true;
	}else if(type!="file" && !$(item).parent().is(":visible")){
		return true;
	}


	if(item.disabled || item.readonly){
		return true;
	}

	var v_rule = this.getString(item.getAttribute('v_rule'));
	if(v_rule.length == 0)return true;
	try{
		v_rule = $.parseJSON(v_rule);
	}catch(e){
		this.showError(item, '验证规则错误:'+e);
		return false;
	}
	if(v_rule == null){
		return true;
	}

	var model = this.getString(v_rule.model);
	var msg_fail = this.getString(v_rule.msg_fail);
	var msg_ok = this.getString(v_rule.msg_ok);
	var msg = this.getString(v_rule.msg);
	var msg_offset = this.getString(v_rule.msg_offset).split(",");
	msg_offset[0] = msg_offset[0] ? this.toInt(msg_offset[0]) : 0;
	msg_offset[1] = msg_offset[1] ? this.toInt(msg_offset[1]) : 0;

	var len = this.getString(v_rule.len);
	var len_max = 0;
	var len_min = 0;
	//判断长度（按字数）
	if(len.length > 0){
		var tmp = len.split(",");
		len_min = this.toInt(tmp[0]);
		len_max = this.toInt(tmp[1]);
		var val = this.trim(this.getString(item.value));
		if(type!="file"){
		item.value = val;
		}

		var val_len = val.length;
		//var val_len = this.strlen(val);

		if(len_min == 0 && val == ''){
			return true;
		}
		if(tmp.length == 1 && len_min > val_len){
			this.showError(item, msg_fail, msg_offset);
			return false;
		}
		if(tmp.length == 2 && len_max < val_len || len_min > val_len){
			this.showError(item, msg_fail, msg_offset);
			return false;
		}
	}
	var blen = this.getString(v_rule.blen);
	var blen_max = 0;
	var blen_min = 0;
	//判断长度(按字节数)
	if(blen.length > 0){
		var tmp = blen.split(",");
		blen_min = this.toInt(tmp[0]);
		blen_max = this.toInt(tmp[1]);
		var val = this.trim(this.getString(item.value));
		item.value = val;
		var val_len = this.strlen(val);

		if(blen_min == 0 && val == ''){
			return true;
		}
		if(tmp.length == 1 && blen_min > val_len){
			this.showError(item, msg_fail, msg_offset);
			return false;
		}
		if(tmp.length == 2 && blen_max < val_len || blen_min > val_len){
			this.showError(item, msg_fail, msg_offset);
			return false;
		}
	}

	var area = this.getString(v_rule.area);
	var area_max = 0;
	var area_min = 0;
	//判断范围
	if(area.length > 0){
		var tmp = area.split(",");
		area_min = this.toFloat(tmp[0]);
		area_max = this.toFloat(tmp[1]);
		var val = this.toFloat(item.value);
		if(tmp.length == 1 && (item.type == 'text' || item.type == 'password' || item.type == 'textarea') && (area_min > val)){
			this.showError(item, msg_fail, msg_offset);
			return false;
		}
		if(tmp.length == 2 && (item.type == 'text' || item.type == 'password' || item.type == 'textarea') && (area_max < val || area_min > val)) {
			this.showError(item, msg_fail, msg_offset);
			return false;
		}
	}



	var to = this.getString(v_rule.to);
	//if(model.length == 0)return true;
	
	switch(model){
		case 'repeat':
			if(to != null){
				if(item.value.length == 0 && fm.elements[to].value.length == 0){
					
					return false;
				}
				if(fm.elements[to].value != item.value){
					this.showError(item, msg_fail, msg_offset);
					return false;
				}
			}

					
			break;

		case 'compare':
			if(to != null){
				var to_obj = (""+to).substr(0,1) == "#" ? $(to)[0] : fm.elements[to];
				if(item.value.length == 0 && to_obj.value.length == 0){
					
					break;
				}
				var op = this.getString(v_rule.op);
				
				var v1 = item.value;
				var v2 = to_obj.value;

				if(v2.length == 0){
					this.showError(item, msg_fail, msg_offset);
					return false;
					break;
				}
				if(/^[-\+]?\d+(\.\d+)?$/.test(v2)){
					v1 = parseFloat(v1, 10);
					v2 = parseFloat(v2, 10);

				
					//验证前一项目规则
					var v_rule2 = this.getString(to_obj.getAttribute('v_rule'));
					try{
						v_rule2 = $.parseJSON(v_rule2);
					}catch(e){
						v_rule2 = null;
					}
					if(v_rule2 != null){
						var model2 = this.getString(v_rule2.model);
						var msg_fail2 = this.getString(v_rule2.msg_fail);
						if(model2.length > 0 && this.check_rule[model2] && !this.check_rule[model2].test(item.value)){
							this.showError(item, msg_fail2, msg_offset);
							return false;
						}
					}
					if(isNaN(v1)){
						this.showError(item, msg_fail, msg_offset);
						return false;
						break;
					}
				}


				if(item.value.length == 0 && to_obj.value.length > 0 || item.value.length > 0 && to_obj.value.length == 0){
					this.showError(item, msg_fail, msg_offset);
					return false;
					break;
				}
				switch(op){
					case '>=':
						if(v1 < v2){
							this.showError(item, msg_fail, msg_offset);
							return false;
						}
						break;
					case '>':
						if(v1 <= v2){
							this.showError(item, msg_fail, msg_offset);
							return false;
						}
						break;
					case '<':
						if(v1 >= v2){
							this.showError(item, msg_fail, msg_offset);
							return false;
						}
						break;
					case '<=':
						if(v1 > v2){
							this.showError(item, msg_fail, msg_offset);
							return false;
						}
						break;
					case '!=':
						if(v1 == v2){
							this.showError(item, msg_fail, msg_offset);
							return false;
						}
						break;
					default:
						if(v1 != v2){
							this.showError(item, msg_fail, msg_offset);
							return false;
						}

						break;
				}
				

			}

					
			break;

		case 'define':
			var rule = this.getString(v_rule.rule);
			if(!new RegExp(rule,"g").test(item.value)){
				this.showError(item, msg_fail, msg_offset);
				return false;
			}
			break;
		case 'select': 
			var check_label = document.getElementsByName(item.name);
			var checked_count = 0;
			for(var j = 0; j < check_label.length; j++){
				if(check_label[j].selected || check_label[j].value != '') checked_count = 1;
			}
			if(checked_count == 0){
				this.showError(item, msg_fail, msg_offset);
				return false;
			}
			break;
		case 'multiple': 
			var checked_count = 0;
			for(var j = 0; j < item.options.length; j++){
				if(item.options[j].selected) checked_count++;
			}
			if(checked_count > area_max && area_max > 0 || checked_count < area_min) {
				this.showError(item, msg_fail, msg_offset);
				return false;
			}
			break;
		case 'radio': 
			var check_label = document.getElementsByName(item.name);
			var checked_count = 0;
			for(var j = 0; j < check_label.length; j++){
				if(check_label[j].checked) checked_count = 1;
			}
			if(checked_count == 0) {
				this.showError(item, msg_fail, msg_offset);
				return false;
			}
			break;
		case 'checkbox': 
			var check_label = document.getElementsByName(item.name);
			var checked_count = 0;
			//var last_label = check_label[check_label.length - 1];
			for(var j = 0; j < check_label.length; j++){
				if(check_label[j].checked) checked_count++;
			}
			
			if(checked_count > area_max && area_max > 0 || checked_count < area_min){
				this.showError(item, msg_fail, msg_offset);
				return false;
			}
			break;
		case 'file': 
			if(area.length > 0){
				var checked_count = 0;
				var tmp = area.split(",");
				for(var j = 0; j < tmp.length; j++){
					if(item.value.match(new RegExp("\."+tmp[j]+"$", "ig"))){
						checked_count++;
					}

				}
				if(checked_count == 0) {
					this.showError(item, msg_fail, msg_offset);
					return false;
				}

			}
			break;
		case 'Y-m-d H:i:s':
		case 'yyyy-MM-dd HH:mm:ss':
		case 'datetime':
			var tmp = item.value.match(new RegExp("^([0-9]{4})[-./]{1}([0-9]{1,2})[-./]{1}([0-9]{1,2})[ ]{1}([0-9]{2})[:]{1}([0-9]{1,2})[:]{1}([0-9]{1,2})$"));
			if(tmp != null ) {//year-month-day tmp[1]-tmp[2]-tmp[3]
				if(!this.check_date(tmp[1], tmp[2], tmp[3]) || !this.check_time(tmp[4], tmp[5], tmp[6])) {
					this.showError(item, msg_fail, msg_offset);
					return false;
				}
			}else {
				this.showError(item, msg_fail, msg_offset);
				return false;
			}
			break;
		case 'H:i:s':
		case 'HH:mm:ss':
		case 'time':
			var tmp = item.value.match(new RegExp("^([0-9]{2})[:]{1}([0-9]{1,2})[:]{1}([0-9]{1,2})$"));
			if(tmp != null ) {//year-month-day tmp[1]-tmp[2]-tmp[3]
				if(!this.check_time(tmp[1], tmp[2], tmp[3])) {
					this.showError(item, msg_fail, msg_offset);
					return false;
				}
			}else {
				this.showError(item, msg_fail, msg_offset);
				return false;
			}
			break;
		case 'Y-m-d':
		case 'yyyy-MM-dd':
		case 'date':
			var tmp = item.value.match(new RegExp("^([0-9]{4})[-./]{1}([0-9]{1,2})[-./]{1}([0-9]{1,2})$"));
			if(tmp != null ) {//year-month-day tmp[1]-tmp[2]-tmp[3]
				if(!this.check_date(tmp[1], tmp[2], tmp[3])) {
					this.showError(item, msg_fail, msg_offset);
					return false;
				}
			}else {
				this.showError(item, msg_fail, msg_offset);
				return false;
			}
			break;

		case 'ip_more':
			var tmp = item.value.split(/[,\r\n]+/);
			if(tmp != null && tmp.length > 0) {
				for(var j in tmp){
					if(!this.check_rule['ip'].test(tmp[j])) {
						this.showError(item, msg_fail, msg_offset);
						return false;
					}
				}

			}else {
				this.showError(item, msg_fail, msg_offset);
				return false;
			}
			break;
		default:

			if(model.length > 0){
				if(!this.check_rule[model] || this.check_rule[model] == 'undefined') {
					return false;
				}else{
					if(!this.check_rule[model].test(item.value)){
						this.showError(item, msg_fail, msg_offset);
						return false;
					}else{

						try{
							if(model == 'idcard' && !validId(item.value)){
								this.showError(item, msg_fail, msg_offset);
								return false;
							}else if(model == 'bankcard' && !luhmCheck(item.value)){
								this.showError(item, msg_fail, msg_offset);
								return false;
							}


						}catch(e){}
					}


				}



			}
			break;
	}
	var def_check_fun = this.getString(v_rule.def_check_fun);
	var def_check_fun2 = this.getString(v_rule.def_check_fun2);
	if(def_check_fun.length > 0 && this.form_item_vals[$(item).attr("name")] != $(item).val()){
		var def_check = this.getString(item.getAttribute('v_def_check'));
		if(is_submit && def_check == "1"){
			if(msg_ok.length>0)this.showOk(item, msg_ok, msg_offset);
			return true;
		}
		
		item.setAttribute('v_def_check', "0");
		try{
			def_check_fun = eval(def_check_fun);
		}catch(e){
			this.showError(item, def_check_fun+"错误:"+e, msg_offset);
			return false;
		}
		if(def_check.length == 0 || def_check == "0")this.showError(item, "检测中......", msg_offset);

		def_check_fun(item, function(t, msg){
			if(t){
				item.setAttribute('v_def_check', "1");
				_this.showOk(item, msg, msg_offset);

			}else{
				item.setAttribute('v_def_check', "-1");
				_this.showError(item, msg, msg_offset);
			}
			
		});
		
		return false;
	}else if(def_check_fun.length == 0 && def_check_fun2.length > 0){
		
		try{
			def_check_fun2 = eval(def_check_fun2);
		}catch(e){
			this.showError(item, def_check_fun2+"错误:"+e, msg_offset);
			return false;
		}
		if(def_check_fun2 (item, item)){
			if(msg_ok.length>0)this.showOk(item, msg_ok, msg_offset);
		}else{
			this.showError(item, msg_fail, msg_offset);
			return false;
		}
		
		
	}else{
		if(msg_ok.length>0)this.showOk(item, msg_ok, msg_offset);
	}
	
	return true;

}
//改变规则
FormValidate.prototype.set_rule = function(obj, rule){
	if(typeof(obj) == 'string'){
		obj = $('#'+obj);
	}else{
		obj = $(obj);
	}
	
	if(obj.length>0){
		obj.attr('v_rule', rule);
		if(obj[0].form)this.init(obj[0].form);
	}


}
FormValidate.prototype.check_date = function(y, m, d){ 
	if((y <= 0) || (m > 12 || m < 1) || (d > 31 || d < 1)) return false;
	return true;
}

FormValidate.prototype.check_time = function(h, i, s){ 
	if((h > 23 || h < 1) || (i > 59 || i < 1) || (s > 59 || s < 1)) return false;
	return true;
}


FormValidate.prototype.getString = function(str){ 
	if(typeof(str) == "string"){
		return str;
	}
	return "";
}


FormValidate.prototype.trim = function(value){ 
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

FormValidate.prototype.toFloat = function(str){ 
	var result = parseFloat(str, 10);
	return isNaN(result) ? 0 : result;
}
FormValidate.prototype.toInt = function(str){ 
	var result = parseInt(str, 10);
	return isNaN(result) ? 0 : result;
}
FormValidate.prototype.strlen = function(s){ 
	return (s+"").replace(/[^\x00-\xff]/g, "**").length;
}

FormValidate.prototype.showError = function(obj, str, msg_offset){
	if(this.msg_type == "dialog"){

		my_alert(str, function (){
			obj.focus();
		});
		return;
	}
	

	var msg_obj = this.getMsgId(obj);
	msg_obj.find(".form_v_msg_c_m").children("div").removeClass().addClass("form_v_msg_alert").html(str);
	msg_obj.show();
	this.setMsgPos(obj, msg_obj, msg_offset);
	var _this = this;
	$(window).bind('resize', function (){
		_this.setMsgPos(obj, msg_obj, msg_offset);	
		
	});

}  
FormValidate.prototype.showOk = function(obj, str, msg_offset){
	if(this.msg_type == "dialog"){
		
		return;
	}
	var msg_obj = this.getMsgId(obj);
	msg_obj.find(".form_v_msg_c_m").children("div").removeClass().addClass("form_v_msg_ok").html(str);
	msg_obj.show();
	this.setMsgPos(obj, msg_obj, msg_offset);
	var _this = this;
	$(window).bind('resize', function (){
		_this.setMsgPos(obj, msg_obj, msg_offset);	
		
	});

} 
FormValidate.prototype.hideError = function(obj){
	this.getMsgId(obj).hide();
} 

FormValidate.prototype.showTip = function(obj, str, msg_offset){
	var msg_obj = this.getMsgId(obj);
	msg_obj.find(".form_v_msg_c_m").children("div").removeClass().addClass("form_v_msg_tip").html(str);
	msg_obj.show();
	this.setMsgPos(obj, msg_obj, msg_offset);
	var _this = this;
	$(window).bind('resize', function (){
		_this.setMsgPos(obj, msg_obj, msg_offset);	
		
	});

}      
FormValidate.prototype.hideTip = function(obj){
	this.getMsgId(obj).hide();
}   

FormValidate.prototype.getMsgId = function(obj){
	var msg_id = this.getString(obj.getAttribute('v_msg_fail_id'));
	if(msg_id == ""){
		msg_id = this.getCountID("");
		obj.setAttribute('v_msg_fail_id', msg_id);

		var html = '';
		html += '<div id="'+msg_id+'" class="form_v_msg">';
		
		html += '<div class="form_v_msg_left"></div>';
		html += '<table height="0" border="0" cellpadding="0" cellspacing="0">';
		html += '<tr><td class="form_v_msg_l_t"></td><td class="form_v_msg_c_t"></td><td class="form_v_msg_r_t"></td></tr>';
		html += '<tr><td class="form_v_msg_l_m"><div class="spacer"></div></td><td class="form_v_msg_c_m"><div></div></td><td class="form_v_msg_r_m"><div class="spacer"></div></td></tr>';
		html += '<tr><td class="form_v_msg_l_b"></td><td class="form_v_msg_c_b"></td><td class="form_v_msg_r_b"></td></tr>';
		html += '</table>';

		html += '</div>';
//		$(document.body).append(html);
		this.appendHtml(html);
	}
	return $("#"+msg_id);
}
FormValidate.prototype.setMsgPos = function(obj, msg_obj, msg_offset){
	
	msg_obj.width(500);

	obj = $(obj);
	var is_show = obj.is(":visible");
	obj.show()
	var offset = this.getOffset(obj);
	if(!is_show)obj.hide();
	
	
	if(!is_show){
		var obj2 = obj.parents(".newforms_select");
		if(obj2.length == 1){
			obj = obj2;
			offset = this.getOffset(obj);
		}
	}
	is_show = obj.parent().is(":visible");

	if(offset.left < 0){
		var obj2 = obj.parents(".newforms_input_text");
		if(obj2.length == 1){
			obj = obj2;
			offset = this.getOffset(obj);
		}else{
			obj = obj.parent();
			offset = this.getOffset(obj);
		}
	}
	if(obj.attr("ntype") == "number"){
		var obj2 = obj.parents(".newforms_input_text");
		if(obj2.length == 1){
			obj = obj2;
			offset = this.getOffset(obj);
		}
	}

	if(obj.attr("type") == "file" && obj.parents(".newforms_file_hidden")){
		var obj2 = obj.parent().parent();
		if(obj2.length == 1){
			obj = obj2;
			offset = this.getOffset(obj);
		}
	}
	var msg_obj_tb = msg_obj.children("table");
	
	msg_obj.css({
		left:(offset.left + obj.outerWidth(true) + 10 + (msg_offset ? msg_offset[0] : 0))+"px", 
		top:(offset.top-(msg_obj_tb.outerHeight(true)-obj.outerHeight(true))/2 + (msg_offset ? msg_offset[1] : 0))+"px"
		}).width(msg_obj_tb.outerWidth(true)+2);
}
FormValidate.prototype.getOffset = function(obj){
	if(this.isOverflow){
		var offset = obj.offset();
		var poffset = this.overflowDiv.offset();
		var pscrolltop = this.overflowDiv.scrollTop();
		var pscrollleft = this.overflowDiv.scrollLeft();

		offset.top += (pscrolltop-poffset.top);
		offset.left += (pscrollleft-poffset.left);

		return offset;
	}else{
		return obj.offset();
	}
}
FormValidate.prototype.appendHtml = function(html){
	if(this.isOverflow){
		this.overflowDiv.append(html);
	}else{
		this.form.append(html);
	}
}
FormValidate.prototype.getParentID = function(){
	if(this.isOverflow){
		return this.overflowDiv;
	}else{
		return this.form;
	}
}


FormValidate.prototype.getCountID = function(str){
	return this.id_pfx + str + (this.count++);
}

 


var form_v = new FormValidate();
