
;(function($){
	var x_slide = function(elm, options) {
		
		this.elm = $(elm);
		this.opts = $.extend({}, $.fn.x_slide.defaults, options);
		
		this.con = null;
		this.btn = null;
		this.title = null;


		this.is_stop = false;

		this.auto_show_timer = null;
		this.curr_item = 0;

		this.init();
	};

	x_slide.prototype.init = function(){
		this.con = this.elm.find("ul>li");
		this.btn = this.elm.find("div>a");
		this.title = this.elm.find(".titles ul>li");
		
		this.elm.find(".titles").width(this.con.width());
		this.btn.show();

		var _this = this;
		this.elm.hover(function (){
			_this.is_stop = true;
		},function (){
			_this.is_stop = false;
		});

		this.btn.click(function (){
			var index = _this.btn.index($(this));
			_this.curr_item = index;
			_this.show(index);
			return false;	
		});

		_this.auto_show();
		this.auto_show_timer = window.setInterval(function (){
			_this.auto_show();
		}, this.opts.slide_time);

	}
	x_slide.prototype.auto_show = function(){
		if(this.is_stop)return;
	
		if(this.curr_item>=this.con.length){
			this.curr_item=0;
		}
		this.show(this.curr_item);
		this.curr_item++;
	}

	x_slide.prototype.show = function(n){
		if(this.con.length > 0 && n < this.con.length){
			this.con.hide();
			$(this.con[n]).fadeIn(600);

			if(this.opts.OnShowItem){
				this.opts.OnShowItem(n);
			}
		}
		if(this.btn.length > 0 && n < this.btn.length){
			this.btn.removeClass("on");
			$(this.btn[n]).addClass("on");
		}
		if(this.title.length > 0 && n < this.title.length){
			this.title.hide();
			$(this.title[n]).fadeIn(600);
		}
		
	}




	$.fn.x_slide = function(options){
		var opts = $.extend({}, $.fn.x_slide.defaults, options);
		return $(this).each(function() {
			new x_slide(this, opts);
		});
	};
	
	
	$.fn.isInit = function(){
		return this.hasClass("x_slide");
	};


	// default settings
	$.fn.x_slide.defaults = {
		slide_time : 4000,
		OnShowItem : null
	};
})(jQuery);