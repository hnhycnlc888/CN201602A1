package cn.richinfo.service;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.richinfo.dao.SubDao;
import cn.richinfo.dao.UserDao;
import yzkf.model.UserInfo;
import cn.richinfo.pojo.SubscribeModel;

@Service
public class SsologinService {
	@Resource
	UserDao userDao;

	/**
	 * 记录用户从邮箱单点登录信息
	 * 
	 * @param model
	 * @return int
	 * @throws SQLException
	 */
	public int ssolog(cn.richinfo.login.pojo.UserInfo userInfo, String ip, int flag) throws SQLException {
		return userDao.ssolog(userInfo, ip, flag);
	}
}
