package cn.richinfo.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import yzkf.app.Mail;
import yzkf.config.ConfigFactory;
import yzkf.config.MailConfig;
import yzkf.config.ProjectConfig;
import yzkf.exception.MailException;
import yzkf.exception.ParserConfigException;
import yzkf.utils.LocalCache;

public class SendMail {
	/**
	 * 发邮件方法
	 * 
	 * @param UserMobile
	 *            发送人手机号码
	 * @param RecMobile
	 *            接收人手机号码
	 * @param Fromname
	 *            发送人名称
	 * @param Subject
	 *            邮件主题
	 * @param MailDemo
	 *            邮件模板地址
	 * @param hs
	 *            需要替换的模板中的键值对
	 * @param CacheName
	 *            邮件模板缓存名称
	 * @return
	 * @throws ParserConfigException
	 * @throws IOException
	 * @throws MailException
	 */
	public boolean sendMail(HttpServletRequest request, String UserMobile, String RecMobile,
			String Fromname, String Subject, String AppendText, String MailDemo,
			HashMap<String, Object> hs, String CacheName) throws ParserConfigException,
			IOException, MailException {

		ConfigFactory factory = ConfigFactory.getInstance();
		MailConfig config = factory.newMailConfig();
		String doMain = ProjectConfig.getInstance().getAppSetting("Draw/RecDomain");
		// 使用config配置对象 创建 计算活跃 的邮件对象
		Mail mail = new Mail(true, config);
		// 设置发件人
		mail.setFrom(UserMobile + doMain);
		// 设置发件人名称
		mail.setFromname(Fromname);
		// 设置收件人，多个收件人逗号隔开
		mail.setTo(RecMobile + doMain);
		// 设置抄送
		// mail.setCc("13987654321@139.com,alias2@139.com");
		// 设置密送
		// mail.setBcc("13912341234@139.com,alias3@139.com");
		mail.setSubject(Subject);// 设置邮件标题
		// 设置html正文

		String ct = buildMail(request, UserMobile, hs, MailDemo, CacheName);
//		String ct = "1111111111";
		mail.setHtml(ct);
		// mail.setText(text);
		// mail.setHtml("<html><head><title>Hello 139 Mail!</title></head><body><a href=\" http://mail.10086.cn?id=calendar\" id=\"139Command_LinksShow\" rel=\"calendar\" params=\"addnew=6\" target=\"_blank\">生日提醒</a></body></html>");
		// 设置wap简版正文，注意：仅限139邮箱，如果发送外域，请不要同时设置 setHtml 和 setText，外遇邮箱将同时显示2种内容
		mail.setText(AppendText);
		// 增加附件
		// mail.addAttachments("E:\\attachment1.txt");
		// 增加多个附件
		// mail.addAttachments("E:\\attachment2.txt");
		// 发送邮件
		mail.Send();
		return true;
	}

	// 创建邮件
	protected String buildMail(HttpServletRequest request, String UserNumber,
			HashMap<String, Object> hs, String mailDemo, String cachename) {
		String mailBody = "";
		String mailPath = "";
		String htmlPath = "";
		/*
		 * LocalCache.setCache(cachename, null); if
		 * (LocalCache.getCache(cachename)==null) {
		 */
		// 获取邮件模板的HTML
		try {
			// htmlPath=getHtmlUrl(request);
			// mailPath=htmlPath+mailDemo;
			// mailBody = FileRW.readString(path4,"gb2312");
			mailBody = GetHtmlContent.getOneHtml(mailDemo);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
		}
		// 永久缓存
		// LocalCache.setCache(cachename, mailBody);
		/*
		 * } else { mailBody=LocalCache.getCache(cachename).toString(); }
		 */
		mailBody = mailBody.toLowerCase();
		mailBody = mailBody.replaceAll("imagessss/", htmlPath + "mail/images/");
		for (Map.Entry<String, Object> me : hs.entrySet()) {// me是存放hashmap中取出的内容，并用Map.Entry<Integer,
															// String> 指定其泛型;
			if (me.getValue() != null)
				mailBody = mailBody.replace(me.getKey(), me.getValue().toString());
		}

		return mailBody;
	}

	/**
	 * 获取当前页面的URL根目录：如http://happy.mail.10086.cn/club/index.jsp返回的结果为http://
	 * happy.mail.10086.cn/club/
	 * 
	 * @param request
	 * @return
	 */
	public static String getHtmlUrl(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();
		String hostPath_1 = url
				.delete(url.length() - request.getRequestURI().length(), url.length()).append("")
				.toString();
		String hostPath_2 = request.getContextPath().toString();
		String newPath = hostPath_1 + hostPath_2 + "/";
		/*
		 * String mailPathXml=ReadXml.readApp("MailDemoPath"); if(mailPathXml !=
		 * null && mailPathXml.length() > 0) { newPath=mailPathXml; }
		 */
		return newPath;
	}

}
