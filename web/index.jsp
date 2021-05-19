<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.net.URLEncoder"%>
<!DOCTYPE html>
<html>
<head>
<title>测试JSON交互</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<script type="text/javascript"src="javascript/jquery-1.8.0.min.js"></script>
<script type="text/javascript">
function testJson(){
    // 获取输入的用户名和密码
	var username = $("#username").val();
	var password = $("#password").val();
	$.ajax({
		url : "${pageContext.request.contextPath }/testJson.do",
		type : "post", 
		// data表示发送的数据
		data : JSON.stringify({"username":username,"password":password}),
		// 定义发送请求的数据格式为JSON字符串
		contentType : "application/json;charset=UTF-8",
		//定义回调响应的数据格式为JSON字符串,该属性可以省略
		dataType : "json",
		//成功响应的结果
		success : function(data){
			if(data != null){					
			  alert("您输入的用户名为："+data.username+
                                 "密码为："+data.password);
			}
		}
	});
}
</script>
</head>
<body>
	<a href="${pageContext.request.contextPath }/download.do?filename=<%=URLEncoder.encode("壁纸.jpg","UTF-8") %>">中文名称文件下载 </a>
	<br />
    <form>
        用户名：<input type="text" name="username" id="username"><br />
        密&nbsp;&nbsp;&nbsp;码：
          <input type="password" name="password" id="password"><br />
	     <input type="button" value="测试JSON交互" onclick=" testJson()" />
    </form> 
</body>
</html>