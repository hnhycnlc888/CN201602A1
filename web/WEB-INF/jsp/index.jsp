<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="cn.richinfo.login.pojo.UserInfo"  %>
<%@ page import="cn.richinfo.login.impl.Login"  %>
<%@ page import="cn.richinfo.core.spring.SpringContext"%>

<%
UserInfo userInfo = SpringContext.getBean("Login", Login.class).getUserInfo(request, response);
String sid = "";
if(userInfo != null){
	sid = userInfo.getSid();
}
request.setAttribute("sid", sid);
%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="renderer" content="webkit">
<title>我要上头条</title>
<link type="text/css" href="style/layout.css" rel="stylesheet">
<link type="text/css" href="style/module_top.css" rel="stylesheet">
</head> 
<body>
${username}
 <div id="module_header">
   <div class="wrap clearfix">
     <h1 class="fl logo"><a href="./ssoto.do?type=1&flag=2&sid=${sid}" target="_blank"><img src="images/logo.jpg" width="183" height="30" alt="139邮箱" title="139邮箱"></a>
         <a href="./ssoto.do?type=1&flag=2&sid=${sid}" target="_blank"><img src="images/module_top_logo.jpg" width="120" height="29" alt="139邮箱" title="139邮箱"></a>
     </h1> 
     <!--没登陆之前START-->  
     <ul class="fr loadBefore" <c:if test="${login }">style="display:none;"</c:if>
     							<c:if test="${!login }">style="display:block;"</c:if>>
       <li><a href="https://www.cmpassport.com/umc/reg/phone/?from=3&_fv=5" target="_blank" class="regBtn">免费注册</a></li>
       <li><a href="javascript:void(0);" class="loadIcon module_top_icon" title="用户登录" onclick="showLoginBox();">用户登录</a></li>
       <li><a href="./ssoto.do?type=1&flag=2&sid=${sid}" target="_blank" class="emailIcon module_top_icon" title="邮件">邮箱</a></li>
       <li><a href="./ssoto.do?type=2&flag=5&sid=${sid}" target="_blank" class="giftIcon module_top_icon" title="积分">积分</a></li>
     </ul>
     <!--没登陆之前END--> 
    
    <!--登录之后Start--> 
     <ul class="fr loadAfter" <c:if test="${login }">style="display:block;"</c:if>
     							<c:if test="${!login }">style="display:none;"</c:if>>
       <li><span class="clearfix personBtn">
       		<span class="module_top_icon personIcon fl">&nbsp;</span>
       		<span class="telephoneBtn">${mobile}</span></span>
       </li>
       <li><a href="./logout.do" class="quitBtn">退出</a></li>
       <li>
       		<a href="./ssoto.do?type=1&flag=2&sid=${sid}" target="_blank" class="emailNumIcon module_top_icon" title="未读邮件数">
       		<span id="mailnum">0</span> 封</a>
       </li>
       <li>
       		<a href="./ssoto.do?type=2&flag=5&sid=${sid}" target="_blank" class="giftNumIcon module_top_icon" title="邮箱积分数">
       		<span id="score">0</span> 分</a>
       </li>
     </ul>
    <!--登录之后END-->   
  </div>
   
	<!--弹窗START-->
		<div class="module_popLayout_load" style="display: none;"
			id="loginDiv">
			<div class="title clearfix">
				<div class="title_general title_account    fl general_style"
					id="title_account"
					onclick="_zc.titleCheck(this,'title_msg','account_tab')">帐号登录</div>
				<div class="title_general title_msg    fl general_style"
					id="title_msg"
					onclick="_zc.titleCheck(this,'title_account','msg_tab')">短信登录</div>
				<div class="title_bottom_style general_style"
					id="title_bottom_style"></div>
				<a href="javascript:void(0);" onclick="hidePopBox('loginDiv')"
					class="module_closeBtn module_top_icon">关闭</a>
			</div>
			<div class="loadWrap">
				<table align="center" cellpadding="0" cellspacing="0" width="320"
					class="inputTable account_tab  " id="account_tab">
					<tr>
						<td>
							<div class="txtbox txtbox01" style="display: none;" id="usermobile_error_div">
								<span id="usermobile_error">帐号不存在<a
									href="https://www.cmpassport.com/umc/reg/phone/?from=3&_fv=5"
									target="_blank">立即注册</a></span>
								<div class="tips_ico"></div>
							</div>
							<div class="accountWrap">
								<div class="module_top_icoInput module_top_ico03"></div>
								<input type="text" id="usermobile" value="通行证/手机号/别名"
									class="accountIpt" onFocus="_zc.inputFocus(this,'通行证/手机号/别名')"
									onBlur="_zc.inputBlur(this,'通行证/手机号/别名')">
								<div class="ico139"></div>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="txtbox txtbox02" style="display: none;" id="passwd_error_div">
								<span id="passwd_error">帐号或密码输入错误</span>
								<div class="tips_ico"></div>
							</div>
							<div class="passWordWrap">
								<div class="module_top_icoInput module_top_ico04"></div>
								<label for="passWordIpt" class="labelpwd" id="labelpwd">密码</label>  
								<input type="password" class="passWordIpt" id="passWordIpt"
									onFocus="_zc.passWordFocus(this)"
									onBlur="_zc.passWordBlur(this)">
							</div>
						</td>
					</tr>
					<tr>
						<td>

							<div class="txtbox txtbox03" style="display: none;" id="code_error_div">
								<span id="code_error">验证码输入错误</span>
								<div class="tips_ico"></div>
							</div>

							<div class="codeWordWrap">
								<div class="module_top_icoInput module_top_ico05"></div>
								<input type="text" id="verifycode" value="请输入正确的字母或数字"
									class="yzmIpt" onFocus="_zc.inputFocus(this,'请输入正确的字母或数字');"
									onBlur="_zc.inputBlur(this,'请输入正确的字母或数字');">
								<div class="yzmIpt_hint_error"></div>
								<!--yzmIpt_hint_error yzmIpt_hint_right-->
							</div>

							<div class="moudle_code clearfix" id="moudle_code">
								<div class="moudle_code_img fl">
									<img src="images/moudule_code.jpg" />
								</div>

								<div class="module_pop_refresh fl module_opacity"></div>
							</div>
						</td>
					</tr>
					<tr>
						<td><a href="javascript:void(0);" class="loadBtn" onclick="WebLogin()">登 录</a> <a
							href="https://www.cmpassport.com/umc/management/forgetpwd/mobile/reset/?_fv=5&from=3"
							target="_blank" class="getPassWords">忘记密码</a></td>
					</tr>
				</table>
			<table align="center" cellpadding="0" cellspacing="0" width="320"
					class="inputTable msg_tab hide" id="msg_tab">
					<tr>
						<td>
							<div class="txtbox txtbox01" style="display: none;" id="mobile_div">
								<span><label id="mobile_errorinfo"></label> <a
									href="https://www.cmpassport.com/umc/reg/phone/?from=3&_fv=5"
									target="_blank">立即注册</a></span>
								<div class="tips_ico"></div>
							</div>
							<div class="accountWrap_msg fl" >
								<div class="module_top_ico08 module_top_icoInput"></div>
								<input type="text" id="mobile" value="手机号" class="accountIpt_msg"
									onFocus="_zc.inputFocus(this,'手机号')"
									onBlur="_zc.inputBlur(this,'手机号')">
							</div>
							<div class="getCodeBtn" id="getCodeBtn">获取验证码</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="txtbox txtbox03" style="display: none;" id="message_div">
								<span id="message_errorinfo">验证码输入错误</span>
								<div class="tips_ico"></div>
							</div>
							<div class="codeWordWrap">
								<div class="module_top_icoInput module_top_ico05"></div>
								<input type="text" id="messagecode" value="短信验证码" class="yzmIpt  "
									onFocus="_zc.inputFocus(this,'短信验证码');"
									onBlur="_zc.inputBlur(this,'短信验证码');">
								<div class="yzmIpt_hint_right"></div>
							</div>
							<div class="moudle_code clearfix" id="moudle_code2">
								<div class="moudle_code_img fl">
									<img src="images/moudule_code.jpg" />
								</div>
								<div class="module_pop_refresh fl module_opacity"></div>
							</div>
						</td>
					</tr>
					<tr>
						<td><a href="javascript:void(0);" class="loadBtn_msg" onclick="SMSLogin()">登 录</a></td>
					</tr>
				</table>
			</div>
		</div>
		<!--弹窗END-->   
