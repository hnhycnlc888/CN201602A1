package cn.richinfo.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import cn.richinfo.core.mybatis.extend.dao.MybaitsDao;
import cn.richinfo.core.mybatis.extend.utils.MybatisDAOHelper;
import cn.richinfo.login.pojo.UserInfo;
import cn.richinfo.pojo.PrizeInfo;
import cn.richinfo.pojo.WinResult;

@Repository
public class DrawDao {

	@Resource
	private MybaitsDao mybaitsDao;
	
	private String sqlMapId = "DrawDao";
	
	public Map<String, Object> draw(UserInfo userInfo,String ip){
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> pm = new HashMap<String,Object>();
		pm.put("I_MOBILE", userInfo.getUserNumber());
		pm.put("I_PROVCODE", userInfo.getProvCode());
		pm.put("I_AREACODE", userInfo.getAreaCode());
		pm.put("I_CANWIN", 1);
		pm.put("I_LOGINID", userInfo.getLoginid());
		pm.put("I_COMEFROM", "WEB");
		pm.put("I_IP", ip);
		mybaitsDao.selectList(sqlMapId + ".draw",pm);
		int result = MybatisDAOHelper.getIntegerValue(pm, "O_RETURN");
		List<WinResult> list = (List<WinResult>) pm.get("O_CURSOR");
		map.put("result", result);
		WinResult win = new WinResult();
		if(list!=null && list.size()>0){
			win = list.get(0);
		}
		map.put("win", win);
		
		return map;
	}
	
	//根据号码查询用户抽奖机会
	public int getChance(String mobile){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("I_MOBILE", mobile);
		mybaitsDao.selectList(sqlMapId + ".getChanceByMobile",map);
		int chance = MybatisDAOHelper.getIntegerValue(map, "O_RETURN");
		if(chance < 0){
			chance = 0;
		}
		return chance;
	}
	
	//查询所有中奖用户列表
	public List<PrizeInfo> getWinnerList(){
		List<PrizeInfo> winners = mybaitsDao.selectList(sqlMapId+".queryAllPrizes");
		return winners;
	}
	
	/**
	 * 增加用户抽奖机会
	 * @param mobile
	 * @param ctype 获得机会来源，1生成头条，2分享，3订阅杂志
	 * @return
	 */
	public int addChance(String mobile,int ctype){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("I_USERMOBILE", mobile);
		map.put("I_CHANCETYPE", ctype);
		map.put("I_COMEFROM", "WEB");
		mybaitsDao.selectList(sqlMapId + ".addchance",map);
		int result = MybatisDAOHelper.getIntegerValue(map, "O_RETURN");
		return result;
	}
}