package cn.richinfo.service;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.richinfo.dao.SubDao;
import cn.richinfo.pojo.PrizeInfo;
import cn.richinfo.pojo.SubscribeModel;
import cn.richinfo.pojo.User;

@Service
public class SubscribeService {
	@Resource
	SubDao subscribeDao;

	/**
	 * 记录订阅信息
	 * 
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	public int subscribe(SubscribeModel model) throws SQLException {
		return subscribeDao.subscribe(model);
	}
	
	//获取用户已订阅杂志ID
	public List<Integer> getSubscribedIds(String mobile){
		return subscribeDao.querySubcribedIds(mobile);
	}
	
	//测试spring整合mybaits操作mysql
	public List<Integer> deleteAccountTbl(String AccountTblId) throws SQLException {
		return subscribeDao.deleteAccountTbl(AccountTblId);
	}
	
	//测试spring整合mybaits调用存储过程操作mysql
	public int loginAndRegByProcedure(String user_MobileCode, String user_Pwd, String user_RegIP) throws SQLException {
		return subscribeDao.loginAndRegByProcedure(user_MobileCode, user_Pwd, user_RegIP);
	}
	
	//测试spring整合mybaits调用存储过程操作mysql事务测试
	public void updateAccounttByProcedure() throws SQLException {
		subscribeDao.updateAccounttByProcedure();
	}
	
	//查询所有用户结果集
	public List<User> getUsersList(){
		List<User> users = subscribeDao.getUsersList();
		return users;
	}
}