</div>
 <div class="main">
 	<input type="hidden" id="divIsSSO" value="${sso }"/>
     <div id="page1" class="page1">
             <div class="page-header pr">
             	<!-- 返回首页链接 Start -->
                <a href="./index.do?sid=${sid }" class="return">

                </a>
                <!-- 返回首页链接 End -->
                 <div class="page1-title pa "></div>
                 <div class="page1-date pa p18 col_425 center ">
                     <div>${year }/${month }</div>
                     <div class="p60 page1-day">${day }</div>
                     <div>${week }</div>
                 </div>
                  


             </div>
             <div class="page1-module01 lay_auto pr">
                 <div id="divNickName" class="module01-txt pa">${nick }</div>
                 <div class="module01-btn pa opacity90"></div>
                <div class="module01-txt01">
                    	生成头条去抽奖，GO!GO!GO!
                </div>
             </div>
             <div class="page1-module02 lay_auto">

             </div>
             <div class="page1-module03 lay_auto">

             </div> 
       </div>
       <div id="page2"  class="page2">
             <div class="page-header pr"> 
             	<!-- 返回首页链接 Start -->
                <a href="./index.do?sid=${sid }" class="return">

                </a>
                <!-- 返回首页链接 End -->
                 <div class="page2-getGiftwrap">
                     <div class="p18 col center col_425 page2-giftTxt">剩余<span id="divDrawChance" class="col_b34">${chance }</span>次抽奖机会</div>
                     <div class="page2-getGiftBtn opacity90"></div>
                 </div> 
             </div>
             <div class="page2-module clearfix">
                  <div class="fl page2-left">
                      <div class="page2-news page2-news1  hide">
                           <div class="p28 news-title">22线明星<span class="name">#nick#用户</span>竟是王健林私生子，且与胡歌霍建华是至交！</div>
                          <img src="images/1.jpg" />
                          <div class="p16 news-txt">
                              22线明星<span class="name">#nick#用户</span>据悉家庭背景雄厚，据云邮局八卦协会爆料<span class="name">#nick#用户</span>是亚洲首富王健林第32代私生子，王思聪经常带着<span class="name">#nick#用户</span>混迹于各大夜店，泡网红、开壕车，据悉东北著名F4高大帅气威猛之宋小宝是<span class="name">#nick#用户</span>的二舅，由于受到其二舅影响，<span class="name">#nick#用户</span>自带幽默细胞，深得同行喜爱，据悉<span class="name">#nick#用户</span>经常与胡歌、霍建华等大牌明星吃着火锅唱着歌。
                          </div>
                      </div>
                      <div class="page2-news page2-news2 hide  ">
                           <div class="p28 news-title"><span class="name">#nick#用户</span>在吉尼斯世界之夜荣获亚洲第一杀马特称号，实至名归！</div>
                          <img src="images/2.jpg" />
                          <div class="p16 news-txt">
                              号外！号外！<span class="name">#nick#用户</span>作为乡村非主流挑战吉尼斯世界纪录，最终获得亚洲第一杀马特称号，外媒称<span class="name">#nick#用户</span>高端大气上档次，低调奢华有内涵，奔放洋气有深度，简约时尚国际范，低端粗俗甩节操，土憋矫情无下限，装模作样绿茶婊，外猛内柔女汉子，卖萌嘟嘴剪刀手，忧郁深沉无所谓，狂拽帅气吊炸天，冷艳高贵接地气，时尚亮丽小清新，可爱乡村非主流，贵族王朝杀马特。
                          </div>
                      </div>
                       <div class="page2-news page2-news3  hide ">
                           <div class="p28 news-title"><span class="name">#nick#用户</span>花费20万人民币造17万假币捐助乞丐！同时靠行乞游遍全国！</div>
                          <img src="images/3.jpg" />
                          <div class="p16 news-txt">
                              2888年13月32日，警方抓获一起特大假币制造团伙，<span class="name">#nick#用户</span>等三人花费20万人民币制造出17万钢镚，据CCAV报到称，警方发现三人的17万假币仅剩1元，<span class="name">#nick#用户</span>表示三人带着假币在全国各地旅游，看到有残疾的乞讨者，就会投一个硬币，一年内共捐助16.9999万硬币，至于旅游其他支出费用，均是行乞所得，连警察都无法查证<span class="name">#nick#用户</span>是好人还是坏人。
                            </div>
                      </div>
                       <div class="page2-news page2-news4  hide ">
                           <div class="p28 news-title"><span class="name">#nick#用户</span>不畏被讹风险勇扶大妈，大写的赞！但……看大妈怎么说！</div>
                          <img src="images/4.jpg" />
                          <div class="p16 news-txt">
                              勇敢青年<span class="name">#nick#用户</span>勇扶跌倒大妈，北京时间22：22分，大明村村东头一老妇不慎跌倒，骑三轮车卖豆腐路过的<span class="name">#nick#用户</span>当即扔下三轮车，扶起老奶奶。事后，沃德天记者采访<span class="name">#nick#用户</span>时，他说道：我当时什么都没想，看到大妈倒在地上我就立马奔了过去。记者在现场听到震耳欲聋的嗓音，只听大妈说道：干什么玩意儿，我约了老张头一起打扑克，刚坐下，咋的，占你座了啊。本台记者跟踪报道。
                            </div>
                      </div>
                       <div class="page2-news page2-news5   hide">
                           <div class="p28 news-title">残障<span class="name">#nick#用户</span>终成一代股神身价达1500亿！他是天才呢还是天才呢？</div>
                          <img src="images/5.jpg" />
                          <div class="p16 news-txt"> 
                              <span class="name">#nick#用户</span>自小顶着炒股神童的头衔，在16岁东借西凑终于凑齐了一万元入市，结果一个星期就赚了50万，记者问他怎么赚的？他说“给人荐股，跌停了，被人打断了腿，对方赔的！”于是 <span class="name">#nick#用户</span>拿着这人生的第一桶金，在股市中摸爬滚打，跨足各大投资行业，透过资本市场为自己身家保值增值。至今，身家已达到1500亿。
  </div>
                      </div>
                       <div class="page2-news page2-news6 hide">
                           <div class="p28 news-title">钱多任性，彩民<span class="name">#nick#用户</span>在大街上撒钱“济贫”啦！你还不赶紧去捡？！</div>
                          <img src="images/6.jpg" />
                          <div class="p16 news-txt">
                              3.599亿双色球获得者<span class="name">#nick#用户</span>带猴头面具领奖，云邮局体彩网 10月10日晚，第14118期开奖。本期开奖送6注头奖，被这位来自天津的幸运彩民<span class="name">#nick#用户</span>斩获。当记者问到将如何使用笔钱时，<span class="name">#nick#用户</span>说：“我会带着一麻袋百元大钞走在路上，然后麻袋下面撕个口，边走边漏钱，捡到钱的好心人会跟我说：嗨，你的钱掉了。我回眸一笑告诉他：是你的钱。红红火火哈哈哈哈” 
                           </div>
                      </div>
                      <div class="page2-share">
                          <ul class="ulTag ulShare ">
                              <li onclick="shareto('weixin');" class="share-wx change" title="微信分享"></li>
                              <li onclick="shareto('weibo');" class="share-xl change" title="新浪分享"></li>
                              <li onclick="shareto('qzone');" class="share-qqkj change" title="qq空间分享"></li>
                          </ul>
                          <a class="jiathis_button_weixin"> </a>
                      </div>
                      <div class="page2-linkArea clearfix">
                          <ul class="fl ulLink col_425">
                              <li><a href="./ssoto.do?type=2&flag=5" target="_blank">邮箱积分当钱花，缤纷好礼属于你！</a></li>
                              <li><a href="./ssoto.do?type=2&flag=10" target="_blank">得新，更应手--139邮箱新版见证</a></li>
                              <li><a href="./ssoto.do?type=2&flag=6" target="_blank">为爱充值，100%赢取安全保险</a></li>
                              <li><a href="./ssoto.do?type=2&flag=7" target="_blank">发贺卡赚积分，抽取丰富好礼。</a></li>
                              <li><a href="./ssoto.do?type=2&flag=8" target="_blank">疯了！好奖拿不完！简直停不下来！</a></li>
                              <li><a href="http://mail.10086.cn/login/sso.aspx?id=dingyuezhongxin&sid=${sid }" target="_blank">一站式服务，尽在云邮局，点击发现精彩</a></li> 
                          </ul>
                          <div class="fr page2-imgModule opacity80">
                              <a href="./ssoto.do?type=2&flag=8" target="_blank"><img src="images/ad1.jpg" /></a>
                          </div>
                      </div>
                  </div>
                  <div class="fr page2-right">
                      <div class="module-order">
                        <div class="module-order-item">
                            <img src="images/order_img01.png" class="module-order-img" />
                            <div id="mgzID_38601" onclick="subscribe(this,38601);" class="order-btn opacity90"></div>
                        </div>
                        <div class="module-order-item">
                            <img src="images/order_img02.png" class="module-order-img" />
                            <div id="mgzID_38553" onclick="subscribe(this,38553);" class="order-btn opacity90"></div>
                        </div>
                        <div class="module-order-item">
                            <img src="images/order_img03.png" class="module-order-img" />
                            <div id="mgzID_39378" onclick="subscribe(this,39378);" class="order-btn opacity90"></div>
                        </div>
                        <ul class="ulTag module-order-point">
                            <li class="cur"></li>
                            <li></li>
                            <li></li>
                        </ul>
                    </div>
                      <div class="module-buy">
                          <img src="images/order_img00.png" />
                          <div class="p14 module-order-txt "> 
                              心事.领御<br />
                             67588位用户<br />
                            <span class="p18 col_b34">￥2.00</span>/月</div>
                          <div id="btn39416" onclick="location.href='./ssoto.do?type=3&mid=39416&sid=${sid}';" class="order-btn opacity90 ">
                          <div id="cbtn39416" class="order-btn opacity90 cur hide">
                          </div>

                      </div>
                  </div>
             </div>
         </div>
 </div>

      <!--弹窗，START-->
    <div class="pop_module pop_module01 hide">
        <div class="pop_wrap "> 
            <div class="pa close change"></div>
            <div id="divDrawText" class="pop_txt">
            	恭喜您获得<br /><span id="popPrizeTitle" class="col_b34"></span>！
            </div>
            <ul class="ulTag pop_link">
            	<li class="pop_link01">
            		<a href="./ssoto.do?type=1&flag=2&sid=${sid}">查看奖品</a>
            	</li>
            	<li class="pop_link02">
            		<a href="./ssoto.do?type=2&flag=5&sid=${sid}">看看其他邮箱活动</a>
            	</li>
            </ul>
        </div>
    </div>
    <div class="pop_module pop_module02 hide ">
        <div class="pop_wrap02 ">
            <div class="pa close change"></div>
            <div id="divFailText" class="pop_txt">很遗憾您未中奖！</div>
            <ul class="ulTag pop_link">
            	<li class="pop_link01"><a href="./index.do?sid=${sid }">再玩一次</a></li>
            	<li class="pop_link02"><a href="./ssoto.do?type=2&flag=5&sid=${sid}">看看其他邮箱活动</a></li>
            </ul>
        </div>
    </div> 
    
