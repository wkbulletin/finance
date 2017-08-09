$(document).ready(function(){
	window.setTimeout(function (){
		$("a,input[type='button'],input[type='reset'],input[type='submit'],button").attr("hidefocus", "true").focus(function (){this.blur();});
	}, 200);

	$('form').attr("autocomplete", "off");


	$("a[title],div[title],span[title],strong[title],td[title],p[title]").each(function (){
			
	});
	/*
	$(document.body).tooltip({
			selector: "a[title],div[title],span[title],strong[title],td[title],p[title]",
			placement: "bottom",
			container: "body"
		});
	*/
});

//将id,pid方式的数据转换为jstree数据
//tree_data2 = get_tree_children_data(tree_data);
//var tree_data = [{"checked":false,"icon":false,"id":3,"pid":0,"text":"sadafs22"},{"checked":false,"icon":false,"id":4,"pid":0,"text":"asdfas3"},{"checked":false,"icon":false,"id":5,"pid":0,"text":"asdfas"},{"checked":false,"icon":false,"id":6,"pid":0,"text":"asdfas"},{"checked":false,"icon":false,"id":7,"pid":0,"text":"asdfas"},{"checked":false,"icon":false,"id":12,"pid":4,"text":"asdfa"},{"checked":false,"icon":false,"id":13,"pid":4,"text":"sadaf"},{"checked":false,"icon":false,"id":14,"pid":3,"text":"sda"}];
function get_tree_children_data(tree_data){
	var node_childs = new Array();
	var tree_data2 = new Array();
	for(var i in tree_data){
		if(!node_childs[tree_data[i].pid]){
			node_childs[tree_data[i].pid] = new Array();
		}
		node_childs[tree_data[i].pid].push(tree_data[i]);
	}
	function get_tree_data(pid){
		var data1 = node_childs[pid];
		if(data1 == null || data1.length == 0)return null;
		for(var i in data1){
			var data2 = get_tree_data(data1[i].id);
			data1[i]['state'] = new Array();
			data1[i]['state']['selected'] = data1[i]['checked'];
			if(data2 != null && data2.length > 0){
				data1[i]['children'] = data2;
			}
		}
		return data1;
	}
	return get_tree_data(0);
}



//ajax方式提交
function ajax_submit_form_push(frm, pre_fun){
	if(!form_v.check(frm)){
		return false;
	}

	  var push_time= $("#tmp_push_time").val();
	  var begin_time= $("#tmp_push_begin_time").val();
	 if(!(check_current_time()&&compare_time(begin_time,push_time))){
		 return false;
	 }
	var frm = $(frm);

	//验证
	//submit
	layer.load('请稍候......', 0);
	var post_data;
	if(typeof(pre_fun) == 'function'){
		post_data = pre_fun(frm);
	}else{
		post_data = frm.serialize();
	}
	my_ajax_json(frm.attr("action"), post_data, function (data){
		layer.closeAll();
		if(data && data.result == 1){
			my_msg(data.message,1, function (){
				if(typeof(parent.page_reload) == 'function'){
					parent.page_reload();
				}
				if(typeof(ajax_submit_ok) == 'function'){
					ajax_submit_ok(data);
				}
				parent.$('#myModal').modal('hide');
			});
			return;
		}else if(data){
			my_alert(data.message, function (){
				if(typeof(ajax_submit_error) == 'function'){
					ajax_submit_error(data);
				}	
			});
			return;
		}
		my_alert('操作失败');
		
	});

	return false;
}


//ajax方式提交
function ajax_submit_form(frm, pre_fun){
	if(!form_v.check(frm)){
		return false;
	}
	var frm = $(frm);

	//验证
	//submit
	layer.load('请稍候......', 0);
	var post_data;
	if(typeof(pre_fun) == 'function'){
		post_data = pre_fun(frm);
	}else{
		post_data = frm.serialize();
	}
	my_ajax_json(frm.attr("action"), post_data, function (data){
		layer.closeAll();
		if(data && data.result == 1){
			my_msg(data.message,1, function (){
				if(typeof(parent.page_reload) == 'function'){
					parent.page_reload();
				}
				if(typeof(ajax_submit_ok) == 'function'){
					ajax_submit_ok(data);
				}
				parent.$('#myModal').modal('hide');
			});
			return;
		}else if(data){
			my_alert(data.message, function (){
				if(typeof(ajax_submit_error) == 'function'){
					ajax_submit_error(data);
				}	
			});
			return;
		}
		my_alert('操作失败');
		
	});

	return false;
}


