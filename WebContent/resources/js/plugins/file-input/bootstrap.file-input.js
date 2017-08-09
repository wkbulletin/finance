/**
 * 利用label辅助点击file框
 * 可完美解决在ie中的“访问限制”问题（即必须是用户点击才能进行文件上传）
 * 仅对class为custom-file-input应用
 */
$(function() {
	$('input[type=file]').each(function(i,elem){
		var input = $(this);

		if(!input.prop("id")) {
			input.prop("id", "nfile_" + i);
		}
		var btnTitle = "选择文件……";
		if (input.prop('title')) {
			btnTitle = input.prop('title');
		}
		input.wrap("<div class='file-input'></div>");

		var btn = $('<a class="btn btn-outline btn-default" style="vertical-align:baseline"><label for="' + input.prop("id") + '">' + btnTitle + '</label></a>');
		input.before(btn);
		input.addClass("file-input-opacity");

		input.change(function() {
			var btn = $(this).parents(".file-input").find("a");
			btn.next('.name').remove();
			btn.after('<span class="name">'+$(this).val()+'</span>');
		});

	});
	//修正firefox label不能触发输入框点击
	if(navigator.userAgent.indexOf("Firefox") > 0) {
		$(document).on('click', 'label', function(e) {
			if(e.currentTarget === this && e.target.nodeName !== 'INPUT') {
				$(this.control).click();
			}
		});
	}

});