<!--      <div class="pop_module pop_module02 hide">
        <div class="pop_wrap03">
            <div class="pa close change"></div>
            <div class="pop_txt">
                很遗憾您未中奖！
            </div>
            <ul class="ulTag pop_link"><li class="pop_link01"><a href="#">查看奖品</a></li><li class="pop_link02"><a href="#">看看其他邮箱活动</a></li></ul>
        </div>
    </div> -->
    
    <div class="pop_module pop_module03 hide">
        <div class="pop_wrap04">
            <div class="pa close change"></div>
            <div class="pop_txt" style="padding:40px 0 0;">
                恭喜您获得<br/>
                <b style="color:#b34045;">杂志大礼包</b>一份！
            </div>
            <div class="txt01">
                杂志大礼包内含：IT时代周刊、凤凰生活、商业价值、<br/>
                中国国家旅游4本杂志，杂志阅读有效期为1个月
            </div>
            <ul class="ulTag pop_link"><li class="pop_link01"><a href="http://mail.10086.cn/login/sso.aspx?id=dingyuezhongxin_39428&sid=${sid}">查看杂志</a></li><li class="pop_link02"><a href="./ssoto.do?type=2&flag=5&sid=${sid}">看看其他邮箱活动</a></li></ul>
        </div>
    </div>
    <!--弹窗，START-->
    <div class="foot" style="font-size: 14px; color: white; background: #454444; line-height: 30px; text-align: center; padding: 20px 0;">
        <p align="center">
            <img src="images/topV3_icon2.png" alt="" width="138" height="63">
        </p> 
        京ICP备05002571号 | 中国移动通信版权所有 | 客服热线:10086
    </div> 
    <input id="curSid" type="hidden" value="${sid }" />
