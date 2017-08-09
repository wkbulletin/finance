
//检测身份证号
var powers=new Array("7","9","10","5","8","4","2","1","6","3","7","9","10","5","8","4","2");
var parityBit=new Array("1","0","X","9","8","7","6","5","4","3","2");
var idcard_sex="M";
//校验身份证号码的主调用
function validId(_id){
	if(_id=="")return false;
	var _valid=false;
	if(_id.length==15){
		_valid=validId15(_id);
		if(!_valid){
			return false;
		}
		_valid=validId18(validId15to18(_id));
	}else if(_id.length==18){
		_valid=validId18(_id);
	}
	if(_valid){
		_valid = validIdIsBan(_id);
	}
	if(!_valid){
		//alert("身份证号码有误,请检查!");
		return false;
	}
	return _valid;
}    

/*
	var str = "";
	str = "111111111111111";
	str = "555555555555551111";
	str = "123456789012345111";
	//str = "123456789012345";
	v = validIdIsBan(str);
	//alert(v+"");
*/
//检测是否为无效数字
function validIdIsBan(_id){
	_id =_id+"";
	for(var i = 0; i < 10; i++){
		var rule	= new RegExp("(["+i+"]{15,18})");
		if(rule.test(_id)){
			return false;
		}
	}
	var ban = [
		"0123456789",
		"1234567890",
		"123456789",
		"012345678901234",
		"012345678901234567",
		"123456789012345",
		"123456789012345789",
		"987654321987654",
		"987654321987654321",
		"098765432109876",
		"098765432109876543",
		];
	for(var i = 0; i < ban.length; i++){
		ban[i] = ban[i]+"";
		if(ban[i] == _id){
			return false;
		}
		if(ban[i] == _id.substr(0, ban[i].length)){
			return false;
		}
	}
	return true;
	
}
//15位转换为18位
function validId15to18(_id){
	_id =_id+"";
	_id = _id.substring(0,6) + "19" + _id.substring(6,15);
	var _num=_id;
	var _power=0;
	for(var i=0;i< 17;i++){
		//校验每一位的合法性

		if(_num.charAt(i)<'0'||_num.charAt(i)>'9'){
			return false;
			break;
		}else{
			//加权

			_power+=parseInt(_num.charAt(i))*parseInt(powers[i]);
			//设置性别

			if(i==16&&parseInt(_num.charAt(i))%2==0){
				idcard_sex="F";
			}else{
				idcard_sex="M";
			}
		}
	}
	//取模

	var mod=parseInt(_power)%11;
	return _id + parityBit[mod] + "";
	//alert(_id);
}
//校验18位的身份证号码

function validId18(_id){
	_id=_id+"";
	var _num=_id.substr(0,17);
	var _parityBit=_id.substr(17);
	var _power=0;
		
	var year=_id.substr(8,2);
	var month=_id.substr(10,2);
	var day=_id.substr(12,2);
	
//	if(year<'01'||year >'90')return false;
	//校验月份

	if(month<'01'||month >'12')return false;
	//校验日

	if(day<'01'||day >'31')return false;
	//设置性别
	
	for(var i=0;i< 17;i++){
		//校验每一位的合法性

		if(_num.charAt(i)<'0'||_num.charAt(i)>'9'){
			return false;
			break;
		}else{
			//加权

			_power+=parseInt(_num.charAt(i))*parseInt(powers[i]);
			//设置性别

			if(i==16&&parseInt(_num.charAt(i))%2==0){
				idcard_sex="F";
			}else{
				idcard_sex="M";
			}
		}
	}

	if(_parityBit == 'X' || _parityBit == 'x'){
		return true;
	}
	//取模
	var mod=parseInt(_power)%11; 
	if(parityBit[mod]==_parityBit){
		return true;
	}
	return false;
}
//校验15位的身份证号码

function validId15(_id){
	_id=_id+"";
	for(var i=0;i<_id.length;i++){
		//校验每一位的合法性

		if(_id.charAt(i)<'0'||_id.charAt(i)>'9'){
			return false;
			break;
		}
	}
	var year=_id.substr(6,2);
	var month=_id.substr(8,2);
	var day=_id.substr(10,2);
	var sexBit=_id.substr(14);
	//校验年份位

//	if(year<'01'||year >'90')return false;
	//校验月份

	if(month<'01'||month >'12')return false;
	//校验日

	if(day<'01'||day >'31')return false;
	//设置性别

	if(sexBit%2==0){
		sex="F";
	}else{
		sex="M";
	}
	return true;
} 