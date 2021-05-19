var code = true;
var time = 60;
var interval = null;
var isLogin = false;
$(function(){
	//查询用户信息
	getUserInfo();
	
	$("#verifycode").VerifyCode();
	
	$("#usermobile").focus(function(){
		$("#usermobile_error_div").hide();
	});
	$("#passWordIpt").change(function(){
		if($(this).val()!=''){
			$("#labelpwd").hide();
		}
	});
	$("#passWordIpt").focus(function(){
		$("#passwd_error_div").hide();
		$("#usermobile_error_div").hide();
	});
	$("#verifycode").focus(function(){
		$("#usermobile_error_div").hide();
		$("#code_error_div").hide();
	});
	
	$("#mobile").focus(function(){
		$("#mobile_div").hide();
		$("#message_div").hide();
	});
	$("#messagecode").focus(function(){
		$("#message_div").hide();
		$("#mobile_div").hide();
	});
	
	//获取短信验证码
	$("#getCodeBtn").click(function(){
		var divHtml = $(this).html();
		if(code){
			code = false;
			if(divHtml=='获取验证码' || divHtml=='重新获取'){
				var obj = $(this);
				var mobile = $("#mobile").val();
				if(mobile=="" || mobile=='手机号'){
					$("#mobile_div").show();
					$("#mobile_errorinfo").html("请输入手机号码");
					return;
				}
				$.post("getmobilecode.do?ran="+Math.random(), {
					"mobile":mobile
				}, function(result) {
					var resultJson = eval("("+result+")");
					if(resultJson.success==1){
						//成功
						time = 60;
						interval = setInterval(function(){
							var message = countDown();
							$(obj).html(message);
						},1000); 
					}if(resultJson.success==0){
						code = true;
						//发送失败
						$("#mobile_div").show();
						$("#mobile_errorinfo").html("网络异常,请稍后再试");
					}if(resultJson.success==2){
						//不存在
						code = true;
						$("#mobile_div").show();
						$("#mobile_errorinfo").html("手机号码不存在");
					}if(resultJson.success==3){
						//不存在
						code = true;
						$("#mobile_div").show();
						$("#mobile_errorinfo").html("超过频次限制,拒绝获取短信验证码");
					}
				});
			}
		}
	});
});

//帐号密码登录
function WebLogin(){
	var usermobile = $("#usermobile").val();
	var password = $("#passWordIpt").val();
	var verifycode = $("#verifycode").val();
	if(usermobile=="" || usermobile=='通行证/手机号/别名'){
		$("#usermobile_error_div").show();
		$("#usermobile_error").html("请输入139邮箱帐号");
		return;
	}
	if(password==""){
		$("#passwd_error_div").show();
		$("#passwd_error").html("请输入密码");
		return;
	}
	if(verifycode=="" || verifycode=='请输入正确的字母或数字'){
		$("#code_error_div").show();
		$("#code_error").html("请输入验证码");
		return;
	}
	$.post("login.do?ran="+Math.random(), {
		account : usermobile,
		password : password,
		verifycode : verifycode
	}, function(result) {
		var resultJson = eval("("+result+")");
		$.yz.VerifyReset();
		if(resultJson.success==-1){
			$("#usermobile_error_div").show();
			$("#usermobile_error").html(resultJson.desc);
			return;
		}if(resultJson.success==-2){
			$("#code_error_div").show();
			$("#code_error").html(resultJson.desc);
			return;
		}
		$("#loginDiv").hide();
		location.href="index.do";
	});
};
//短信验证码登录
function SMSLogin(){
	var usermobile = $("#mobile").val();
	var verifycode = $("#messagecode").val();
	if(usermobile=="" || usermobile=='手机号'){
		$("#mobile_div").show();
		$("#mobile_errorinfo").html("请输入手机号码");
		return;
	}
	if(verifycode=="" || verifycode=='短信验证码'){
		$("#message_div").show();
		$("#message_errorinfo").html("请输入验证码");
		return;
	}
	$.post("smslogin.do?ran="+Math.random(), {
		"mobile" : usermobile,
		"verifycode" : verifycode,
	}, function(result) {
		var resultJson = eval("("+result+")");
		$.yz.VerifyReset();
		if(resultJson.success==-1){
			$("#mobile_div").show();
			$("#mobile_errorinfo").html(resultJson.desc);
			return;
		}
		if(resultJson.success==-2){
			$("#message_div").show();
			$("#message_errorinfo").html(resultJson.desc);
			return;
		}
		if(resultJson.success==-3){
			$("#mobile_div").show();
			$("#mobile_errorinfo").html(resultJson.desc);
			return;
		}
		if(resultJson.success == 1){
			$("#loginDiv").hide();
			location.href="index.do";
		}else{
			$("#mobile_div").show();
			$("#mobile_errorinfo").html(resultJson.desc);
			return;
		}
		
	});
};

