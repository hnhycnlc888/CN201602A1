package cn.richinfo.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import cn.richinfo.core.mybatis.extend.dao.MybaitsDao;
import cn.richinfo.core.mybatis.extend.utils.MybatisDAOHelper;
import cn.richinfo.pojo.PrizeInfo;
import cn.richinfo.pojo.SubscribeModel;
import cn.richinfo.pojo.User;
import cn.richinfo.pojo.WinResult;

@Repository
public class SubDao {
	
	@Resource
	private MybaitsDao mybaitsDao;
	
	private String sqlMapId = "SubDao";
	
	public int subscribe(SubscribeModel model) {
		Map<String, Object> pm = new HashMap<String,Object>();
		pm.put("I_USERMOBILE", model.getUsermobile());
		pm.put("I_PROVCODE", model.getProvcode());
		pm.put("I_AREACODE", model.getAreacode());
		pm.put("I_LOGINID", model.getLoginId());
		pm.put("I_COMEFROM", model.getComefrom());
		pm.put("I_IP", model.getIpaddress());
		pm.put("I_COLUMNID", model.getMagid());
		mybaitsDao.insert(sqlMapId+".subscribe", pm);
		return 0;
	}
	
	//获取用户已订阅杂志ID
	public List<Integer> querySubcribedIds(String mobile){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", mobile);
		return mybaitsDao.selectList(sqlMapId+".getMagazineIdByMobile", map);
	}
	
	//测试spring整合mybaits操作mysql
	public List<Integer> deleteAccountTbl(String AccountTblId) {
		Map<String, Object> pm = new HashMap<String,Object>();
		pm.put("id", AccountTblId);
		return mybaitsDao.selectList(sqlMapId+".deleteAccountTbl", pm);
	}
	
	//测试spring整合mybaits调用存储过程操作mysql
	public int loginAndRegByProcedure(String user_MobileCode, String user_Pwd, String user_RegIP) {
		Map<String, Object> paramMap = new HashMap<String,Object>();
        paramMap.put("user_Pwd", user_Pwd);  
        paramMap.put("user_MobileCode", user_MobileCode);  
        paramMap.put("user_RegIP", user_RegIP); 
		mybaitsDao.selectList(sqlMapId+".loginAndRegByProcedure", paramMap);
		//paramMap.get("userId");
		//List<WinResult> list = (List<WinResult>) paramMap.get("O_CURSOR");
		return MybatisDAOHelper.getIntegerValue(paramMap, "userId");
	}
	
	//测试spring整合mybaits调用存储过程操作mysql事务测试
	public void updateAccounttByProcedure() {
		mybaitsDao.selectList(sqlMapId+".updateAccounttblProcedure");
	}
	
	//查询所有用户结果集
	public List<User> getUsersList(){
		List<User> users = mybaitsDao.selectList(sqlMapId+".queryAllUsers");
		return users;
	}
}