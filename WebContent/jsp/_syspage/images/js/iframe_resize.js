<!--

var _iframe_id = "comment_iframe";
var _iframe_min_height = 390;
try{
	_iframe_id = window.self.name;
}catch(e){}

function set_iframe_id(id){
	_iframe_id = id;
}
function set_iframe_min_height(h){
	_iframe_min_height = h;
}
function iframe_resize(){
        var dyniframe   = null;
	try{

		if (document.getElementById){
		

			try{
				dyniframe       = window.parent.document.getElementById(_iframe_id);
			}catch(e){}
		
			if (dyniframe){
				dyniframe.style.display         = "block";
					
				var h = get_body_height();
				if(h < _iframe_min_height)h=_iframe_min_height;
				var obj_h = dyniframe.height + "";
				if(obj_h.indexOf("%") == -1 && parseInt(dyniframe.height, 10) == h)return;
				dyniframe.height        = h;
				//window.status=h;
			}
		}
	}catch(e){

	}
}
function iframe_resize_init(h){
	if(h)set_iframe_min_height(h);
	addEvent(window, "onload", iframe_resize);
	addEvent(window, "onresize", iframe_resize);
	if(!document.all){
		addEvent(window.document, "onclick", iframe_resize);
	}

}

-->