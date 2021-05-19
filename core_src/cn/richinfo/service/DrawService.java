package cn.richinfo.service;


import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import cn.richinfo.dao.DrawDao;
import cn.richinfo.login.pojo.UserInfo;
import cn.richinfo.pojo.PrizeInfo;

@Service
public class DrawService {
	
	@Resource
	private DrawDao drawDao;

	

	/**
	 * 抽奖
	 * @param userInfo
	 * @param ip 
	 */
	public Map<String, Object> draw(UserInfo userInfo, String ip) {
		Map<String, Object> map = drawDao.draw(userInfo,ip);
		return map;
	}
	
	/**
	 * 根据号码查询抽奖机会
	 * @param mobile
	 * @return
	 */
	public int getDrawChance(String mobile){
		int chance = drawDao.getChance(mobile);
		return chance;
	}
	
	public List<PrizeInfo> getWinnerList(){
		List<PrizeInfo> winners = drawDao.getWinnerList();
		return winners;
	}
	/**
	 * 增加抽奖机会
	 * @param mobile
	 * @param from 机会来源
	 * @return
	 */
	public int addChance(String mobile,int from){
		return drawDao.addChance(mobile, from);
	}

}
