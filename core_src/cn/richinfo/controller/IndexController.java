package cn.richinfo.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import cn.richinfo.login.pojo.Result;
import cn.richinfo.login.pojo.UserInfo;
import cn.richinfo.pojo.Device;
import cn.richinfo.pojo.User;
import cn.richinfo.service.DrawService;
import cn.richinfo.service.SsologinService;
import cn.richinfo.service.SubscribeService;
import yzkf.api.Mail;
import yzkf.api.result.MailResult;
import yzkf.config.ProjectConfig;

import cn.richinfo.core.action.BaseAction;
import cn.richinfo.core.utils.web.WebUtils;
import cn.richinfo.login.impl.Login;

@Controller
public class IndexController{
	
	@Autowired
	private Login login;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private DrawService drawService;
	@Resource
	private SubscribeService subscribeService;
	@Resource
	private SsologinService ssologinService;
	
	//springmvc post 对象，application/x-www-form-urlencoded 后端接收
	@RequestMapping(value= {"/test1"}, method= {RequestMethod.POST})
	@ResponseBody
	public void test1(@RequestParam(value="username",required=false) String username){
		System.out.println(username);
	}
	
	 //RESTful风格http://localhost:8080/CN201602A1/test3/user/100.do
	@RequestMapping(value= {"/test3/user/{id}"}, method= {RequestMethod.GET})
	//@ResponseBody
	public void test3(HttpServletRequest request,HttpServletResponse response,@PathVariable("id") String id){
		System.out.println("id="+id);
		JSONObject json = new JSONObject();
		json.put("success", "success");
		BaseAction.printJson(response, json);
	}
	
	//springmvc post json字符 application/json 后端接收
	//以Map或者POJO接收，返回httpcode = 415, 需要MappingJacksonHttpMessageConverter转换
	//public void test(@RequestBody Device device){
	//public void test(@RequestBody Map<String, Object> mapJson){
	@RequestMapping(value= {"/test"}, method= {RequestMethod.POST}, produces= {"application/json"})
	@ResponseBody
	public void test(@RequestBody String paramJson){
		System.out.println(paramJson);
		JSONObject b = JSONObject.parseObject(paramJson);
		String sn = (String) b.get("sn");
		System.out.println(sn);
	}
	
	/**
	 * 接收页面请求的JSON数据，并返回JSON格式结果
	 */
	@RequestMapping("/testJson")
	@ResponseBody
	public String testJson(@RequestBody String body) {
		// 打印接收的JSON格式数据
		System.out.println(body);
		// 返回JSON格式的响应
		//return JSONArray.toJSONString(body);
		return "{\"username\":\"123\",\"password\":\"1234324\"}";
	}
	
