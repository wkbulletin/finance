<%@ page language="java" contentType="text/html; charset=GBK" isErrorPage="true" pageEncoding="GBK"%><%@ page import="java.io.*,java.util.*"%><%
response.setStatus(HttpServletResponse.SC_OK);
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>提示信息</title>

<link href="/jsp/_syspage/images/css/style.css" rel="stylesheet" type="text/css" />

</head>
<body>
<script src="/jsp/_syspage/images/js/projectout_mini.js"></script>
<script src="/jsp/_syspage/images/js/iframe_resize.js"></script>

<div style="width:480px;height:240px;overflow:hidden; clear:both; margin:0px auto; background:url(/jsp/_syspage/images/body_bg.png); ">
<table width="100%" height="230" border="0" cellpadding="0" cellspacing="0" style="padding-top:3px;">
	<tr>
		<td width="128" height="38">&nbsp;</td>
		<td>&nbsp;</td>
		</tr>
	<tr>
		<td align="right" valign="top" style="padding-right:25px;"><img src="/jsp/_syspage/images/alert.png" border="0" /></td>
		<td valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td height="28" style="color:#333333; font-size:14px; font-weight:bold;">您浏览的网页暂时不能显示</td>
				</tr>
			<tr>
				<td height="10">&nbsp;</td>
				</tr>
			<tr>
				<td height="25" style="font-size:14px;">您正在浏览的网页可能已被删除、重命名或暂时不可用。<br />
					试试下面几种方法吧：</td>
				</tr>
			<tr>
				<td height="25" style="font-size:12px;"><strong>1、检查网址是否正确。 </strong></td>
			</tr>
			<tr>
				<td height="25" style="font-size:12px;"><strong>2、检查输入是否有误。 </strong></td>
			</tr>
			<tr>
				<td height="25">&nbsp;</td>
			</tr>
		</table></td>
	</tr>
</table>

</div>


<script>
if(window.top.location != window.location){
	iframe_resize_init(100);
}
</script>
   
</body>
</html>