$(document).ready(function(){

	$('.i-checks').iCheck({
		checkboxClass: 'icheckbox_square-green',
		radioClass: 'iradio_square-green',
	});

	//init_left_menu();
	init_list_tag();
});


//初始化菜单
function init_left_menu(){
	//菜单生成
	create_left_menu();



}

var menu_a = new Array();
var menu_child = new Array();
var menu_all = new Array();
var menu_id_all = new Array();
var menu_curr_info = null;
var menu_all2 = new Array();
function create_left_menu(){
	function init(){
		for(var i = 0; i < left_menu_data.length; i++){
			var arr = left_menu_data[i];
			menu_all2[arr.id] = arr;
			if(arr.type != 1){
				continue;
			}
			if(arr.pid == 0){
				menu_a.push(arr);
			}
			menu_all[arr.id] = arr;
			menu_id_all.push(arr.id);
			if(typeof(menu_child[arr.pid]) == 'undefined'){
				menu_child[arr.pid] = new Array();
			}
			menu_child[arr.pid].push(arr);
		}
	}
	init();

	function get_url_info(curr_url, search){
		for(var i = 0; i < left_menu_data.length; i++){
			var arr = left_menu_data[i];
			var url = (arr.url+"").split("?");
			if(url.length > 0 && url[0].length > 0 && url[0] != '#'){
				if(url.length == 1 && curr_url.toLowerCase() == url[0].toLowerCase()){
					return arr;
				}else if(url.length > 1 && curr_url.toLowerCase() == url[0].toLowerCase()){
					var uArr = parse_str(url[1]);
					var qArr = parse_str(search);
					if(!uArr || uArr.length == 0){
						return arr;
					}
					var count = 0;
					var r_count = 0;
					for(var j in uArr){
						count++;
						if(uArr[j] == qArr[j]){
							r_count++;
						}
					}
					if(count == r_count){
						return arr;
					}
				}
			}
		}
		return 0;
	}
	var curr_info = get_url_info(window.location.pathname, window.location.search+"");
	//curr_info = get_url_info("/privilege/privilege_list.do");
	var curr_all_pid = [];
	if(curr_info){
		curr_all_pid = get_menu_parent(curr_info.id);
		set_nav(curr_all_pid, curr_info.id);
	}else{
		set_nav(0);
	}
	menu_curr_info = curr_info;
	

	var menu_str = new Array();
	var dis_style = "";
	for(var i = 0; i < menu_a.length; i++){
		var arr = menu_a[i];
		var arr2 = menu_child[menu_a[i].id];

		menu_str.push('<li');
		dis_style = '';
		if(arr && inArray(arr.id, curr_all_pid)){
			menu_str.push(' class="active"');
			//dis_style = ' style="display:block;"';
		}
		menu_str.push('>');
		menu_str.push('<a href="javascript:;"><i class="iconfont icon-'+arr.k_id+'"></i> <span class="nav-label">'+arr.title+'</span><span class="fa arrow"></span></a>');
		
		if(arr2 && arr2.length > 0){
			menu_str.push('<ul'+dis_style+'  class="nav nav-second-level">');
			

			for(var j = 0; j < arr2.length; j++){
				var arr3 = menu_child[arr2[j].id];
				dis_style = '';
				if(arr3 && arr3.length > 0){
					menu_str.push('<li sub="1"');
					if(arr2 && inArray(arr2[j].id, curr_all_pid)){
						menu_str.push(' class="active"');
						//dis_style = ' style="display:block;"';
					}
					menu_str.push('>');
					menu_str.push('<a href="javascript:;"');
				}else{
					menu_str.push('<li sub="0"');
					if(arr2 && inArray(arr2[j].id, curr_all_pid)){
						menu_str.push(' class="active"');
						//dis_style = ' style="display:block;"';
					}
					menu_str.push('>');
					menu_str.push('<a href="'+arr2[j].url+'"');
				}

				
				var title_sub = arr2[j].title.cutStr(16);
				if(title_sub != arr2[j].title){
					menu_str.push(' title="'+arr2[j].title+'"');
				}
				menu_str.push('>'+title_sub);
				if(arr3 && arr3.length > 0){
					menu_str.push('<span class="fa arrow"></span>');
				}


				menu_str.push('</a>');

				if(arr3 && arr3.length > 0){
					menu_str.push('<ul'+dis_style+' class="nav nav-third-level" aria-expanded="true">');
					for(var k = 0; k < arr3.length; k++){
						menu_str.push('<li');
						if(arr3 && inArray(arr3[k].id, curr_all_pid)){
							menu_str.push(' class="active"');
						}
						menu_str.push('>');

						menu_str.push('<a href="'+arr3[k].url+'"');
				
						var title_sub = arr3[k].title.cutStr(16);
						if(title_sub != arr3[k].title){
							menu_str.push(' title="'+arr3[k].title+'"');
						}
						menu_str.push('>'+title_sub+'</a>');
						
						
						menu_str.push('</li>');
					}
					menu_str.push('</ul>');
				}
				menu_str.push('</li>');
			}
			menu_str.push('</ul>');
		}

		menu_str.push('</li>');
	}
	$("#side-menu").append(menu_str.join(""));
	$("#side-menu a").attr("hidefocus", "true").focus(function (){this.blur();});
	$('#side-menu').metisMenu();
}