//获取用户信息
function getUserInfo(){
	$.post("userinfo.do?ran="+Math.random(), {
	}, function(result) {
		var resultJson = eval("("+result+")");
		if(resultJson.islogin == 1){
			isLogin = true;
		}
		$("#mailnum").html(resultJson.mailnum);
		$("#score").html(resultJson.score);
		//查询是否已订阅杂志
		var magazineIds = [38601,38553,39378,39416];
		console.log("sub:" + resultJson.magids);
		if(resultJson.magids != "" && resultJson.magids != "undefined"){
			var  mgzString = resultJson.magids;
			for(var i=0;magazineIds.length > i; i++){
				var pos = mgzString.indexOf(magazineIds[i]);
				console.log("pos " + pos + "," +  mgzString + "|" + magazineIds[i]);
				if(pos >= 0){
					$("#mgzID_" + magazineIds[i]).addClass("cur hide");
					var mgzbtn = "#btn" + magazineIds[i];
					var cmgzbtn = "#cbtn" + magazineIds[i];
					$(mgzbtn).hide();
					$(cmgzbtn).show();
				}
			}
		}
	});
};

//弹出登陆框
function showLoginBox(){
	$("#loginDiv").show();
}
//关闭窗口
function hidePopBox(id){
	$("#"+id).hide();
}

//倒计时
function countDown(){
	var message = "";
	if(time>1){
		time--;
		message = time+"s后重试";
	}else{
		code = true;
		clearInterval(interval);
		message = "重新获取";
	}
	return message;
}

//订阅杂志
function subscribe(obj, mid){
	if($(obj).is(".hide")){
		window.location.href="http://mail.10086.cn/login/sso.aspx?id=dingyuezhongxin_"+mid+"&sid="+$('#curSid').val();
	}else{
		$.post("subscribe.do?ran="+Math.random(),{
			magzineid: mid
		},function(jsonData){
			var resultJson = eval("("+jsonData+")");
			if (resultJson.result == -100){
				showLoginBox();
				return;
			}
			if(resultJson.result > 0){
				$(obj).addClass("cur hide");
				alert("订阅成功！您可以在139邮箱云邮局-我的订阅-我的资讯/我的杂志”中进行阅读。页面推荐杂志仅有一次订阅机会，不可重复订阅。");
				addChance(3);
			} else{
				alert(resultJson.desc);
			}
		});
	}
}
//增加抽奖机会
function addChance(fid){
	$.post("addchance.do?ran="+Math.random(),{
		from: fid
	},function(jsonData){
		var resultJson = eval("("+jsonData+")");
		if (resultJson.result == -100){
			showLoginBox();
			return;
		}
		if(resultJson.result > 0){
			var left = parseInt($("#divDrawChance").html()) + 1;
			console.log('add chance ' + left);
			$("#divDrawChance").html(left);
		}
		return;
	});
}

//分享到第三方
function shareto(id){
	if(isLogin == 0){
		showLoginBox();
		return false;
	}
	http://happy.mail.10086.cn:8001/
	var pic=encodeURIComponent('https://happy.mail.10086.cn/jsp/web/cn/headlines/images/share.jpg');
	var url=encodeURIComponent('https://happy.mail.10086.cn/jsp/web/cn/headlines/index.do');
	addChance(2);//分享获得抽奖机会
	if(id=="weixin"){
		$('.jiathis_button_weixin').click();
		return;
    }else if(id=="weibo"){
		var title=encodeURIComponent('我正在玩“我要上头条”，都要被自己的头条惊呆了~快来看看你的头条是什么~');
		window.open("http://v.t.sina.com.cn/share/share.php?url="+url+"&appkey=3347543801&title="+title+"&pic="+pic,"_blank");
		return;
    }else if(id=="qzone"){
		var title=encodeURIComponent('我正在玩“我要上头条”，都要被自己的头条惊呆了~快来看看你的头条是什么~');
    	window.open('http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url='+ url+ '&title='+title+'&pics='+pic,'_blank');
    }
}