</body> 
 
<script type="text/javascript" src="javascript/yzkf.js"></script>  
<script src="javascript/index.js"></script>
<script type="text/javascript" src="javascript/module_top.js"></script>

<script type="text/javascript"> 
	var rand = parseInt(Math.random() * 5 + 1);
	var nick = $("#divNickName").html();
    $(function () {
    	var isSSO = $("#divIsSSO").val();
    	if(isSSO == "1"){
    		$("#module_header").hide();
    	}
        var dateObj = new Date();
        var week = dateObj.getDay();
        switch (week) {
            case 0: week = "星期日"; break;
            case 1: week = "星期一"; break;
            case 2: week = "星期二"; break;
            case 3: week = "星期三"; break;
            case 4: week = "星期四"; break;
            case 5: week = "星期五"; break;
            case 6: week = "星期六"; break;
        }
        var day = dateObj.getDate(); 
        var year = dateObj.getFullYear();
        var month = dateObj.getMonth()+1; 
        if (month < 10) {
            month = "0" + month;
        }
        var $date = $("#page1 .page1-header .page1-date");
        $date.find("div:eq(0)").html(year + "/" + month);
        $date.find("div:eq(1)").html(day);
        $date.find("div:eq(2)").html(week);
        //查询是否已订阅杂志
        var magazineIds = [38601,38553,39378];
        
        //请先登录
        $("#divNickName").click(function () {
            if($(this).text()=="" || $(this).text()=="请先登录"){
            	showLoginBox();
            }
        });

        //关闭
        $(".pop_module .close").click(function () {
            $(this).parents(".pop_module").fadeOut();
        });
        //抽奖
        $(".page2-getGiftBtn").click(function () {
        	if(isLogin){
        		//进行抽奖
        		$.post("draw.do?ran="+Math.random(), {
				}, function(result) {
					var data = eval("("+result+")");
					if(data.result == 99){
						//未登陆
						showLoginBox();
						return false;
					}
					if(data.result == -1){
						$("#divFailText").html("抽奖机会不足");
						$(".pop_module02").fadeIn();
					}else{
						if(data.result == 1){
							//中奖
							if(data.win.code=="CN201602A1003"){
								$(".pop_module03").fadeIn();
								$.post("subscribe.do?ran="+Math.random(),{
									magzineid: 39428
								},function(jsonData){});
								$.post("subscribe.do?ran="+Math.random(),{
									magzineid: 39429
								},function(jsonData){});
								$.post("subscribe.do?ran="+Math.random(),{
									magzineid: 39430
								},function(jsonData){});
								$.post("subscribe.do?ran="+Math.random(),{
									magzineid: 39433
								},function(jsonData){});
							}else{
								$("#popPrizeTitle").html(data.win.title);
								$(".pop_module01").fadeIn();
							}
						}else{
							//不中奖
							$(".pop_module02").fadeIn();
						}
						var left = parseInt($("#divDrawChance").html()) - 1;
						if(left < 0){
							left = 0;
						}
						$("#divDrawChance").html(left);
					}
	        		return;
				});
        	}else{
        		showLoginBox();
        	}
            
        });
        //打开页面2
        $(".page1-module01 .module01-btn").click(function () {
            if(isLogin){
            	$("#page1").hide();
                $("#page2").fadeIn();
                
                var news = ".page2-news" + rand;
                
                var newsContent = $(news).html();
                newsContent = newsContent.replace(/#nick#/g,nick);
                $(news).html(newsContent);
                $(news).show();
                addChance(1);//生成头条获得抽奖机会
            }else{
            	showLoginBox();
            }
        	
        });
      //滚动
        function Turn() {
            var $order = $(".module-order");
            var $item = $order.find(".module-order-item");
            var T = null;
            var $this = this;
            var $ul = $order.find(".module-order-point");
            var $li = $ul.find("li");
            var len = $ul.find("li").length;


            var setPointStyle = function ($obj) {

                $obj.addClass("cur").siblings().removeClass("cur");
            }
            var setImg = function (index) {

                $item.eq(index).css("display", "block");
                $item.not($item.eq(index)).css("display", "none");

            }

            setImg(0);//初始化

            $this.hover = function () {
                $li.hover(function () {
                    //清空计时器
                    clearInterval(T);

                    var index = $(this).index();
                    //设置点的样式
                    setPointStyle($(this));


                    //切换图片
                    setImg(index);


                }, function () {
                    $this.autoTurn();
                });
            }
            $this.autoTurn = function () {
                T = setInterval(function () {
                    var index = ($order.find(".cur").index() + 1) % len;

                    //设置点的样式
                    setPointStyle($order.find("li:eq(" + index + ")"));

                    //切换图片
                    setImg(index);

                }, 3000);
            }
        }
        var turnObj = new Turn();
        turnObj.hover();
        turnObj.autoTurn();

    });
    var newsContent_title = $('.page2-news.page2-news' + rand + ' div.p28.news-title').text();
    newsContent_title = newsContent_title.replace(/#nick#/g,nick);
	if(window.location.port == "8001"){
		p_url = "http://happy.mail.10086.cn:8001/jsp/wap/cn/headlines/index.do?nid=" + rand + "&fid=" + nick + "&pagetitle=" + encodeURIComponent(newsContent_title);
	}else{
		p_url = "http://happy.mail.10086.cn/jsp/wap/cn/headlines/index.do?nid=" + rand + "&fid=" + nick + "&pagetitle=" + encodeURIComponent(newsContent_title);
	}
    var jiathis_config = {
    	    url:p_url 
    }
</script>
<script type="text/javascript" src="http://v3.jiathis.com/code/jia.js?uid=1" charset="utf-8"></script>
<script src="https://happy.mail.10086.cn/api/ipcount/website.aspx?projectid=CN201602A1"></script>	
</html>