//获取菜单父菜单
function get_menu_parent(id){
	var pid_arr = new Array();
	do{
		var arr = menu_all2[id];
		pid_arr.push(arr.id);

		id = arr.pid;
	}while(arr&&id>0);
	return pid_arr;
}

//设置导航字符串
function set_nav(all_pid, curr_id){

	if(curr_id>0){
		if(all_pid==null){
			all_pid = get_menu_parent(curr_id);
		}
		var navStr=new Array();
		var navStr2=new Array();
		var all_pid2 = all_pid.reverse();
		for(var i in all_pid2){
			var row = menu_all[all_pid2[i]];
			if(!row)continue;
			if(row['id'] == curr_id){
				$("#nav_title").html(row['title']);
				navStr.push("<li><strong>");
			}else{
				navStr.push("<li><span>");
			}
			navStr.push(row['title']);
			if(row['id'] == curr_id){
				navStr.push("</strong></li>");
			}else{
				navStr.push("</span></li>");
			}
			navStr2.push(row['title']);
		}
		$("#nav_link").html('<li><a href="/">首页</a></li>'+navStr.join('  '));
		document.title=navStr2.reverse().join(' - ') + " - " + document.title;
	}else{
	}
}

//初始化列表中的相关功能
function init_list_tag(){
	//有确认功能的链接
	$("a[_confirm_url]").click(function (){
		var _confirm_url = $(this).attr("_confirm_url");
		my_confirm($(this).attr("_confirm_msg"), function (){
			window.location = _confirm_url;
		});
		return false;	
	});

	//没有确认功能的链接
	$("a[_url]").click(function (){
		var _url = $(this).attr("_url");
		window.location = _url;
		return false;	
	});
	//弹出小窗口
	$("a[_open_url]").click(function (event){
		event.preventDefault();
			 
		var url = $(this).attr("_open_url");
		var title = $(this).attr("_open_msg")
		

		$('#my_modal_win').off('show.bs.modal').on('show.bs.modal', function (event) {
			var button = $(event.relatedTarget);	
			var modal = $(this);
			modal.find("h5").html(title);
			modal.find('iframe').remove();
			modal.find('.modal-body').append('<iframe allowtransparency="true" src="'+url+'" scrolling="no"  frameborder="0" width="100%" height="500"></iframe>');
			modal.find('iframe').load(function(){
				modal.find('.modal-body').show();	
			});
			$(".modal-content").addClass("animated bounceInRight");
		});
		$('#my_modal_win').off('hidden.bs.modal').on('hidden.bs.modal', function (e) {
			$(".modal-content").removeClass("animated bounceInRight");
			$(this).removeData("bs.modal");
			
			var modal = $(this);
			modal.find('iframe').remove();
		}) 
		
		$("#my_modal_win").modal({
			backdrop: 'static' 
		});
		    
		
	});
	//ajax方式的链接，可以自定义一个回调函数，参数1是a标签对象，参数2是响应数据
	$("a[_confirm_ajax_url]").click(function (event){
		event.preventDefault();

		var _confirm_url = $(this).attr("_confirm_ajax_url");
		var _confirm_ajax_call = $(this).attr("_confirm_ajax_call");
		var a = $(this);
		my_confirm($(this).attr("_confirm_msg"), function (){
			layer.load('请稍候...', 0);
			my_ajax_json(_confirm_url, {}, function (data){
				layer.closeAll();
				if(data && data.result == 1){
					if(_confirm_ajax_call){
						try{
							eval(_confirm_ajax_call).call(this, a, data);
						}catch(e){alert(e)}
					}
					return;
				}else if(data){
					my_alert(data.message);
					return;
				}
				my_alert('操作失败');
				
			});
		});
	});
}

//刷新列表页面
function page_reload(url){
	if(url){
		window.location = url;
	}else{
		window.location.reload();
	}
}