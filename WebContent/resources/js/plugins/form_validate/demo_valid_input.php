<?php
if($_REQUEST['act'] == "check_nickname"){
	if($_REQUEST['nickname'] == 'andyfoo'){
		echo '{"result":1,"message":"用户已经存在"}';
		die();
	}
	echo '{"result":0,"message":"用户可以注册"}';
	die();
}



?>