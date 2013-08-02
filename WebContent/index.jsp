<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div>
		<a href="?lang=en_US">English</a> <a href="?lang=zh_CN">中文</a>
	</div>
	<br />
	<div>${lang["welcome"]} - ${lang.name} - ${lang.sex}</div>
	<br />
	<div>
		<form action="upload_adapter.do" method="POST" enctype="multipart/form-data">
			<input type="text" name="name_1" value="value_1"> 
			<input type="file" name="file_input_name_1">
			<input type="text" name="name_2" value="value_2_1"> 
			<input type="file" name="file_input_name_2">
			<input type="text" name="name_2" value="value_2_2"> 
			<input type="submit">
		</form>
	</div>
	<br />
	<h2>welcome</h2>
	<a href="login.do">login.do</a>
	<a href="index.do">index.do</a>
	<a href="news.do">news.do</a>
	<a href="druid">druid</a>
	<br />
	<a href="httl.do">httl.do</a>
	<a href="fm.do">fm</a>
	<a href="fm2.do">fm2</a>
	<a href="vl.do">vl</a>
	<a href="vl2.do">vl2</a>
	<a href="bt.do">bt</a>
	<a href="testjsp.do">testjsp</a>
	<a href="1.htm">1.htm</a>
	<br />
	<a href="404.htm">404.htm</a>
	<a href="test_3.htm">test_3.htm</a>
	<br />
	<a href="json.htm">json.htm</a>
	<a href="xml.htm">xml.htm</a>
	<a href="text.htm">text.htm</a>
	<a href="testViewType.htm">testViewType.htm</a>
	<a href="test_action_path_default_value.htm">test_action_path_default_value.htm</a>
	<br />
	<a href="test_ctx.htm">test_ctx.htm</a>
	<a href="test_all.htm">test_all.htm</a>
	<br />
	<form action="1.do" method="GET">
		<input type="submit" value="1?get" />
	</form>
	<form action="1.do" method="POST">
		<input type="submit" value="1?post" />
	</form>
	<form action="2.do" method="GET">
		<input type="submit" value="2?get" />
	</form>
	<form action="2.do" method="POST">
		<input type="submit" value="2?post" />
	</form>
	<br />
	<form action="test_dev_filter.htm" method="POST">
		<input name="id" value="1" /> <input name="id" value="3" /> <input
			name="id" value="5" /> <input name="id" value="7" /> <input
			name="id" value="9" /> <input name="int1" value="1" /> <input
			name="int2" value="2" /> <input name="str1" value="str1" /> <input
			name="str2" value="str2" /> <input name="account1.username"
			value="account1.username" /> <input name="account2.username"
			value="account2.username" /> <input name="bol" value="true" /> <input
			type="submit" value="submit" />
	</form>
	<h2>test abs action</h2>
	<form action="test_abs_action.htm" method="POST">
		<input name="id" value="1" /> <input name="id" value="3" /> <input
			name="id" value="5" /> <input name="id" value="7" /> <input
			name="id" value="9" /> <input name="int1" value="1" /> <input
			name="int2" value="2" /> <input name="str1" value="str1" /> <input
			name="str2" value="str2" /> <input name="account1.username"
			value="account1.username" /> <input name="account2.username"
			value="account2.username" /> <input type="submit" value="submit" />
	</form>
	<br />
	<form action="upload.htm" method="post" enctype="multipart/form-data">
		<input type="file" name="file_input_name" /> <input type="file"
			name="file_input_name" /> <input type="submit" value="上传" />
	</form>
</body>
</html>