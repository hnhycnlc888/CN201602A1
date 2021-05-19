package cn.richinfo.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.richinfo.core.utils.web.HttpClient;
import cn.richinfo.core.utils.web.WebUtils;
import cn.richinfo.dao.SubDao;
import cn.richinfo.login.pojo.UserInfo;
import cn.richinfo.pojo.SubscribeModel;
import yzkf.config.ProjectConfig;

@Service
public class SubService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private SubDao subDao;
	
	/**
	 * 订阅杂志
	 * 
	 * @param request
	 * @param columnId
	 *            杂志ID
	 * @param from
	 *            来源，WEB或WAP
	 * @return
	 */
	public boolean subscribe(UserInfo user,HttpServletRequest request, String columnId, String from, int subType) {
		if (user == null) {
			return false;
		}
		String channel = ProjectConfig.getInstance().getAppSetting("Api/channel");
		String comeFrom = ProjectConfig.getInstance().getAppSetting("Api/comeFrom");

		String jsonString = "{\"Message\":{\"header\":{\"verify\":\"\",\"clientId\":2004,\"operatorId\":2,\"operateTime\":\"2015-07-14\"},\"body\":{\"columnId\":"
				+ columnId
				+ ",\"userNumber\":\"86"
				+ user.getUserNumber()
				+ "\",\"channel\":"
				+ channel + ",\"comeFrom\":" + comeFrom + "}}}";

		Map<String, String> requestHeaders = new HashMap<String, String>();
		String encoding = ProjectConfig.getInstance().getAppSetting("Api/encoding");
		String url = ProjectConfig.getInstance().getAppSetting("Api/url");
		requestHeaders.put("Content-Type", "application/json");
		String out = "";
		try {
			out = HttpClient.send(url, jsonString, encoding, true, requestHeaders);
		} catch (IOException e) {
			logger.error("订阅接口异常", e);
			return false;
		}
		try {
			JSONObject outJson = new JSONObject();
			JSONObject Message = outJson.getJSONObject("Message");
			JSONObject header = Message.getJSONObject("header");
			String retcode = header.getString("resultCode");
			String description = header.getString("description");
			if (subType == 1) {
				if (retcode.equals("0")) {
					SubscribeModel model = new SubscribeModel();
					model.setUsermobile(user.getUserNumber());
					model.setProvcode(user.getProvCode());
					model.setAreacode(user.getAreaCode());
					model.setLoginId(user.getLoginid());
					model.setIpaddress(WebUtils.getClientIP(request));
					model.setComefrom(from);
					model.setMagid(Integer.parseInt(columnId));
					int subResult = subDao.subscribe(model);
					if (subResult == 1)
						return true;
				}
				logger.warn("订阅接口返回错误信息" + retcode + "++++++++++++信息:" + description);
			}

		} catch (Exception e) {
			logger.error("记录订阅信息异常", e);
		}
		return false;
	}
}