	@RequestMapping("/download")
	public ResponseEntity<byte[]> fileDownload(HttpServletRequest request,
	                                           String filename) throws Exception{
		// 指定要下载的文件所在路径
	    String path = request.getServletContext().getRealPath("/upload/");
	    // 创建该文件对象
	    File file = new File(path+File.separator+filename);
	    // 设置响应头
	    HttpHeaders headers = new HttpHeaders();
	    // 通知浏览器以下载的方式打开文件
	    headers.setContentDispositionFormData("attachment", 
	    		this.getFilename(request, filename));
	    // 定义以流的形式下载返回文件数据
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	    // 使用Sring MVC框架的ResponseEntity对象封装返回下载数据
	   return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
	                                           headers,HttpStatus.OK);
	}
	/**
	 * 根据浏览器的不同进行编码设置，返回编码后的文件名
	 */
	public String getFilename(HttpServletRequest request,
	                                            String filename) throws Exception { 
	    // IE不同版本User-Agent中出现的关键词
	    String[] IEBrowserKeyWords = {"MSIE", "Trident", "Edge"};  
	    // 获取请求头代理信息
	    String userAgent = request.getHeader("User-Agent");  
	    for (String keyWord : IEBrowserKeyWords) { 
	         if (userAgent.contains(keyWord)) { 
	              //IE内核浏览器，统一为UTF-8编码显示
	              return URLEncoder.encode(filename, "UTF-8");
	         }
	    }  
	    //火狐等其它浏览器统一为ISO-8859-1编码显示
	    return new String(filename.getBytes("UTF-8"),"ISO-8859-1");  
	}
	
	@RequestMapping(value="/index")
	public String index(HttpServletRequest request,HttpServletResponse response){
		try {
			//测试spring整合mybaits连接mysql数据库并操作
			subscribeService.deleteAccountTbl("1");
			int ret = subscribeService.loginAndRegByProcedure("15916146089", "123456789", "127.0.0.1");
			//subscribeService.updateAccounttByProcedure();
			System.out.println(ret);
			List<User> list = subscribeService.getUsersList();
			if (list.size() > 0) {
				for(int i=0; i< list.size();i++){
					System.out.println(list.get(i).getUserMobileCode());
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String nick = "请先登录";
		int chance = 0;
		
		UserInfo userInfo = login.getUserInfo(request, response);
		
		
		if(userInfo == null){
			request.setAttribute("login", false);
		}else{
			request.setAttribute("login", true);
			request.setAttribute("mobile", userInfo.getUserNumber());
			nick = userInfo.getAliase();
			if(nick == null || nick.isEmpty()){
				nick = userInfo.getUserNumber();
			}
			//获取抽奖机会
			chance = drawService.getDrawChance(userInfo.getUserNumber());
			//判断是否从云邮局内嵌页访问
			String sso = request.getParameter("sso");
			if(sso == null){
				sso = "";
			}
			request.setAttribute("sso", sso);
		}
		//获取当前日期及星期
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int w = c.get(Calendar.DAY_OF_WEEK) - 1;
		if(w < 0){
			w = 0;
		}
		request.setAttribute("year", c.get(Calendar.YEAR));
		request.setAttribute("month", (c.get(Calendar.MONTH) + 1));
		request.setAttribute("day", c.get(Calendar.DATE));
		request.setAttribute("week", weekDays[w]);
		request.setAttribute("nick", nick);
		request.setAttribute("chance", chance);
		return "index";
	}
	
	/**
	 * 登录入口
	 * @param request
	 * @param response
	 * @return {"success":1,"desc":""}
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public void login(HttpServletRequest request,HttpServletResponse response) {
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		String verifyCode = request.getParameter("verifycode");
		int success = 0;
		JSONObject json = new JSONObject();
		Result result = login.login(request, response, account, password, verifyCode, "Web");
		if(result.isOK()){
			//登录成功
			success = 1;
		}else{
			//登录失败
			String code = result.getCode();
			String desc = result.getDescr();
			if(code!=null && !"".equals(code)){
				if(code=="S002"){
					//别名或手机号码不存在，请重新输入
					success = -1;
				}else if(code=="S001"){
					//您输入的用户名或密码错误，请重新输入
					success = -1;
				}else if(code=="S9999"){
					//网络异常,请稍后再试
					success = -1;
					desc = "网络异常,请稍后再试";
				}else{
					//验证码异常
					success = -2;
				}
			}else{
				success = -2;
			}
			json.put("desc", desc);
		}
		json.put("success", success);
		BaseAction.printJson(response, json);
	}
	
	/**
	 * 短信方式登录
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/smslogin",method=RequestMethod.POST)
	public void SMSLogin(HttpServletRequest request,HttpServletResponse response){
		String account = request.getParameter("mobile");
		String verifyCode = request.getParameter("verifycode");
		int success = 0;
		JSONObject json = new JSONObject();
		Result result = login.login(request, response, account, verifyCode, "", "SMS");
		if(result.isOK()){
			//登录成功
			success = 1;
		}else{
			String code = result.getCode();
			String desc = result.getDescr();
			if(code!=null && !"".equals(code)){
					if(code=="S002"){
						//手机号码不存在
						success = -1;
						desc = "手机号码不存在";
					}else if(code=="S001"){
						//密码不正确
						success = -2;
						desc = "验证码错误";
					}else if(code=="S9999"){
						success = 0;
						desc = "网络异常,请稍后再试";
					}
			}else{
				//密码不正确
				success = -3;
			}
			json.put("desc",desc);
		}
		json.put("success", success);
		BaseAction.printJson(response, json);
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getmobilecode",method=RequestMethod.POST)
	public void getMobileCode(HttpServletRequest request,HttpServletResponse response){
		String mobile = request.getParameter("mobile");
		JSONObject json = new JSONObject();
		try {
			Mail mail = new Mail();
			MailResult mailResult = mail.getAttribute(mobile);
			if(mailResult.isOK()){
				yzkf.model.UserInfo user = mail.getUserInfo();
				if(user != null){
					//发送短信
					logger.info("CN201602A1:" + new Date() + "开始发送短信:"+mobile);
					Result result = login.getSms(request, mobile);
					if(result.isOK()){
						//发送成功 
						json.put("success", 1);
					}else{
						//发送失败
						String code = result.getCode();
						if(code != null && "PML404010001".equals(code)){
							//超过频次限制，拒绝获取短信验证码
							json.put("success", 3);
						}else{
							json.put("success", 0);
						}
					}
				}else{
					//用户不存在
					json.put("success", 2);
				}
			}else{
				//用户不存在
				json.put("success", 2);
			}
		} catch (Exception e) {
			logger.error("获取手机短信验证码失败:"+e);
		}
		BaseAction.printJson(response, json);
	}
	
	/**
	 * 单点登录入口
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/sso")
	public String sso(HttpServletRequest request,HttpServletResponse response){
		UserInfo userInfo = login.getUserInfo(request,response);
		if(userInfo == null){
			//单点登录失败
			logger.info("单点登录失败,回到主页面...");
			return "redirect:/index.do";
		}else{
			String flag = request.getParameter("flag");
			try {
/*				logger.error(userInfo.getUserNumber());
				logger.error(Integer.toString(userInfo.getProvCode()));
				logger.error(Integer.toString(userInfo.getAreaCode()));
				logger.error(Long.toString(userInfo.getLoginid()));
				logger.error(WebUtils.getClientIP(request));
				logger.error(flag);*/
				ssologinService.ssolog(userInfo, WebUtils.getClientIP(request), Integer.parseInt(flag));
			} catch (SQLException e) {
				logger.error("sso logsave exception! ", e);
				e.printStackTrace();
				return "redirect:/index.do";
			}
			return "redirect:/index.do?sid="+userInfo.getSid();
		}
	}
	
	
	/**
	 * 获取用户信息
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/userinfo",method=RequestMethod.POST)
	public void userInfo(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		int mailNum = 0;
		int mailScore = 0;
		int isLogin = 0;
		String magazineIds = "";
		try{
			UserInfo userInfo = login.getUserInfo(request, response);
			if(userInfo != null){
				isLogin = 1;
				Mail mail = new Mail();
				mail.getMailNum(userInfo.getUserNumber());
				mailNum = mail.getUserInfo().getUnreadMailNum();
				try{
					MailResult result = mail.getIntegralTotal(userInfo.getUserNumber());
					if(result.isOK()){
						yzkf.model.UserInfo mUser = (yzkf.model.UserInfo) result.getValue();
						mailScore = mUser.getIntegralEffect();
					}
				}catch(Exception ex){
					logger.error("获取用户邮箱积分异常..." + ex);
				}
				
				//获取订阅杂志情况
				try{
					List<Integer> list = subscribeService.getSubscribedIds(userInfo.getUserNumber());
					if (list.size() > 0) {
						for(int i=0; i< list.size();i++){
							magazineIds += list.get(i) + "|";
						}
					}
				}catch(Exception ex){
					logger.error("获取用户订阅日志ID异常..." + ex);
				}
			}
		}catch(Exception e){
			logger.error("获取用户信息异常..." + e);
		}
		json.put("islogin", isLogin);
		json.put("mailnum", mailNum);
		json.put("score", mailScore);
		json.put("magids", magazineIds);
		BaseAction.printJson(response, json);
		
	}
	
	/**
	 * 单点登录到邮箱
	 * @param model
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/ssoto", method = RequestMethod.GET)
	public void ssoto(Model model, HttpServletRequest request, HttpServletResponse response) {
		UserInfo user = login.getUserInfo(request,response);
		String jumpToMail = ProjectConfig.getInstance().getAppSetting("ssotoEmail");
		String jumpToProject = ProjectConfig.getInstance().getAppSetting("ssotoActivity");
		String jumpToMagazine = ProjectConfig.getInstance().getAppSetting("ssoToMagazine");
		String type = "";
		String flag = "";
		
		try {
			type = request.getParameter("type");
			flag = request.getParameter("flag");
			
			if (type.equals("1")) {
				logger.info(user.getUserNumber()+"开始单点到邮箱");
				if (user != null) {
					if(flag=="1"){
							//邮箱首页
							jumpToMail = String.format(jumpToMail, "welcome",user.getSid());
					}else if(flag=="2"){
							//邮件
							jumpToMail = String.format(jumpToMail, "welcome",user.getSid());
					}
					logger.info(user.getUserNumber()+"获取单点地址成功"+jumpToMail);
				}
				response.sendRedirect(jumpToMail);
			} else if (type.equals("2")) {
				if (user == null) {
					logger.info("开始单点到用户中心,用户未登录");
					jumpToProject = "https://happy.mail.10086.cn/club/";
					if(flag=="10"){
						jumpToProject = "https://happy.mail.10086.cn/jsp/web/cn/yxjc/index";
					}
				}else{
					if(flag=="5"){
						jumpToProject = String.format(jumpToProject, "CN201204A1",user.getSid());
						logger.info(user.getUserNumber()+"开始单点到用户中心,单点地址为"+jumpToProject);
						}else if(flag=="6"){
						jumpToProject = String.format(jumpToProject, "CN201512A1",user.getSid());
						logger.info(user.getUserNumber()+"开始单点到邮厅活动,单点地址为"+jumpToProject);
						}else if(flag=="7"){
						jumpToProject = String.format(jumpToProject, "CN201504A1",user.getSid());
						logger.info(user.getUserNumber()+"开始单点到邮爱区,单点地址为"+jumpToProject);
						}else if(flag=="8"){
						jumpToProject = String.format(jumpToProject, "CN201504C1",user.getSid());
						logger.info(user.getUserNumber()+"开始单点到活动区,单点地址为"+jumpToProject);
						}else if(flag=="10"){
						jumpToProject = String.format(jumpToProject, "CN201511D1",user.getSid());
						logger.info(user.getUserNumber()+"开始单点到活动区,单点地址为"+jumpToProject);
						}
					response.sendRedirect(jumpToProject);
				}
			} else if(type.equals("3")){
				if (user != null) {
					logger.info("开始单点到云邮局");
					//单点到收费杂志
					String mgzid = request.getParameter("mid");
					jumpToMagazine = String.format(jumpToMagazine, mgzid,user.getSid());
					logger.info(user.getUserNumber()+"获取云邮局单点地址成功"+jumpToMagazine);
				}
				
				response.sendRedirect(jumpToMagazine);
			}
		} catch (Exception e) {
			logger.info("单点到邮箱或用户中心出现异常："+e);
		}
	}
	
	/**
	 * 单点登录入口
	 * 活动到本活动的单点
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/asso")
	public String activitySSO(HttpServletRequest request,HttpServletResponse response){
		yzkf.model.UserInfo user = null;
		yzkf.api.result.Result result = null;
		Map<String, Object> userMap = new HashMap<String, Object>();
		try {
			result = yzkf.com.Login.getInstance().sso(request);
			user =  (yzkf.model.UserInfo) result.getValue();
			userMap = login.getSid(user.getMobile());
			login.logout(request,response);
			login.getUserInfo(request,response, userMap.get("sid").toString(), userMap.get("rmkey").toString());
		} catch (Exception e) {
			logger.error("sso exception! ", e);
		}
		return "redirect:/index.do?sid="+userMap.get("sid");
	}
	
	/**
	 * 退出
	 * @param request
	 * @param response
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response){
		login.logout(request, response);
		return "redirect:/index.do";
	}
}