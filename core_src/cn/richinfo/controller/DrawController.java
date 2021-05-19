package cn.richinfo.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;

import cn.richinfo.core.action.BaseAction;
import cn.richinfo.core.utils.web.WebUtils;
import cn.richinfo.login.impl.Login;
import cn.richinfo.login.pojo.UserInfo;
import cn.richinfo.pojo.SendMailModel;
import cn.richinfo.pojo.WinResult;
import cn.richinfo.service.DrawService;
import cn.richinfo.service.SendMailService;
import yzkf.app.ActiveLog;
import yzkf.config.ProjectConfig;
import yzkf.enums.ActiveFunction;
import yzkf.enums.ActiveOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class DrawController {
	
	@Resource
	Login login;
	
	@Resource
	private DrawService drawService;
	@Resource
	SendMailService sendMailService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 开始抽奖
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="draw",method=RequestMethod.POST)
	public void draw(HttpServletRequest request,HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try{
			//判断是否登录
			UserInfo userInfo = login.getUserInfo(request, response);
			if(userInfo == null){
				json.put("result", 99);
				BaseAction.printJson(response, json);
				return;
			}
			//进行抽奖
			String clientIp = WebUtils.getClientIP(request);
			Map<String,Object> map = drawService.draw(userInfo, clientIp);
			int result = Integer.parseInt(map.get("result").toString());
			logger.info("draw result:" + result);
			WinResult winResult = new WinResult();
			winResult = (WinResult) map.get("win");
			if(result == -1){
				//抽奖机会不足
				json.put("result", -1);
				BaseAction.printJson(response, json);
			}else if(result==0){
				try{
					//中奖下发中奖邮件
					String mailPath = winResult.getMailTemp();
					SendMailModel mailModel = new SendMailModel();
					mailModel.setUsermobile(userInfo.getUserNumber());
					mailModel.setProvcode(userInfo.getProvCode());
					mailModel.setAreacode(userInfo.getAreaCode());
					mailModel.setComefrom("WEB");
					mailModel.setIpaddress(WebUtils.getClientIP(request));
					mailModel.setPrizename(winResult.getTitle());
					String subject = String.format(
							ProjectConfig.getInstance().getAppSetting("Draw/Subject"),
							winResult.getTitle());
					//发送邮件并保存记录
					if(!winResult.getCode().equals("CN201602A1003")){
						boolean isSend = sendMailService.sendservice(mailModel, request, userInfo, userInfo.getUserNumber(),subject, mailPath);
						if (!isSend) {
							logger.error("下发中奖邮件异常：中奖号码为" + userInfo.getUserNumber());
						}
					}
				}catch(Exception e){
					logger.info("中奖邮件发送失败,"+e);
				}
				ActiveLog.getInstance().WriteBehaviorLog("CN201602A1", userInfo.getUserNumber(),
						userInfo.getProvCode(), userInfo.getAreaCode(), WebUtils.getClientIP(request),
						ActiveFunction.Nothing, ActiveOperation.LotterySuccess, "", "");
				json.put("result", 1);
				json.put("win", winResult);
				BaseAction.printJson(response, json);
			}else{
				//未中奖
				json.put("result", 0);
				BaseAction.printJson(response, json);
			}
			//判断是否中奖
		}catch(Exception e){
			logger.error("抽奖异常 " + e);
			json.put("result", -100);
			BaseAction.printJson(response, json);
		}
		
	}
	
	/**
	 * 增加抽奖机会次数
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="addchance",method=RequestMethod.POST)
	public void addChance(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		//获得机会来源，1：分享头条新闻，杂志ID：订阅杂志
		int chanceFrom = Integer.parseInt(request.getParameter("from"));
		try{
			//判断是否登录
			UserInfo userInfo = login.getUserInfo(request, response);
			if(userInfo == null){
				json.put("result", -100);
				BaseAction.printJson(response, json);
				return;
			}
			int result = drawService.addChance(userInfo.getUserNumber(), chanceFrom);
			if(result > 0){
				json.put("result", 1);
			}else{
				json.put("result", 0);
			}
			BaseAction.printJson(response, json);
			return;
		}catch(Exception e){
			logger.error("添加抽奖机会异常 " + e);
			json.put("result", -100);
			BaseAction.printJson(response, json);
			return;
		}
	}
}