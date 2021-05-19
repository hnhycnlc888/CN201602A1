package cn.richinfo.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yzkf.config.ProjectConfig;
import yzkf.utils.Xml;
import cn.richinfo.core.utils.MD5;
import cn.richinfo.core.utils.web.HttpClient;
import cn.richinfo.login.DesService;
import cn.richinfo.login.impl.Login;
import cn.richinfo.login.pojo.UserInfo;
import cn.richinfo.pojo.SubscribeModel;

@Service
public class ApiService {
	
	@Resource
	SubscribeService subscribeService;
	
	@Autowired
	private Login login;

	private static String clientId = ProjectConfig.getInstance().getAppSetting(
			"QuerySubApi/clientId");
	private static String operatorId = ProjectConfig.getInstance().getAppSetting(
			"QuerySubApi/operatorId");
	private static String headString = "{\"Message\":{\"header\":{\"verify\":\"\",\"clientId\":\""
			+ clientId + "\",\"operatorId\":\"" + operatorId + "\",\"operateTime\":\"" + getDate()
			+ "\"},%s}}";
	private static Logger logger = LoggerFactory.getLogger(ApiService.class);

	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateNowStr = sdf.format(new Date());
		return dateNowStr;
	}

	public static String sso(String usermobile) {
		String url = ProjectConfig.getInstance().getAppSetting("SsoApi/url");
		String clientId = ProjectConfig.getInstance().getAppSetting("SsoApi/clientId");
		String key = ProjectConfig.getInstance().getAppSetting("SsoApi/key");
		String msisdn = "";
		try {
			msisdn = DesService.encrypt(usermobile);

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			Date old = format.parse("2000-1-1 00-00-00");
			Date now = new Date();
			String timeStamp = String.valueOf((now.getTime() - old.getTime()) / 1000 + 1000);

			String skey = MD5.encode(clientId + msisdn + timeStamp + key);
			url = url + "?ClientID=" + clientId + "&MSISDN=" + msisdn + "&TimeStamp=" + timeStamp
					+ "&Skey=" + skey;
		} catch (Exception e1) {
			logger.error("生成链接操作异常apiservice", e1);
			return "0";
		}
		String out = "";
		String result = "0";
		Map<String, String> requestHeaders = new HashMap<String, String>();
		String encoding = ProjectConfig.getInstance().getAppSetting("Api/encoding");
		requestHeaders.put("Content-Type", "application/test");
		try {
			logger.error("打印请求URL地址：--------" + url);
			out = HttpClient.send(url, "", encoding, false, requestHeaders);
			// out = "Result:0\r\n SSOSID:08097b73-b428-4bcc-9cfe-7024646e9a23";
			if (out.indexOf("Result:0") > -1) {
				String[] arrResult = out.split(":");
				result = arrResult[arrResult.length - 1];
			}
		} catch (IOException e) {
			logger.error("单点接口异常apiservice", e);
			return "0";
		}
		return result;
	}
	
	/**
	 * 订阅栏目
	 * @param request
	 * @param response
	 * @param columnid
	 * @return
	 */
	private String subColumnParam = "{\"columnId\": %s,\"comeFrom\": \"503\"}";
	private String subColumnUrl = ProjectConfig.getInstance().getAppSetting("SubApi/subUrl");
	public boolean subColumnApi(HttpServletRequest request, HttpServletResponse response,
			String columnid){
		try {

			UserInfo user = login.getUserInfo(request, response);
			if (user != null) {
				String cookie = "RMKEY=" + user.getRmkey() + ";";
				String sid = user.getSid();

				Map<String, String> headersMap = new HashMap<String, String>();
				headersMap.put("Cookie", cookie);
				logger.info("用户"+user.getUserNumber()+"订阅杂志ID:"+columnid+";发送链接为："+subColumnUrl + sid+";cookies是"+headersMap);
				
				String out = HttpClient.send(subColumnUrl + sid,
						String.format(subColumnParam, columnid), null, true, headersMap);
				// 获取结果JSON格式
				// {"body":{"couponInfo":null,"integralInfo":null,"returnCode":10,"wabpInfo":null},"description":"操作成功","resultCode":"0"}
				logger.info("用户"+user.getUserNumber()+"订阅资讯ID:"+columnid+";结果为："+out);
				
				JSONObject outJson = new JSONObject(out);
				int returnCode = 0;
				String resultCode = "";
				if (!outJson.isNull("body")) {
					JSONObject body = outJson.getJSONObject("body");
					returnCode = body.getInt("returnCode");
					resultCode = outJson.getString("resultCode");
				}
				if (returnCode == 10 && resultCode.equals("0")) {
					//记录订阅日志
					SubscribeModel smodel = new SubscribeModel();
					smodel.setUsermobile(user.getUserNumber());
					smodel.setProvcode(user.getProvCode());
					smodel.setAreacode(user.getAreaCode());
					smodel.setLoginId(user.getLoginid());
					smodel.setComefrom("WEB");
					smodel.setMagid(Integer.parseInt(columnid));
					subscribeService.subscribe(smodel);
					return true;
				} else {
					logger.error("SUBCOLUMNAPI_ERROE:[" + user.getUserNumber() + "]" + out);
					return false;
				}
			}
		} catch (Exception e) {
			logger.error("接口异常subColumnApi", e);
		}
		return false;
	}

	private String authurl = ProjectConfig.getInstance().getAppSetting("SubApi/authurl");

	/**
	 * 设置用户的SID和保存邮箱SID会话 退出必须清空
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> setRmkeyAndSid(HttpServletRequest request,
			HttpServletResponse response) {

		Map<String, String> outMap = new HashMap<String, String>();

		UserInfo user = login.getUserInfo(request, response);
		if (user != null) {
			try {

				// 获取是否会话
				Object objMap = request.getSession(true).getAttribute(
						"CN201509A1_user_rmkeyandsid" + request.getSession(true).getId());

				if (objMap != null) {
					return (Map<String, String>) objMap;
				}

				// 请求SSOID
				String ssoid = ApiService.sso(user.getUserNumber());
				// 获取邮箱的SID和RMKEY
				String geturl = authurl + "?Mobile_No=%s&SSOID=%s";
				String out = HttpClient.get(String.format(geturl, user.getUserNumber(),
						ssoid));

				// 返回XML
				// 例如：<?xml version="1.0"
				// encoding="UTF-8"?><responsedata><account>15014172051</account><returncode>000</returncode><realSid>MTQ0NDI5NzE2NjAwMjExMTU405152489000001</realSid><mailSid>2717741444297166</mailSid><partId>21</partId></responsedata>
				Xml outXml = Xml.parseXml(out);
				// 返回RMKEY保存到Session
				String rmkey = outXml.evaluate("/responsedata/mailSid");
				// 返回SID保存到Session
				String sid = outXml.evaluate("/responsedata/realSid");
				outMap.put("rmkey", rmkey);
				outMap.put("sid", sid);

				// 保存Session
				request.getSession(true).setAttribute(
						"CN201509A1_user_rmkeyandsid" + request.getSession(true).getId(), outMap);

			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				logger.error("接口异常setRmkeyAndSid", e);
			}
		}
		return outMap;
	}

	/**
	 * 移除rmkey和sid
	 * 
	 * @param request
	 */
	public static void removeRmkeyAndSid(HttpServletRequest request) {
		String rmkeyandsidCache = "CN201509A1_user_rmkeyandsid" + request.getSession().getId();
		request.getSession().removeAttribute(rmkeyandsidCache);
	}
	
	/*
	 * public static void main(String[] args) { String result =
	 * sso("13760869714"); System.out.println(result); try { String
	 * Message=URLEncoder.encode("id=dingyuezhongxin_39367", "utf-8");
	 * System.out.println(Message); } catch (UnsupportedEncodingException e) {
	 * e.printStackTrace(); } }
	 */

	/**
	 * 获取用户userid
	 * @param userMobile手机号码
	 * @return
	 */
	public static int getUserId(String userMobile) {
		String doMainString = ProjectConfig.getInstance().getAppSetting("QuerySubApi/RecDomain");
		String bodyString = "\"body\":{\"userEmail\":\"" + userMobile + doMainString
				+ "\",\"containsExtra\":false}";
		String jsonString = String.format(headString, bodyString);

		Map<String, String> requestHeaders = new HashMap<String, String>();
		String encoding = ProjectConfig.getInstance().getAppSetting("Api/encoding");
		String url = ProjectConfig.getInstance().getAppSetting("QuerySubApi/userIdUrl");
		requestHeaders.put("Content-Type", "application/json");
		String out = "";
		try {
			out = HttpClient.send(url, jsonString, encoding, true, requestHeaders);
		} catch (IOException e) {
			logger.error("查询用户userid接口异常", e);
			return 0;
		}
		try {
			JSONObject outJson = new JSONObject(out);
			JSONObject Message = outJson.getJSONObject("Message");
			JSONObject header = Message.getJSONObject("header");
			String retcode = header.getString("resultCode");
			JSONObject body = Message.getJSONObject("body");
			int userid = body.getInt("userId");
			if (retcode.equals("0")) {
				return userid;
			}
			logger.info("查询用户userid接口返回错误信息" + retcode + "++++++++++++信息:");
		} catch (Exception e) {
			logger.error("处理用户userid接口异常", e);
		}
		return 0;
	}

	/**
	 * 获取用户已订阅杂志id列表
	 * 
	 * @param userMobile手机号码
	 * @return
	 */
	public List<Object> querySub(String userMobile) {
		List<Object> list = new ArrayList<Object>();
		int userId = getUserId(userMobile);
		if (userId == 0) {
			return list;
		}
		String bodyString = "";
		String jsonString = "";
		Map<String, String> requestHeaders = new HashMap<String, String>();
		String encoding = ProjectConfig.getInstance().getAppSetting("Api/encoding");
		String url = ProjectConfig.getInstance().getAppSetting("QuerySubApi/querySubUrl");
		requestHeaders.put("Content-Type", "application/json");
		String out = "";
		try {
			for (int i = 1; i < 9999; i++) {
				bodyString = "\"body\":{\"userId\":" + userId + ",\"paging\":{\"currentPageNum\":"
						+ i + ",\"perPageCount\":100}}";
				jsonString = String.format(headString, bodyString);
				out = HttpClient.send(url, jsonString, encoding, true, requestHeaders);
				JSONObject outJson = new JSONObject(out);
				JSONObject Message = outJson.getJSONObject("Message");
				JSONObject header = Message.getJSONObject("header");
				String retcode = header.getString("resultCode");
				if (retcode.equals("0")) {
					JSONObject body = Message.getJSONObject("body");
					//JSONObject itemsJsonObject = body.getJSONObject("items");
					JSONObject paging = body.getJSONObject("paging");
					// 获取记录总页码数
					int totalPageNum = paging.getInt("totalPageNum");
					JSONArray items = body.getJSONArray("items");
					// 遍历jsonArray
					for (int j = 0; j < items.length(); j++) {
						// 获取每一个json对象
						JSONObject jsonItem = items.getJSONObject(j);
						// 获取每一个json对象的值
						Map<String, Object> itemMap = new HashMap<String, Object>();
						itemMap.put("magid", jsonItem.getInt("columnId"));
						list.add(itemMap);
					}
					if (totalPageNum <= i)
						break;
				} else {
					logger.warn("订阅接口返回错误信息" + retcode + "++++++++++++信息:");
				}
			}
			return list;
		} catch (Exception e) {
			logger.error("查询已订阅杂志接口异常", e);
			return list;
		}

	}

}
