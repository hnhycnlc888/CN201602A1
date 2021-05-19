package cn.richinfo.pojo;

public class User {
	private int userId; // 用户id
	private String userMobileCode; // 用户手机号码
	private String userPwd; // 用户密码
	private int userVisibility; // 用户可见性
	private int userLevel; // 用户级别
	private String userRegTime; // 用户注册时间
	private String userRegIP; // 用户注册ip
	private int userCollecter;
	private int userCollected;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserMobileCode() {
		return userMobileCode;
	}

	public void setUserMobileCode(String userMobileCode) {
		this.userMobileCode = userMobileCode;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public int getUserVisibility() {
		return userVisibility;
	}

	public void setUserVisibility(int userVisibility) {
		this.userVisibility = userVisibility;
	}

	public int getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	public String getUserRegTime() {
		return userRegTime;
	}

	public void setUserRegTime(String userRegTime) {
		this.userRegTime = userRegTime;
	}

	public String getUserRegIP() {
		return userRegIP;
	}

	public void setUserRegIP(String userRegIP) {
		this.userRegIP = userRegIP;
	}

	public int getUserCollecter() {
		return userCollecter;
	}

	public void setUserCollecter(int userCollecter) {
		this.userCollecter = userCollecter;
	}

	public int getUserCollected() {
		return userCollected;
	}

	public void setUserCollected(int userCollected) {
		this.userCollected = userCollected;
	}
}
