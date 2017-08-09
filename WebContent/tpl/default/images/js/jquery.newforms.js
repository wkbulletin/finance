/*
美化表单：Andyfoo
http://www.andyfoo.com
http://www.pslib.com
varsion:1.0
date:2012-11-14
*/

(function($) {
	var browser_ie6 = $.browser.msie && $.browser.version == 6;
	var browser_ie = $.browser.msie;
	var browser_firefox = navigator.userAgent.toLowerCase().match(/firefox\/([\d.]+)/) ? true : false;
	var browser_chrome = $.browser.chrome;
	var NewForms_count = 0;

	var NewForms_id_buffer = new Array();
	var NewForms_id_events = new Array();

	function log(str){
		window.console && console.log('log: '+str);

	}


	$.NewForms = function(elm, options) {
		var base = this;
		this.$elm = $(elm);
		this.form = null;
		this.opts = $.extend({}, $.fn.newforms.defaults, options);
		
		this.mouseTimer = null;

		this.id_buffer_key = this.$elm.attr("newforms_id_buffer");
		if(this.id_buffer_key == null || this.id_buffer_key.length == 0){
			this.id_buffer_key = this.getCount();
			NewForms_id_buffer[this.id_buffer_key] = new Array();
		}else{
			if(!NewForms_id_buffer[this.id_buffer_key]){
				NewForms_id_buffer[this.id_buffer_key] = new Array();
			}
		}
		this.$elm.attr("newforms_id_buffer", this.id_buffer_key);
		
		this.init();
	};
	$.NewForms.prototype = {
		init: function() {
			//if(this.$elm.attr("class") == "newforms")return;

			
			this.$elm.addClass("newforms");
			if(this.$elm.length > 0 && this.$elm[0].tagName.toLowerCase() == 'form'){
				this.form = this.$elm;
			}else if(this.$elm.find("form").length>0){
				this.form = this.$elm.find("form");
			}else{
				return;
			}
			this.form.attr("autocomplete", "off"); 
			this.form.find("input[type=button],input[type=submit],a[class=newforms_link]").attr("hidefocus","true").bind("focus", function (){this.blur();});

			this.replace_input_text();
			this.replace_textarea();

			this.replace_file();
			this.replace_checkbox();
			this.replace_radio();

			this.replace_select();
			this.replace_select_multiple();

			this.replace_button();

			this.replace_button_image();

			this.replace_link();
		},
		replace_input_text: function() {
			//this.$elm.find("input").wrap('<table><tr><td></td><td><div></div></td><td></td></tr></table>');
			var inputs = this.$elm.find("input[type=text],input[type=password]");
			var _this = this;
			inputs.each(function(i){
				var input = $(this);

				var is_init = input.attr("newforms_is_init");
				if(is_init == "1"){
					return;
				}
				input.attr("newforms_is_init", "1");


				if(input.attr("disabled") || input.attr("readonly")){
					input.addClass("newforms_input_text_disabled");
					
				}


				
				if(input.attr("ntype") == "number"){
					var _max = input.attr("max");
					var max = _this.toInt(_max);
					var _min = input.attr("min");
					var min = _this.toInt(_min);
					var val = _this.toInt(input.val());
					if(!_this.isNull(_min) && val < min){
						val = min;
						input.val(val);
					}else if(!_this.isNull(_max) && val > max){
						val = max;
						input.val(val);
					}
					

					input.wrap('<table class="newforms_input_text" border="0" cellpadding="0" cellspacing="0"><tr><td class="newforms_input_text_center"></td></tr></table>')
							.parent("td").before('<td class="newforms_input_text_left"></td>').after('<td class="newforms_input_text_spinner"><a href="#" class="newforms_t"></a><a href="#" class="newforms_b"></a></td>');
					
					input.width(input.width() - _this.opts.input_text_date_space - (input.outerWidth(true)-input.width()));
					input.bind("focus", function (){
						if(input.attr("disabled") || input.attr("readonly"))return;

						this.style.imeMode='disabled';   // 禁用输入法,禁止输入中文字符

						var table = $(this).parents(".newforms_input_text");
						table.find("td.newforms_input_text_left").addClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_center").addClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_spinner").addClass("newforms_input_text_spinner_focus");

						$(this).addClass("newforms_input_text_focus");
						
					});
					
					input.bind("blur", function (){
						var table = $(this).parents(".newforms_input_text");
						table.find("td.newforms_input_text_left").removeClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_center").removeClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_spinner").removeClass("newforms_input_text_spinner_focus");
						
						$(this).removeClass("newforms_input_text_focus");
						

						var _max = input.attr("max");
						var max = _this.toInt(_max);
						var _min = input.attr("min");
						var min = _this.toInt(_min);


						var val = _this.toInt(input.val());
						if(!_this.isNull(_min) && val < min){
							val = min;
							input.val(val);
						}else if(!_this.isNull(_max) && val > max){
							val = max;
							input.val(val);
						}else if(val+"" != input.val()){
							input.val(val);
						}

					});



					input.bind("keypress", function (e){
						var keyCode = e.keyCode ? e.keyCode : e.which;
						if((keyCode > 47 && keyCode < 58) || keyCode == 8 || keyCode == 17 || keyCode == 45){
							return true;
						}
						 return false;
					});
					var table = input.parent("td").parent("tr").parent("tbody").parent("table");

					var add = table.find("a.newforms_t");
					var sub = table.find("a.newforms_b");
					add.focus(function (){this.blur();}).click(function (event){event.preventDefault();}).attr("hidefocus", "true");
					sub.focus(function (){this.blur();}).click(function (event){event.preventDefault();}).attr("hidefocus", "true");



					var add_fun = function (){
							var _max = input.attr("max");
							var max = _this.toInt(_max);
							var _min = input.attr("min");
							var min = _this.toInt(_min);


							var _val = input.val();
							if(_this.isNull(_val))_val = _this.isNull(_min) ? 0 : min ;
							var val = _this.toInt(_val);
							if(!_this.isNull(_min) && val < min){
								val = min;
							}else if(!_this.isNull(_max) && val > max){
								val = max;
							}
							input.val(val);
							val++
									
							if(!_this.isNull(_max) && val <= max){
								input.val(val);
							}else if(_this.isNull(_max)){
								input.val(val);

							}else{
								_this.clearMouseTimer();
							}
							_this.setMouseTimer(add_fun, 100);
						};
					add.mousedown(function (){
						if(input.attr("disabled") || input.attr("readonly"))return;
 
						add_fun();
						_this.setMouseTimer(add_fun, 600);
						
					});
					add.mouseup(function (){
						if(input.attr("disabled") || input.attr("readonly"))return;

						input.focus();
						_this.clearMouseTimer();
					});
					add.mouseleave(function (){
						_this.clearMouseTimer();
					});
					var sub_fun = function (){
							var _max = input.attr("max");
							var max = _this.toInt(_max);
							var _min = input.attr("min");
							var min = _this.toInt(_min);

							var _val = input.val();
							if(_this.isNull(_val))_val=1;
							var val = _this.toInt(_val);
							if(!_this.isNull(_min) && val < min){
								val = min;
							}else if(!_this.isNull(_max) && val > max){
								val = max;
							}
							input.val(val);
							val--;


							if(!_this.isNull(_min) && val >= min){
								input.val(val);
							}else if(_this.isNull(_min)){
								
								input.val(val);

							}else{
								_this.clearMouseTimer();
							}
							_this.setMouseTimer(sub_fun, 100);
						};
					sub.mousedown(function (){
						if(input.attr("disabled") || input.attr("readonly"))return;
						sub_fun();
						_this.setMouseTimer(sub_fun, 600);
					});

					sub.mouseup(function (){
						if(input.attr("disabled") || input.attr("readonly"))return;
						input.focus();
						_this.clearMouseTimer();
					});
					sub.mouseleave(function (){
						_this.clearMouseTimer();
					});

				}else if(input.attr("ntype") == "date"){
					var id = _this.getItemId();

					
					input.attr("readonly", true);

					//日期输入框设置 cale_config属性可以配置日历参数
					var cale_config = input.attr("cale_config");
					if(cale_config){
						cale_config = $.parseJSON(cale_config);
					}else{
						cale_config = {};
					}

					input.wrap('<div></div>').parent("div").wrap('<table class="newforms_input_text" border="0" cellpadding="0" cellspacing="0" id="'+id+'"><tr><td class="newforms_input_text_center"></td></tr></table>')
							.parent("td").before('<td class="newforms_input_text_left"></td>').after('<td class="newforms_input_text_right"></td>');
					
					input.after('<div class="newforms_input_text_datespinner"><a href="#"></a></div>');
					


					input.width(input.width() - _this.opts.input_text_space - (input.outerWidth(true)-input.width()));

					input.bind("focus", function (){
						this.style.imeMode='disabled';   // 禁用输入法,禁止输入中文字符

						var table = $(this).parents(".newforms_input_text");
						table.find("td.newforms_input_text_left").addClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_center").addClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_right").addClass("newforms_input_text_border_focus");

						$(this).addClass("newforms_input_text_focus");

						
					});
					
					input.bind("blur", function (){
						var table = $(this).parents(".newforms_input_text");
						table.find("td.newforms_input_text_left").removeClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_center").removeClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_right").removeClass("newforms_input_text_border_focus");
						
						$(this).removeClass("newforms_input_text_focus");
						
					});

					
					var table = input.parent("div").parent("td").parent("tr").parent("tbody").parent("table");
					var date = table.find("a");
					
					date.focus(function (){this.blur();}).attr("hidefocus", "true");
					
					cale_config.id = input;
					cale_config.offsetX = table.find("td.newforms_input_text_left").width();
					if(!input.attr("disabled")){
						$('#'+id).calendar(cale_config);
					}else{
						date.click(function (event){event.preventDefault();});
					}
					
					date.mouseup(function (){
						if(input.attr("disabled"))return;

						input.focus();
						
						
					});
					
				 
					
				}else if(input.attr("ntype") == "box"){
					var id = _this.getItemId();

					
					input.attr("readonly", true);

					var box_config = input.attr("box_config");
					if(box_config){
						try{
							box_config = $.parseJSON(box_config);
						}catch(e){
							box_config = {};
						}
					}else{
						box_config = {};
					}

					var input_width = input.outerWidth();

					input.wrap('<div></div>').parent("div").wrap('<table class="newforms_input_text" border="0" cellpadding="0" cellspacing="0" id="'+id+'"><tr><td class="newforms_input_text_center"></td></tr></table>')
							.parent("td").before('<td class="newforms_input_text_left"></td>').after('<td class="newforms_input_text_right"></td>');
					
					input.after('<div class="newforms_input_text_boxspinner"><a href="#"></a></div>');
					
					input.attr("box_type", "val").hide();
					input.before('<input box_type="text" type="text" size="'+input.attr("size")+'" style="width:'+input_width+'px">');


					var table = input.parent("div").parent("td").parent("tr").parent("tbody").parent("table");

					var input2 = table.find("input[box_type=text]");
					input2.attr("readonly", true);


					input2.width(input.width() - _this.opts.input_text_space - (input.outerWidth(true)-input.width()));

					input2.bind("focus", function (){
						if(input.attr("disabled")){
							return false;
						}
						
						var table = $(this).parents(".newforms_input_text");
						table.find("td.newforms_input_text_left").addClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_center").addClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_right").addClass("newforms_input_text_border_focus");

						$(this).addClass("newforms_input_text_focus");
						
					});
					
					input2.bind("blur", function (){
						if(input.attr("disabled")){
							return false;
						}

						var table = $(this).parents(".newforms_input_text");
						table.find("td.newforms_input_text_left").removeClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_center").removeClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_right").removeClass("newforms_input_text_border_focus");
						
						$(this).removeClass("newforms_input_text_focus");

						input.blur();
					});

					

					
					
					var a = table.find("a");
					
					a.focus(function (){this.blur();}).attr("hidefocus", "true");
					
					var nf_url = box_config.url;
					var nf_width = box_config.width ? box_config.width : 500;
					var nf_height = box_config.height ? box_config.height : 300;

					if(window.newforms_box_data == null){
						window.newforms_box_data = new Array();
					}
				
					var xopen = new newforms_xopen(id);

					box_config.xopen = xopen;
					box_config.id = id;
					box_config.obj_text = input2;
					box_config.obj_val = input;

					window.newforms_box_data[id] = box_config;
					nf_url += ((nf_url.indexOf('?') >= 0 ? "&" : "?") + "nf_item_id="+id);

					

					xopen.open(nf_url, nf_width, nf_height);

					input2.bind("click", function (event){
						event.preventDefault();
						if(!input.attr("disabled")){
							
							xopen.show(nf_url);
						}
					});
					a.click(function (event){
						event.preventDefault();
						if(!input.attr("disabled")){
							
							xopen.show(nf_url);
						}
					});
					
					
					a.mouseup(function (){
						if(input.attr("disabled"))return;

						input2.focus();
						
						
					});
					
				}else if(input.attr("ntype") == "ztree"){
					var id = _this.getItemId();

					
					input.attr("readonly", true);

					var ztree_config = input.attr("ztree_config");
					if(ztree_config){
						try{
							ztree_config = $.parseJSON(ztree_config);
						}catch(e){
							ztree_config = {};
						}
						
					}else{
						ztree_config = {};
					}
					var ztree_data = input.attr("ztree_data");
					if(ztree_data){
						try{
							ztree_data = $.parseJSON(ztree_data);
						}catch(e){
							ztree_data = [];
						}
					}else{
						ztree_data = [];
					}

					
					var input_width = input.outerWidth();

					input.wrap('<div></div>').parent("div").wrap('<table class="newforms_input_text" border="0" cellpadding="0" cellspacing="0" id="'+id+'"><tr><td class="newforms_input_text_center"></td></tr></table>')
							.parent("td").before('<td class="newforms_input_text_left"></td>').after('<td class="newforms_input_text_right"></td>');
					
					input.after('<div class="newforms_input_text_ztreespinner"><a href="#"></a></div>');
					
					input.attr("box_type", "val").hide();

					var level = 1;

					if(ztree_config.return_type=="array" || ztree_config.return_type=="array_id"){
						level = ztree_config.array_level ? ztree_config.array_level : 3;
						var w = Math.floor(input_width/level);
						for(var i = 0; i < level-1; i++){
							input.before('<input box_type="text" type="text" size="'+input.attr("size")+'" style="width:'+w+'px;margin-right:3px;">');
						}
						input.before('<input box_type="text" type="text" size="'+input.attr("size")+'" style="width:'+w+'px;">');
					}else{
						input.before('<input box_type="text" type="text" size="'+input.attr("size")+'" style="width:'+input_width+'px">');
					}

					var input_txts = input.parent().children("input[box_type=text]");

					

					var table = input.parent("div").parent("td").parent("tr").parent("tbody").parent("table");



					var a = table.find("a");
					
					a.focus(function (){this.blur();}).attr("hidefocus", "true");
					
					

					var input2 = table.find("input[box_type=text]");
					input2.attr("readonly", true);


					//初始化值
					var input_val = null;
					try{
						if(input.val().length > 0 && (input.val().indexOf("[") >= 0 || input.val().indexOf("{") >= 0)){
							input_val = input.val().length > 0 ? $.parseJSON(input.val()) : null;
						}else if(input.val().length > 0){
							input_val = [input.val()];
						}
						
					}catch(e){
						input_val = null;
						
					}
					var input_val_array = new Array();
					if(input_val!=null){
						if(ztree_config.return_type=="array"){
							var val2 = new Array();
							for(var i = 0; i < level; i++){
								var txt = new Array();
								var val = new Array();
								for(var j = 0; input_val[i] && j < input_val[i].length; j++){
									val.push(input_val[i][j][0]);
									txt.push(input_val[i][j][1]);
									input_val_array[input_val[i][j][0]] = true;
								}
								$(input_txts[i]).val(txt.join(","));
								val2.push(val);
								
							}
							input.val($.toJSON(val2));
						}else if(ztree_config.return_type=="array_id"){
							var val2 = new Array();
							for(var i = 0; i < level; i++){
								var txt = new Array();
								for(var j = 0; input_val[i] && j < input_val[i].length; j++){
									val2.push(input_val[i][j][0]);
									txt.push(input_val[i][j][1]);
									input_val_array[input_val[i][j][0]] = true;
								}
								$(input_txts[i]).val(txt.join(","));
								
								
							}
							input.val($.toJSON(val2));
						}else if(ztree_config.return_type=="json"){
							var txt = new Array();
							var val = new Array();
							for(var j = 0; j<input_val.length; j++){
								val.push(input_val[j][0]);
								txt.push(input_val[j][1]);

								input_val_array[input_val[j][0]] = true;
							}
							input_txts.val(txt.join(","));
							input.val($.toJSON(val));
						}else if(ztree_config.return_type=="name"){
							var txt = new Array();
							var val = new Array();
							for(var j = 0; j<input_val.length; j++){
								val.push(input_val[j][0]);
								txt.push(input_val[j][1]);

								input_val_array[input_val[j][0]] = true;
							}
							input_txts.val(txt.join(","));
							input.val(txt.join(","));
						}else{
							var txt = new Array();
							var val = new Array();
							for(var j = 0; j<input_val.length; j++){
								val.push(input_val[j][0]);
								txt.push(input_val[j][1]);

								input_val_array[input_val[j][0]] = true;
							}
							input_txts.val(txt.join(","));
							input.val($.toJSON(val));
						}
					}









					if(ztree_config.return_type=="array" || ztree_config.return_type=="array_id"){
						input_width = input_width - _this.opts.input_text_space;
						var w = Math.floor(input_width/level) - 3;

						input2.width(w);

						if(ztree_config.array_width && ztree_config.array_width.length>0){
							for(var i = 0; i < level && i < ztree_config.array_width.length; i++){
								$(input2[i]).width(ztree_config.array_width[i]);
							}
						}
						

					}else{
						input2.width(input_width - _this.opts.input_text_space);
					}


					$(document.body).append('<div class="newforms_ztree_div" id="'+id+'_box"><div class="newforms_ztree_div_ul"><ul class="ztree"></ul></div><div class="newforms_ztree_div_bar"><a class="newforms_link_button" ztree_type="ok">&nbsp;确&nbsp;定&nbsp;</a>&nbsp;&nbsp;&nbsp;<a class="newforms_link_button" hrer="#" ztree_type="cancel">&nbsp;取&nbsp;消&nbsp;</a></div></div>');
				
					var ul = $("#"+id+"_box");
					ul.width(table.width()-_this.opts.input_text_space);
					



					if(ztree_config.width){
						ul.width(ztree_config.width);
					}
					




					var setting = {
						check: {
							enable: true
						},
						view: {
							dblClickExpand: false,
							showIcon: false,
							showTitle: false
						},
						data: {
							simpleData: {
								enable: true,
								idKey: "id",
								pIdKey: "pId",
								rootPId: 0
							}
						}
					};
					if(ztree_config.radio_type && (ztree_config.radio_type == "level" || ztree_config.radio_type == "all")){
						setting.check.chkStyle = "radio";
						setting.check.radioType = ztree_config.radio_type;
 
					}

					if(typeof(ztree_config.show_line) == "number" && ztree_config.show_line == 0){
						setting.view.showLine = false;
						setting.view.showIcon = false;
					}




					var active_fun = function (obj){
						table.find("td.newforms_input_text_left").addClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_center").addClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_right").addClass("newforms_input_text_border_focus");

						input2.addClass("newforms_input_text_focus");

					}
					
					var deactive_fun = function (obj){
						table.find("td.newforms_input_text_left").removeClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_center").removeClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_right").removeClass("newforms_input_text_border_focus");
						
						input2.removeClass("newforms_input_text_focus");

						input.blur();
					}

					var hide_all_fun = function (event){
						if(!event){
							$(".newforms_ztree_div").hide();
							deactive_fun();
							return;
						}
						if(ul.is(":visible")){
							var evt = $.event.fix(event);
							var target = $(evt.target);
							if(target.attr("class") == "newforms_input_text" || target.parents(".newforms_input_text").length>0 || target.attr("class") == "newforms_ztree_div" || target.parents(".newforms_ztree_div").length>0){
								return;
							}
							ul.hide();
							deactive_fun();

						}

					}

					var div_ul = ul.children("div").children("ul");
					div_ul.attr("id", id+"_ul");
					

					div_ul.html("");
					
					var click_fun = function (event){
						event.preventDefault();
						if(input.attr("disabled"))return;

						if(div_ul.html().length == 0 || div_ul.html() == "请稍候......"){
							div_ul.html("请稍候......");
							if(ztree_config.url){
								$.getJSON(ztree_config.url, function (data){
									if(input_val_array.length > 0){
										for(var i = 0; i < data.length; i++){
											if(input_val_array[data[i].id]){
												data[i]['checked'] = true;
											}
										}
										
									}

									$.fn.zTree.init(div_ul, setting, data);

									
								});
								
							}else{
								if(input_val_array.length > 0){
									for(var i = 0; i < ztree_data.length; i++){
										if(input_val_array[ztree_data[i].id]){
											ztree_data[i]['checked'] = true;
										}
									}
								}
								$.fn.zTree.init(div_ul, setting, ztree_data);
								

							}
							
						}




						if(ul.is(":visible")){
						
							deactive_fun();

							ul.hide();
							return;
						}
						hide_all_fun();
						active_fun();
						

						var wh = $(window).height();
						var dt = $(window).scrollTop();
						var ul_h = ul.height();
						var div_h = table.height();
						var div_offset = table.offset();
					
						
						if(div_offset.top + ul_h + div_h > wh + dt){
							//ul.css("top",(0 - ul_h) + "px");
							ul.css("top", (div_offset.top-ul_h) + "px");
						}else{
							//ul.css("top",div_h + "px");
							ul.css("top", (div_offset.top+div_h) + "px");
						}
						
						ul.css("left", (div_offset.left + _this.opts.input_text_space/2) + "px");



						//ul.scrollTop(0);
						ul.show();
						
						
					};

					input2.click(click_fun);
					a.click(click_fun);

					ul.click(function (event){
						event.preventDefault();
					
					});


					var ztree_cancel = function (event) {
						hide_all_fun();

						event.preventDefault();
					}
					
					//获取选中的ID或name
					var nf_ztree_sub_ids = function (json, type, level){
						var ids = new Array();
						for(var i = 0; i < json.length; i++){
							if(!json[i].checked)continue;
							if(typeof(level) == "number" && json[i].level != level){
								continue;
							}
							ids.push(type == "name" ? json[i].name : json[i].id);
							if(json[i].children && json[i].children.length > 0){
								ids = ids.concat(nf_ztree_sub_ids(json[i].children, type, level));
							}
						}
						return ids;
					}
					//获取父ID
					var nf_ztree_parent_ids = function (ztree,nodes){
						if(nodes.length==0)return nodes;
						var new_arr = new Array();


						new_arr.push({
							id:nodes[0].id,
							pId:nodes[0].pId,
							name:nodes[0].name,
							level:nodes[0].level,
							tId:nodes[0].tId,
							parentTId:nodes[0].parentTId,
							checked:true
						});

						var node = new_arr[0];
						while(node = ztree.getNodeByTId(node.parentTId)){
							new_arr.push({
								id:node.id,
								pId:node.pId,
								name:node.name,
								level:node.level,
								tId:node.tId,
								parentTId:node.parentTId,
								checked:true
							});

						}

						return new_arr;
					}
					//数组去重
					var nf_ztree_array_unique = function (arr){
						var new_arr = new Array();
						var tmp_arr = new Array();
						for(var i = 0; i < arr.length; i++){
							if(!tmp_arr[arr[i]]){
								new_arr.push(arr[i]);
								tmp_arr[arr[i]] = "1";
							}
						}

						return new_arr.sort();
					}
					//删除未选中的json
					var nf_ztree_json_filter = function (json){
						var new_arr = new Array();
						for(var i = 0; i < json.length; i++){
							if(!json[i].checked)continue;
							new_arr.push(json[i]);
							if(json[i].children && json[i].children.length > 0){
								json[i].children = nf_ztree_json_filter(json[i].children);
							}
						}

						return new_arr;
					}

					var ztree_ok = function (event) {
						event.preventDefault();
						var zTree = $.fn.zTree.getZTreeObj(id+"_ul");
						
						var nodes = zTree.getCheckedNodes(true);
						if(!ztree_config.return_type){
							ztree_config.return_type = "id";
						}

						if(ztree_config.return_type == "name"){
							
							input.val(nf_ztree_array_unique(nf_ztree_sub_ids(nodes, "name")));
							input2.val(nf_ztree_array_unique(nf_ztree_sub_ids(nodes, "name"))+"　　　　");
						}else if(ztree_config.return_type == "json"){
							input.val($.toJSON(nf_ztree_json_filter(nodes)));
							input2.val(nf_ztree_array_unique(nf_ztree_sub_ids(nodes, "name"))+"　　　　");
						}else if(ztree_config.return_type == "array"){
							if(setting.check.chkStyle == "radio"){
								nodes = nf_ztree_parent_ids(zTree,nodes);
							}
							
							var arr = new Array();
							for(var i = 0; i < level; i++){
								arr.push(nf_ztree_array_unique(nf_ztree_sub_ids(nodes, "id", i)));
								
								input2[i].value = (nf_ztree_array_unique(nf_ztree_sub_ids(nodes, "name", i))+"　　　　");
							}
							input.val($.toJSON(arr));

						}else if(ztree_config.return_type == "array_id"){
							if(setting.check.chkStyle == "radio"){
								nodes = nf_ztree_parent_ids(zTree,nodes);
							}
							
							var arr = new Array();
							for(var i = 0; i < level; i++){
								arr.push(nf_ztree_array_unique(nf_ztree_sub_ids(nodes, "id", i)));
								
								input2[i].value = (nf_ztree_array_unique(nf_ztree_sub_ids(nodes, "name", i))+"　　　　");
							}
							input.val(nf_ztree_array_unique(nf_ztree_sub_ids(nodes, "id")));

						}else{
							input.val(nf_ztree_array_unique(nf_ztree_sub_ids(nodes, "id")));
							input2.val(nf_ztree_array_unique(nf_ztree_sub_ids(nodes, "name"))+"　　　　");
						}
						input.change();
						
						hide_all_fun();
					}
					var a_ztree_ok = ul.children("div").children("a[ztree_type=ok]");
					var a_ztree_cancel = ul.children("div").children("a[ztree_type=cancel]");
					
					a_ztree_ok.click(ztree_ok);
					a_ztree_cancel.click(ztree_cancel);


					_this.addIdEvent(id, hide_all_fun);
					$(document).bind("click", hide_all_fun);

				}else{

					input.wrap('<table class="newforms_input_text" border="0" cellpadding="0" cellspacing="0"><tr><td class="newforms_input_text_center"></td></tr></table>')
							.parent("td").before('<td class="newforms_input_text_left"></td>').after('<td class="newforms_input_text_right"></td>');
					input.width(input.width() - _this.opts.input_text_space - (input.outerWidth(true)-input.width()));

					input.bind("focus", function (){
						if(input.attr("disabled") || input.attr("readonly"))return;

						var table = $(this).parents(".newforms_input_text");
						table.find("td.newforms_input_text_left").addClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_center").addClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_right").addClass("newforms_input_text_border_focus");

						$(this).addClass("newforms_input_text_focus");
						
					});
					input.bind("blur", function (){
						if(input.attr("disabled") || input.attr("readonly"))return;

						var table = $(this).parents(".newforms_input_text");
						table.find("td.newforms_input_text_left").removeClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_center").removeClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_right").removeClass("newforms_input_text_border_focus");
						
						$(this).removeClass("newforms_input_text_focus");
						
					});

				}

			});


		},
		replace_textarea: function() {
			var textareas = this.$elm.find("textarea");
			var _this = this;
			textareas.each(function(i){
				var textarea = $(this);

				var is_init = textarea.attr("newforms_is_init");
				if(is_init == "1"){
					return;
				}
				textarea.attr("newforms_is_init", "1");


				if(textarea.attr("disabled") || textarea.attr("readonly")){
					textarea.addClass("newforms_textarea_disabled");
					
				}


				textarea.wrap('<table class="newforms_textarea" border="0" cellpadding="0" cellspacing="0"><tr><td class="newforms_textarea_c_m"></td></tr></table>')
						.parent("td").before('<td class="newforms_textarea_l_m"></td>').after('<td class="newforms_textarea_r_m"></td>')
						.parent("tr").before('<tr><td class="newforms_textarea_l_t"></td><td class="newforms_textarea_c_t"></td><td class="newforms_textarea_r_t"></td></tr>')
						.after('<tr><td class="newforms_textarea_l_b"></td><td class="newforms_textarea_c_b"></td><td class="newforms_textarea_r_b"></td></tr>');
				
				textarea.width(textarea.width() - _this.opts.input_text_space - (textarea.outerWidth(true)-textarea.width()));

				var table = textarea.parent("td").parent("tr").parent("tbody").parent("table");
 
				textarea.bind("focus", function (){
					if(textarea.attr("disabled") || textarea.attr("readonly"))return;

					var table = $(this).parents(".newforms_textarea");
					table.find("td.newforms_textarea_l_t").addClass("newforms_textarea_l_t_focus");
					table.find("td.newforms_textarea_c_t").addClass("newforms_textarea_l_t_focus");
					table.find("td.newforms_textarea_r_t").addClass("newforms_textarea_l_t_focus");

					table.find("td.newforms_textarea_l_m").addClass("newforms_textarea_l_m_focus");
					//table.find("td.newforms_textarea_c_m").addClass("newforms_textarea_l_m_focus");
					table.find("td.newforms_textarea_r_m").addClass("newforms_textarea_l_m_focus");



					table.find("td.newforms_textarea_l_b").addClass("newforms_textarea_l_t_focus");
					table.find("td.newforms_textarea_c_b").addClass("newforms_textarea_l_t_focus");
					table.find("td.newforms_textarea_r_b").addClass("newforms_textarea_l_t_focus");


					$(this).addClass("newforms_textarea_focus");
					
				});
				textarea.bind("blur", function (){
					var table = $(this).parents(".newforms_textarea");
					table.find("td.newforms_textarea_l_t").removeClass("newforms_textarea_l_t_focus");
					table.find("td.newforms_textarea_c_t").removeClass("newforms_textarea_l_t_focus");
					table.find("td.newforms_textarea_r_t").removeClass("newforms_textarea_l_t_focus");

					table.find("td.newforms_textarea_l_m").removeClass("newforms_textarea_l_m_focus");
					//table.find("td.newforms_textarea_c_m").removeClass("newforms_textarea_l_m_focus");
					table.find("td.newforms_textarea_r_m").removeClass("newforms_textarea_l_m_focus");



					table.find("td.newforms_textarea_l_b").removeClass("newforms_textarea_l_t_focus");
					table.find("td.newforms_textarea_c_b").removeClass("newforms_textarea_l_t_focus");
					table.find("td.newforms_textarea_r_b").removeClass("newforms_textarea_l_t_focus");

					$(this).removeClass("newforms_textarea_focus");
					
				});

			});

		},
		replace_file: function() {
			var inputs = this.$elm.find("input[type=file]");
			var _this = this;
			inputs.each(function(i){
				//var id = _this.getItemId();
				//input.attr("id", id);
				var input = $(this);

				var is_init = input.attr("newforms_is_init");
				if(is_init == "1"){
					return;
				}
				input.attr("newforms_is_init", "1");

				var input_width = input.width();

				if(browser_ie){
					input.wrap('<table class="newforms_input_text" border="0" cellpadding="0" cellspacing="0"><tr><td></td></tr></table>')
							.parent("td")
							.before('<td class="newforms_input_text_left"></td><td class="newforms_input_text_center"><input type="text" readonly  size="'+input.attr("size")+'" style="width:'+input_width+'px" /></td>')
							.after('<td class="newforms_input_text_right"></td><td class="newforms_input_file_select"><a href="#" class="newforms_link_button">浏览...</a></td>');
					
				}else{
					input.wrap('<table class="newforms_input_text" border="0" cellpadding="0" cellspacing="0"><tr><td class="newforms_file_hidden"></td></tr></table>')
							.parent("td")
							.before('<td class="newforms_input_text_left"></td><td class="newforms_input_text_center"><input type="text" readonly  size="'+input.attr("size")+'" style="width:'+input_width+'px" /></td>')
							.after('<td class="newforms_input_text_right"></td><td class="newforms_input_file_select"><a href="#" class="newforms_link_button">浏览...</a></td>');

				
				}


				var table = input.parent("td").parent("tr").parent("tbody").parent("table");
				
 
				var input2 = table.find(".newforms_input_text_center").find("input[type=text]");
				input2.width(input.width() - _this.opts.input_file_space - (input.outerWidth(true)-input.width()));

				input2.attr("newforms_is_init", "1");


				if(browser_ie){
					table.wrap('<div style="position: relative;display:inline-block;"></div>');
					input.width(input_width-(input.outerWidth(true)-input.width())*2);
					input.css({position:"absolute", left:"0px", top:(table.outerHeight(true)-input.outerHeight(true))/2+"px", filter:"alpha(opacity=0)",opacity:"0"});
				}

				var btn = table.find(".newforms_input_file_select").find("a");
				btn.bind("focus", function (){this.blur();}).attr("hidefocus", "true");

				if(input.attr("disabled") || input.attr("readonly")){
					input2.addClass("newforms_input_text_disabled");
					input2.attr("disabled","true");
					
					btn.addClass("newforms_link_button_disabled");
				}
				

				input.bind("change", function (){
					input2.val($(this).val());
					input2.focus();
				});
				input2.bind("click", function (){
					if(input.attr("disabled") || input.attr("readonly"))return;
					if(!browser_ie)input.click(); 
				});
				btn.bind("click", function (event){
					event.preventDefault();
					if(input.attr("disabled") || input.attr("readonly"))return;

					if(!browser_ie)input.click(); 
				});
				btn.bind("blur", function (){
					input.blur();
				});

				if(browser_ie){
					input.bind("focus", function (){
						var table = $(this).parents(".newforms_input_text");
						table.find("td.newforms_input_text_left").addClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_center").addClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_right").addClass("newforms_input_text_border_focus");

						$(this).addClass("newforms_input_text_focus");

						
					});
					input.bind("blur", function (){
						var table = $(this).parents(".newforms_input_text");
						table.find("td.newforms_input_text_left").removeClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_center").removeClass("newforms_input_text_border_focus");
						table.find("td.newforms_input_text_right").removeClass("newforms_input_text_border_focus");
						
						$(this).removeClass("newforms_input_text_focus");
						
					});

				}

				input2.bind("focus", function (){
					var table = $(this).parents(".newforms_input_text");
					table.find("td.newforms_input_text_left").addClass("newforms_input_text_border_focus");
					table.find("td.newforms_input_text_center").addClass("newforms_input_text_border_focus");
					table.find("td.newforms_input_text_right").addClass("newforms_input_text_border_focus");

					$(this).addClass("newforms_input_text_focus");

					
				});
				input2.bind("blur", function (){
					var table = $(this).parents(".newforms_input_text");
					table.find("td.newforms_input_text_left").removeClass("newforms_input_text_border_focus");
					table.find("td.newforms_input_text_center").removeClass("newforms_input_text_border_focus");
					table.find("td.newforms_input_text_right").removeClass("newforms_input_text_border_focus");
					
					$(this).removeClass("newforms_input_text_focus");

					input.blur();
					
				});

			});


		},

		replace_button: function() {
			var inputs = this.$elm.find("input[type=button],input[type=submit],input[type=reset]");
			inputs.each(function(i){
				var input = $(this);

				var is_init = input.attr("newforms_is_init");
				if(is_init == "1"){
					return;
				}
				input.attr("newforms_is_init", "1");

				input.addClass("newforms_button");

				if(input.attr("disabled")){
					input.addClass("newforms_button_disabled");
				}

				input.bind("hover", function (){
					$(this).addClass("newforms_button_hover");
				});
				input.bind("mouseleave", function (){
					$(this).removeClass("newforms_button_hover");
				});
				input.bind("focus", function (){this.blur();}).attr("hidefocus", "true");
			});

		},

		replace_button_image: function() {
			var inputs = this.$elm.find("input[type=image]");
			inputs.each(function(i){
				var input = $(this);

				var is_init = input.attr("newforms_is_init");
				if(is_init == "1"){
					return;
				}
				input.attr("newforms_is_init", "1");

				input.addClass("newforms_button_image");

				if(input.attr("disabled")){
					input.addClass("newforms_button_image_disabled");
				}

				input.bind("hover", function (){
					$(this).addClass("newforms_button_image_hover");
				});
				input.bind("mouseleave", function (){
					$(this).removeClass("newforms_button_image_hover");
				});
				input.bind("focus", function (){this.blur();}).attr("hidefocus", "true");
			});

		},



		replace_checkbox: function() {
			var inputs = this.$elm.find("input[type=checkbox]");
			var _this = this;
			inputs.each(function(i){
				var checkbox = $(inputs[i]);

				var is_init = checkbox.attr("newforms_is_init");
				if(is_init == "1"){
					return;
				}
				checkbox.attr("newforms_is_init", "1");

				if(browser_ie){
					checkbox.width(0).height(0).css("padding", "0px").css("margin", "0px").css("overflow", "hidden");
				}else{
					checkbox.hide();
				}
				var id = _this.getItemId();
				$(this).before('<a href="#" class="newforms_checkbox" id="'+id+'"></a>');
			
				var btn = $("#"+id);
				if(checkbox.attr("checked")){
					btn.addClass("newforms_checkbox_select");
				}
				btn.attr("checkbox_checked", checkbox.attr("checked"));
				checkbox.attr("_id", id);

				if(checkbox.attr("disabled") && checkbox.attr("checked")){
					btn.addClass("newforms_checkbox_disabled_select");
				}else if(checkbox.attr("disabled")){
					btn.addClass("newforms_checkbox_disabled");
				}
				btn.bind("click", function (event){
					event.preventDefault();
					if(checkbox.attr("disabled"))return ;
					if(checkbox.attr("checked")){
						checkbox.attr("checked", false);
						$(this).removeClass("newforms_checkbox_select");
					}else{
						checkbox.attr("checked", true);
						$(this).addClass("newforms_checkbox_select");
					}
					checkbox.change();
				});
				
				checkbox.bind("change", function (){
					if($(this).attr("checked")){
						btn.addClass("newforms_checkbox_select");
					}else{
						btn.removeClass("newforms_checkbox_select");
					}
				});
				_this.form.bind("reset", function (){
					inputs.each(function(i){
						var checkbox = $(inputs[i]);
						var id = checkbox.attr("_id");
						var btn = $("#"+id);
						
						if(btn.attr("checkbox_checked")){
							btn.addClass("newforms_checkbox_select");
						}else{
							btn.removeClass("newforms_checkbox_select");

						}
					});
				});


				
				btn.bind("focus", function (){this.blur();}).attr("hidefocus", "true");
				
			});


		},

		replace_radio: function() {
			var inputs = this.$elm.find("input[type=radio]");
			var _this = this;
			inputs.each(function(i){
				var radio = $(inputs[i]);


				var is_init = radio.attr("newforms_is_init");
				if(is_init == "1"){
					return;
				}
				radio.attr("newforms_is_init", "1");

				if(browser_ie){
					radio.width(0).height(0).css("padding", "0px").css("margin", "0px").css("overflow", "hidden");
				}else{
					radio.hide();
				}
				
				var id = _this.getItemId();
				$(this).before('<a href="#" class="newforms_radio" id="'+id+'" _name="'+radio.attr("name")+'" _index="'+i+'"></a>');
				radio.attr("_index", i);
				radio.attr("_id", id);
			
				var btn = $("#"+id);
				if(radio.attr("checked")){
					btn.addClass("newforms_radio_select");
				}
				btn.attr("checkbox_checked", radio.attr("checked"));

				if(radio.attr("disabled") && radio.attr("checked")){
					btn.addClass("newforms_radio_disabled_select");
				}else if(radio.attr("disabled")){
					btn.addClass("newforms_radio_disabled");
				}
				btn.bind("click", function (event){
					event.preventDefault();
					if(radio.attr("disabled"))return ;

					$("a[_name="+$(this).attr("_name")+"]").each(function(i){
						$(this).removeClass("newforms_radio_select");
						var radio = $("input[_id="+$(this).attr("id")+"]");
						
						if(radio.attr("disabled")){
							$(this).removeClass("newforms_radio_disabled_select");
							$(this).addClass("newforms_radio_disabled");
						}
						

					});
					
					radio.attr("checked", true);
					$(this).addClass("newforms_radio_select");

					radio.change();
				});
				
				radio.bind("change", function (){
					$("a[_name="+btn.attr("_name")+"]").removeClass("newforms_radio_select");
					if($(this).attr("checked")){
						btn.addClass("newforms_radio_select");
					}else{
						btn.removeClass("newforms_radio_select");
					}
				});
				_this.form.bind("reset", function (){
					inputs.each(function(i){
						var radio = $(inputs[i]);
						var id = radio.attr("_id");
						var btn = $("#"+id);
						
						if(btn.attr("checkbox_checked")){
							btn.addClass("newforms_radio_select");
						}else{
							btn.removeClass("newforms_radio_select");

						}
					});
				});

				btn.bind("focus", function (){this.blur();}).attr("hidefocus", "true");
				
			});


		},
		replace_select: function() {
			//this.$elm.find("input").wrap('<table><tr><td></td><td><div></div></td><td></td></tr></table>');
			var selects = this.$elm.find("select").not("select[multiple=multiple]");
			_this = this;
			selects.each(function(i){
				var select = $(this);

				var is_init = select.attr("newforms_is_init");
				if(is_init == "1"){
					return;
				}
				select.attr("newforms_is_init", "1");

				var select_width = select.outerWidth();
				var id = _this.getItemId();
				select.wrap('<div class="newforms_select" obj_id="'+id+'"></div>');


				select.before('<table border="0" cellpadding="0" cellspacing="0" width="100%"><tr><td class="newforms_select_left"></td><td class="newforms_select_center"><div></div></td><td class="newforms_select_trigger"></td></tr></table>');
				
				$(document.body).append('<ul class="newforms_select_ul" id="'+id+'"></ul>');


				var div = select.parent("div");

				div.width(select_width);

				select.hide();

				var table = div.children("table");
				table.width(div.width());
				var selval = table.find("div");

				selval.width(selval.width());
				selval.css("overflow", "hidden");

				if(select.attr("disabled")){
					selval.addClass("newforms_select_disabled");
				}

				selval.attr("default_index", select[0].selectedIndex);
				if(select[0].options.length>0 && select[0].options[select[0].selectedIndex] && typeof(select[0].options[select[0].selectedIndex].text) == 'string'){
					selval.html(select[0].options[select[0].selectedIndex].text);
				}

			
				var ul = $("#"+id);
				ul.width(table.width()-_this.opts.select_space);
				var add_option = function (){
					ul.empty();
					for(var i = 0; i < select[0].options.length; i++){
						ul.append('<li '+(i == select[0].selectedIndex ? 'class="select"' : '')+'><a href="#" hidefocus="true" onfocus="this.blur();" _index='+i+'>'+select[0].options[i].text+'</a></li>');
					}
					ul.find("a").bind("click", function (event){
						event.preventDefault();
						ul.children("li").removeClass("select");
						$(this).parent("li").addClass("select");

						//var select = $(this).parents(".newforms_select").children("select");
						select[0].selectedIndex = $(this).attr("_index");

						selval.html(select[0].options[select[0].selectedIndex].text);

						ul.hide();

						$(select[0]).change();
						
					});

				}
				add_option();



				select.bind("change", function (){
					var lis = ul.children("li");
					if(select[0].options.length != lis.length){
						add_option();
					}
					lis.removeClass("select");
					ul.find("a[_index="+select[0].selectedIndex+"]").parent("li").addClass("select");
					if(select[0].selectedIndex>=0)selval.html(select[0].options[select[0].selectedIndex].text);
					
				});
				_this.form.bind("reset", function (){
					var lis = ul.children("li");
					lis.removeClass("select");
					var default_index = _this.toInt(selval.attr("default_index"));
					if(default_index == -1 && select[0].options.length > 0){
						default_index = 0;
					}
					if(default_index >= 0 && default_index < select[0].options.length){
						ul.find("a[_index="+default_index+"]").parent("li").addClass("select");
						selval.html(select[0].options[default_index].text);
					}

				});


				
				var active_fun = function (obj){
					$(obj).find("td.newforms_select_left").addClass("newforms_select_border_focus");
					$(obj).find("td.newforms_select_center").addClass("newforms_select_border_focus");
					$(obj).find("td.newforms_select_trigger").addClass("newforms_select_trigger_focus");
					$(obj).find("td.newforms_select_center").children("div").addClass("newforms_select_focus");

				}
				
				var deactive_fun = function (obj){
					$(obj).find("td.newforms_select_left").removeClass("newforms_select_border_focus");
					$(obj).find("td.newforms_select_center").removeClass("newforms_select_border_focus");
					$(obj).find("td.newforms_select_trigger").removeClass("newforms_select_trigger_focus");
					$(obj).find("td.newforms_select_center").children("div").removeClass("newforms_select_focus");

				}

				div.bind("hover", function (){
					active_fun(this);
					
				});
				div.bind("mouseleave", function (){
					deactive_fun(this);
					
				});
				ul.hover(function (){
					active_fun(div);
					
				});

				var hide_all_fun = function (event){
					if(!event){
						$(".newforms_select_ul").hide();
						deactive_fun($(".newforms_select"));
						return;
					}
					if(ul.is(":visible")){
						var evt = $.event.fix(event);
						var target = $(evt.target);
						if(target.attr("class") == "newforms_select" || target.parents(".newforms_select").length>0 || target.attr("class") == "newforms_select_ul" || target.parents(".newforms_select_ul").length>0){
							return;
						}
						
						ul.hide();
						deactive_fun(div);

					}

				}
				div.bind("click", function (event){
					event.preventDefault();
					if(select.attr("disabled"))return ;
					var obj = $(event.target);
					if(obj[0].tagName == "A"){
						return ;
					}
					if(ul.is(":visible")){
					
						deactive_fun(this);

						ul.hide();
						return ;
					}
					hide_all_fun();
					active_fun(this);
					

					var wh = $(window).height();
					var dt = $(window).scrollTop();
					var ul_h = ul.height();
					var div_h = $(this).height();
					var div_offset = $(this).offset();
				
			 
					if(div_offset.top + ul_h + div_h > wh + dt){
						ul.css("top", (div_offset.top-ul_h) + "px");
					}else{
						ul.css("top", (div_offset.top+div_h) + "px");
					}

					
					ul.css("left", (div_offset.left + _this.opts.input_text_space/2) + "px");



					ul.scrollTop(0);
					ul.show();
					var li_offset = (select[0].selectedIndex-1) * ul.children("li").height();

					ul.scrollTop(li_offset);

					if(_this.opts.select_menu_type == 2){
						var li_offset2 = ul.children("li[class=select]").position();
						if(li_offset2)ul.css("top", (div_offset.top-li_offset2.top) + "px");
					}


					
					
				});
				//div.bind("mouseleave", function (){

				_this.addIdEvent(id, hide_all_fun);
				$(document).bind("click", hide_all_fun);
				

			});

		},
		replace_select_multiple: function() {
			var selects = this.$elm.find("select[multiple=multiple]");
			var _this = this;
			selects.each(function(i){
				var select = $(this);

				var is_init = select.attr("newforms_is_init");
				if(is_init == "1"){
					return;
				}
				select.attr("newforms_is_init", "1");


				var select_width = select.outerWidth(true);

				if(select.attr("disabled")){
					select.addClass("newforms_textarea_disabled");
				}

				select.wrap('<table class="newforms_textarea" border="0" cellpadding="0" cellspacing="0"><tr><td class="newforms_textarea_c_m"></td></tr></table>')
						.parent("td").before('<td class="newforms_textarea_l_m"></td>').after('<td class="newforms_textarea_r_m"></td>')
						.parent("tr").before('<tr><td class="newforms_textarea_l_t"></td><td class="newforms_textarea_c_t"></td><td class="newforms_textarea_r_t"></td></tr>')
						.after('<tr><td class="newforms_textarea_l_b"></td><td class="newforms_textarea_c_b"></td><td class="newforms_textarea_r_b"></td></tr>');
				select.width(select.width() - _this.opts.input_text_space);

				var table = select.parent("td").parent("tr").parent("tbody").parent("table");
			 
				select.bind("focus", function (){
					var table = $(this).parents(".newforms_textarea");
					table.find("td.newforms_textarea_l_t").addClass("newforms_textarea_l_t_focus");
					table.find("td.newforms_textarea_c_t").addClass("newforms_textarea_l_t_focus");
					table.find("td.newforms_textarea_r_t").addClass("newforms_textarea_l_t_focus");

					table.find("td.newforms_textarea_l_m").addClass("newforms_textarea_l_m_focus");
					//table.find("td.newforms_textarea_c_m").addClass("newforms_textarea_l_m_focus");
					table.find("td.newforms_textarea_r_m").addClass("newforms_textarea_l_m_focus");



					table.find("td.newforms_textarea_l_b").addClass("newforms_textarea_l_t_focus");
					table.find("td.newforms_textarea_c_b").addClass("newforms_textarea_l_t_focus");
					table.find("td.newforms_textarea_r_b").addClass("newforms_textarea_l_t_focus");


					$(this).addClass("newforms_textarea_focus");
					
				});
				select.bind("blur", function (){
					var table = $(this).parents(".newforms_textarea");
					table.find("td.newforms_textarea_l_t").removeClass("newforms_textarea_l_t_focus");
					table.find("td.newforms_textarea_c_t").removeClass("newforms_textarea_l_t_focus");
					table.find("td.newforms_textarea_r_t").removeClass("newforms_textarea_l_t_focus");

					table.find("td.newforms_textarea_l_m").removeClass("newforms_textarea_l_m_focus");
					//table.find("td.newforms_textarea_c_m").removeClass("newforms_textarea_l_m_focus");
					table.find("td.newforms_textarea_r_m").removeClass("newforms_textarea_l_m_focus");



					table.find("td.newforms_textarea_l_b").removeClass("newforms_textarea_l_t_focus");
					table.find("td.newforms_textarea_c_b").removeClass("newforms_textarea_l_t_focus");
					table.find("td.newforms_textarea_r_b").removeClass("newforms_textarea_l_t_focus");

					$(this).removeClass("newforms_textarea_focus");
					
				});
			});

		},


		replace_link: function() {
			var alinks = this.$elm.find("a[ntype=box]");
			var _this = this;
			alinks.each(function(i){
				var alink = $(this);

				var is_init = alink.attr("newforms_is_init");
				if(is_init == "1"){
					return;
				}
				alink.attr("newforms_is_init", "1");

				if(alink.attr("ntype") == "box"){
					var id = _this.getItemId();

					alink.wrap('<div id="'+id+'"></div>');

					var div = alink.parent();
					div.addClass("newforms_link_boxspinner");

					var box_config = alink.attr("box_config");
					if(box_config){
						box_config = $.parseJSON(box_config);
					}

					
					alink.focus(function (){this.blur();}).attr("hidefocus", "true");
					
					var nf_url = box_config.url;
					var nf_width = box_config.width ? box_config.width : 500;
					var nf_height = box_config.height ? box_config.height : 300;

					if(window.newforms_box_data == null){
						window.newforms_box_data = new Array();
					}
				
					var xopen = new newforms_xopen(id);

					box_config.xopen = xopen;
					box_config.id = id;

					window.newforms_box_data[id] = box_config;
					nf_url += ((nf_url.indexOf('?') >= 0 ? "&" : "?") + "nf_item_id="+id);

					

					xopen.open(nf_url, nf_width, nf_height);

					 
					alink.click(function (event){
						event.preventDefault();
						if(!alink.attr("disabled")){
							xopen.show(nf_url);
						}
					});
					
					
				} 

			});


		},
		getItemId: function() {
			var n = "newforms_frm_item_"+this.getCount();
			
			NewForms_id_buffer[this.id_buffer_key].push(n);
			return n;

		},
		getCount: function() {
			return NewForms_count++;

		},
		addIdEvent: function(id, evt) {
			NewForms_id_events[id] = evt;
		},
		toInt: function(str){
			var result = parseInt(str, 10);
			return isNaN(result) ? 0 : result;
		},
		isNull: function(str){
			return str == null || str == "" || typeof(str) == "undefined";
		},
		setMouseTimer: function(fn, t){
			if(this.mouseTimer != null){
				window.clearTimeout(this.mouseTimer);
			}
			this.mouseTimer = window.setTimeout(fn, t);
		},
		clearMouseTimer: function(fn){
			if(this.mouseTimer != null){
				window.clearTimeout(this.mouseTimer);
			}
			this.mouseTimer = null;
		}
	};

	$.fn.newforms = function(options) {
		var opts = $.extend({}, $.fn.newforms.defaults, options);

		return this.each(function() {
			new $.NewForms(this, opts);
		});
	};

	$.fn.newforms_clear = function() {
		var k = $(this).attr("newforms_id_buffer");
		var arr = NewForms_id_buffer[k];
		if(!arr){
			return;
		}
		for(var i in arr){
			$("#"+arr[i]).remove();
			if(NewForms_id_events[arr[i]]){
				$(document).unbind("click", NewForms_id_events[arr[i]]);
				NewForms_id_events[arr[i]] = null;
			}
		}
		NewForms_id_buffer[k] = null;
		arr = new Array();
		for(var i in NewForms_id_buffer){
			if(NewForms_id_buffer[i] == null)continue;
			arr[i]= NewForms_id_buffer[i];
		}
		NewForms_id_buffer = arr;
		
		arr = new Array();
		for(var i in NewForms_id_events){
			if(NewForms_id_events[i] == null)continue;
			arr[i]= NewForms_id_events[i];
		}
		NewForms_id_events = arr;

	};


	// default settings
	$.fn.newforms.defaults = {
		input_text_space:8,
		input_text_date_space:28,
		input_file_space:68,
		select_space:10,
		select_menu_type:1
	};


})(jQuery);