function check_current_time() {
	var today = new Date();
	var year = today.getFullYear();
	var month = today.getMonth() + 1;
	var day = today.getDate();
	var hours = today.getHours();
	var minutes = today.getMinutes();
	var seconds = today.getSeconds();
	  var endTime= document.getElementById("tmp_push_time").value;
	  
	  if(endTime!=null){
			var endTimes = endTime.substring(0, 10).split('-');
			var times = endTime.substring(10, 19)
			var mss = times.split(':');
			if (year > endTimes[0]) {
				alert('定时推送时间必须大于当前时间！');
				return false;
			}
			if (year >= endTimes[0] && month > endTimes[1]) {
				alert('定时推送时间必须大于当前时间！');
				return false;
			}
			if (year >= endTimes[0] && month >= endTimes[1]
					&& day > endTimes[2]) {
				alert('定时推送时间必须大于当前时间！');
				return false;
			}
			if (year >= endTimes[0] && month >= endTimes[1]
					&& day >= endTimes[2] && hours > mss[0]) {
				alert('定时推送时间必须大于当前时间！');
				return false;
			}
			if (year >= endTimes[0] && month >= endTimes[1]
					&& day >= endTimes[2] && hours >= mss[0]
					&& minutes > mss[1]) {
				alert('定时推送时间必须大于当前时间！');
				return false;
			}
			if (year >= endTimes[0] && month >= endTimes[1]
					&& day >= endTimes[2] && hours >= mss[0]
					&& minutes > mss[1]   && seconds > mss[2]) {
				alert('定时推送时间必须大于当前时间！');
				return false;
			} 
	  }else{
		  return true;
	  }
	  return true;
}



function compare_time(start,endTime ) {


	var startTimes = start.substring(0, 10).split('-');
	var st_mss = start.substring(10, 19).split(':');

	

	var year = startTimes[0];
	var month =startTimes[1];
	var day = startTimes[2];
	var hours = st_mss[0];
	var minutes = st_mss[1];
	var seconds = st_mss[2];

			var endTimes = endTime.substring(0, 10).split('-');
			var times = endTime.substring(10, 19)
			var mss = times.split(':');

			if (year > endTimes[0]) {
				alert('定时推送时间必须小于开始时间！');
				return false;
			}
			if (year >= endTimes[0] && month > endTimes[1]) {
				alert('定时推送时间必须大于开始时间！');
				return false;
			}
			if (year >= endTimes[0] && month >= endTimes[1]
					&& day > endTimes[2]) {
				alert('定时推送时间必须大于当开始时间');
				return false;
			}
			if (year >= endTimes[0] && month >= endTimes[1]
					&& day >= endTimes[2] && hours > mss[0]) {
				alert('定时推送时间必须大于开始时间！');
				return false;
			}
			if (year >= endTimes[0] && month >= endTimes[1]
					&& day >= endTimes[2] && hours >= mss[0]
					&& minutes > mss[1]) {
				alert('定时推送时间必须大于开始时间！');
				return false;
			}
			if (year >= endTimes[0] && month >= endTimes[1]
					&& day >= endTimes[2] && hours >= mss[0]
					&& minutes > mss[1]   && seconds > mss[2]) {
				alert('定时推送时间必须大于开始时间！');
				return false;
			} 

	  return true;
}
//普通方式提交
function submit_form(frm, pre_fun){
	if(!form_v.check(frm)){
		return false;
	}
	
	//submit
	if(typeof(pre_fun) == 'function'){
		pre_fun(frm);
	}
	layer.load('请稍候......', 0);
//	layer.closeAll();
	//parent.page_reload();
	//parent.$('#myModal').modal('hide');
	return true;
}