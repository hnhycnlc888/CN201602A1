package cn.richinfo.dao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import yzkf.model.UserInfo;

import cn.richinfo.core.mybatis.extend.dao.MybaitsDao;
import cn.richinfo.core.mybatis.extend.utils.MybatisDAOHelper;
import cn.richinfo.pojo.SubscribeModel;

@Repository
public class UserDao {
	
	@Resource
	private MybaitsDao mybaitsDao;
	
	private String sqlMapId = "UserDao";
	
	public int addChance(String mobile,int type) {
		Map<String, Object> pm = new HashMap<String,Object>();
		pm.put("I_USERMOBILE", mobile);
		pm.put("I_CHANCETYPE", type);
		mybaitsDao.insert(sqlMapId+".addchance", pm);
		return 0;
	}
	
	public int ssolog(cn.richinfo.login.pojo.UserInfo userInfo, String ip, int flag) {
		Map<String, Object> pm = new HashMap<String,Object>();
		pm.put("I_USERMOBILE", userInfo.getUserNumber());
		pm.put("I_PROVCODE", userInfo.getProvCode());
		pm.put("I_AREACODE", userInfo.getAreaCode());
		pm.put("I_LOGINID", userInfo.getLoginid());
		pm.put("I_COMEFROM", "SSO");
		pm.put("I_IP", ip);
		pm.put("I_FLAG", flag);	
		mybaitsDao.insert(sqlMapId+".ssologin", pm);
		int result = MybatisDAOHelper.getIntegerValue(pm, "O_RETURN");
		return result;
	}
	
}