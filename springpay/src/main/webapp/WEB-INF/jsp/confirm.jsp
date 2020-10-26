<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>alipay confirm page</title>
</head>
<body>
	<form action="/springpay/alipaymd5test/submit.do">
		订单编号：<input type="text" name="out_trade_no" value="T0001"><br>
		商品名称：<input type="text" name="subject" value="开发测试1"><br>
		交易金额：<input type="text" name="total_fee" value="0.01"><br>
		<input type="submit" value="确认提交">
	</form>
</body>
</html>