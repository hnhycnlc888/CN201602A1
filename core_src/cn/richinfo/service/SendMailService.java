package cn.richinfo.service;

import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import yzkf.app.ActiveLog;
import yzkf.config.ProjectConfig;
import yzkf.enums.ActiveFunction;
import yzkf.enums.ActiveOperation;
import cn.richinfo.login.pojo.UserInfo;
import org.apache.log4j.Logger;

import cn.richinfo.core.utils.web.WebUtils;
import cn.richinfo.pojo.SendMailModel;

@Service
public class SendMailService {
	private Logger logger = Logger.getLogger(SendMailModel.class);
	
	public boolean sendservice(SendMailModel model, HttpServletRequest request, UserInfo user,
			String receiver, String subject, String mailpath) throws SQLException {
		boolean result = true;
		try {
			HashMap<String, Object> hs = new HashMap<String, Object>();
			String sender = ProjectConfig.getInstance().getAppSetting("Draw/Sender");
			String mailPath = mailpath;
			String appendText = subject;
			hs.put("$receiver$", receiver);
			hs.put("$receiveurl$", ProjectConfig.getInstance().getAppSetting("Draw/ReceiveUrl"));
			hs.put("$prizename$", model.getPrizename());

			SendMail send = new SendMail();
			boolean isSend = send.sendMail(request, sender, model.getUsermobile(),
					"中国移动139邮箱", subject, appendText, mailPath, hs, "CN201602A1_DRAWMAIL");
			if (!isSend) {
				result = false;
			}
			ActiveLog.getInstance().WriteBehaviorLog("CN201509A1", user.getUserNumber(),
					user.getProvCode(), user.getAreaCode(), WebUtils.getClientIP(request),
					ActiveFunction.SendMail, ActiveOperation.LotterySuccess, "", "");
		} catch (Exception e) {
			logger.error("发送中奖邮件异常-sendMailservice", e);
			result = false;
		}
		return result;
	}
}