//设置input值
//["val","text"]
function set_newforms_item_value(item_id, data){
	/*
	var table = $("#"+item_id);
	var input = table.find("input[box_type=val]");
	var input2 = table.find("input[box_type=text]");

	input.val(data[0]);
	input2.val(data[1]);
	*/

	

	if(window.newforms_box_data[item_id]){
		if(data != null){
			if(window.newforms_box_data[item_id].set_fn){
				eval(window.newforms_box_data[item_id].set_fn)(item_id, data);
			}else{
				
				window.newforms_box_data[item_id].obj_val.val(data[0]);
				window.newforms_box_data[item_id].obj_text.val(data[1]+"        ");

				window.newforms_box_data[item_id].obj_val.change();
			}
		}


		window.newforms_box_data[item_id].xopen.close();
	}
}
function get_newforms_item_value(item_id, data){
	if(window.newforms_box_data[item_id]){
		if(window.newforms_box_data[item_id].get_fn){
			return eval(window.newforms_box_data[item_id].get_fn)(item_id, data);
		}else{
			return [window.newforms_box_data[item_id].obj_val.val(),window.newforms_box_data[item_id].obj_text.val() ];
			
		}
	}
}


//弹出层
//窗口管理
function newforms_xopen(_prefix){
	this.prefix = _prefix ? _prefix + "_nf_xopen_" : "nf_xopen_";
	this.full_screen_id = this.prefix+'full_screen';
	this.win_id = this.prefix+'win';
	this.win_name = this.prefix+'win';
	this.closer_id = this.prefix+'closer';
	
	
	this.full_screen = null;
	this.win = null;
	this.closer = null;

	this.init();
}
//初始化 
newforms_xopen.prototype.init = function(){ 
	var str = '<div id="'+this.full_screen_id+'" class="nf_xopen_full_screen"></div>';
	str += '<div id="'+this.win_id+'" class="nf_xopen_win"></div>';
	str += '<div id="'+this.closer_id+'" class="nf_xopen_closer"><a href="#" hidefocus="true" onfocus="this.blur()"></a></div>';
	
	$(document.body).append(str);
	
	this.full_screen = $('#'+this.full_screen_id);
	this.win = $('#'+this.win_id);
	this.closer = $('#'+this.closer_id);
		
		
	this.resize();
	
	var _this = this;
	$(window).bind('resize', function (){_this.resize();})
		.bind('scroll', function (){_this.repos();});
	this.closer.click(function (event){event.preventDefault();_this.close();});
	this.full_screen.click(function (event){event.preventDefault();_this.close();});

}
newforms_xopen.prototype.resize = function(){
	if(this.full_screen.is(":visible")){
		this.full_screen.hide();
		this.full_screen.width($(document).width())
				.height($(document).height());
		this.full_screen.show();
		this.repos();

	}


}
newforms_xopen.prototype.repos = function(){ 
	var window_w = $(window).width();
	var window_h = $(window).height();
	this.win.css("left", ((window_w - this.win.width())/2 + $(document).scrollLeft()) + "px")
		.css("top", ((window_h - this.win.height())/2 + $(document).scrollTop()) + "px");
	var pos = this.win.offset();
	this.closer.css("left", (pos.left +  this.win.width()) + "px")
		.css("top", pos.top + "px");
	
}
//打开窗口 
newforms_xopen.prototype.open = function(url, w, h){
	var _this = this;
	this.win.width(w).height(h);
}

//打开窗口 
newforms_xopen.prototype.show = function(url){
	var _this = this;
	
	this.repos();
	this.full_screen.show();
	this.resize();

	this.win.show();
	this.closer.show();
	this.repos();
	
	if(this.win.children("iframe").length == 0){
		this.win.html('<iframe src="'+url+'" name="'+this.win_name+'" width="100%" height="100%" frameborder="0" scrolling="no" allowtransparency="true"></iframe>');
	}
		

}
newforms_xopen.prototype.close = function(){ 
	this.closer.hide();
	this.win.hide();
	this.full_screen.hide();


}
