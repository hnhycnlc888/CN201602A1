package cn.richinfo.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.alibaba.fastjson.JSONObject;

import cn.richinfo.core.action.BaseAction;
import cn.richinfo.login.impl.Login;
import cn.richinfo.login.pojo.UserInfo;
import cn.richinfo.pojo.SubscribeModel;
import cn.richinfo.service.ApiService;

@Controller
public class SubscribeController {
	@Resource
	ApiService apiService;

	@Autowired
	private Login login;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = "/subscribe", method = RequestMethod.POST)
	public void subscribe(HttpServletRequest request, HttpServletResponse response) {
		UserInfo user = login.getUserInfo(request,response);
		JSONObject json = new JSONObject();
		String magId = request.getParameter("magzineid");
		List<SubscribeModel> list = null;
		try {
			if (user == null) {
				json.put("result",-100);
				json.put("desc","请登录后再试！");
				BaseAction.printJson(response, json);
				return;
			}
			if (magId == null || magId.isEmpty()) {
				json.put("result",-1);
				json.put("desc","请选择杂志！");
				BaseAction.printJson(response, json);
				return;
			}
			boolean subResult = apiService.subColumnApi(request, response, magId);
			if (subResult) {
				json.put("result",1);
				json.put("desc","订阅成功！");
				BaseAction.printJson(response, json);
				return;
			}else{
				json.put("result",0);
				json.put("desc","订阅失败！");
			}	
		} catch (Exception e) {
			logger.error("订阅杂志异常subscribecontroller-", e);
			json.put("result",-99);
		}
		BaseAction.printJson(response, json);
	}
}
